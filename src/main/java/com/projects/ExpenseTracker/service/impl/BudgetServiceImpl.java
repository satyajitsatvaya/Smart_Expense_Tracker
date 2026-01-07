package com.projects.ExpenseTracker.service.impl;

import com.projects.ExpenseTracker.dto.BudgetResponse;
import com.projects.ExpenseTracker.dto.BudgetUsageResponse;
import com.projects.ExpenseTracker.dto.CreateBudgetRequest;
import com.projects.ExpenseTracker.model.Budget;
import com.projects.ExpenseTracker.model.User;
import com.projects.ExpenseTracker.repository.BudgetRepository;
import com.projects.ExpenseTracker.repository.UserRepository;
import com.projects.ExpenseTracker.service.AnalyticsService;
import com.projects.ExpenseTracker.util.SecurityUtil;
import com.projects.ExpenseTracker.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final AnalyticsService analyticsService;


    public User findCurrentUser() {
        String email = SecurityUtil.getCurrentUserEmail();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public BudgetResponse createOrUpdateBudget(CreateBudgetRequest request) {

        User user = findCurrentUser();

        if(request.getCategory() != null && request.getCategory().isBlank()) {
            throw new IllegalArgumentException("Category cannot be blank");
        }

        Budget budget = budgetRepository
                .findByUserIdAndCategoryAndYearAndMonth(
                        user.getId(),
                        request.getCategory(),
                        request.getYear(),
                        request.getMonth()
                )
                .orElse(new Budget());

        budget.setUser(user);
        budget.setCategory(request.getCategory());
        budget.setYear(request.getYear());
        budget.setMonth(request.getMonth());
        budget.setAmount(request.getAmount());

        Budget saved = budgetRepository.save(budget);

        return mapToResponse(saved);
    }

    @Override
    public BudgetResponse getBudget(String category, int year, int month) {

        User user = findCurrentUser();

        Budget budget = budgetRepository
                .findByUserIdAndCategoryAndYearAndMonth(
                        user.getId(),
                        category,
                        year,
                        month
                )
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        return mapToResponse(budget);
    }

    private BudgetResponse mapToResponse(Budget budget) {
        BudgetResponse response = new BudgetResponse();
        response.setId(budget.getId());
        response.setCategory(
                budget.getCategory() == null ? "OVERALL" : budget.getCategory() );
        response.setYear(budget.getYear());
        response.setMonth(budget.getMonth());
        response.setBudgetAmount(budget.getAmount());
        return response;
    }

    @Override
    public List<BudgetResponse> getBudgetsForMonth(int year, int month) {

        User user = findCurrentUser();

        List<Budget> budgets = budgetRepository
                .findByUserIdAndYearAndMonth(user.getId(), year, month);

        return budgets.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BudgetUsageResponse getMonthlyBudgetUsage(String category, int year, int month) {
        User user = findCurrentUser();
        Long userId = user.getId();

        Budget budget;

        if(category == null || category.equalsIgnoreCase("OVERALL")) {
            budget = budgetRepository
                    .findByUserIdAndCategoryIsNullAndYearAndMonth(
                            userId, year, month )
                    .orElseThrow(() -> new RuntimeException("Overall Budget not found"));

        } else {
            budget = budgetRepository
                    .findByUserIdAndCategoryAndYearAndMonth(
                            userId, category, year, month )
                    .orElseThrow(() -> new RuntimeException("Category Budget not found"));
        }

        BigDecimal spentAmount;
        if(budget.isOverallBudget()){
            spentAmount = analyticsService.getMonthlyTotal(year, month);
        } else {
            spentAmount = analyticsService.getCategoryWiseMonthlySummary(year, month)
                    .getOrDefault(budget.getCategory(), BigDecimal.ZERO);
        }

        BigDecimal remaining =
                budget.getAmount().subtract(spentAmount);


        BudgetUsageResponse response = new BudgetUsageResponse();
        response.setCategory(budget.isOverallBudget() ? "OVERALL" : budget.getCategory());
        response.setBudgetAmount(budget.getAmount());
        response.setSpentAmount(spentAmount);
        response.setRemainingAmount(remaining);
        response.setOverSpent(remaining.compareTo(BigDecimal.ZERO) < 0);

        return response;
    }

}
