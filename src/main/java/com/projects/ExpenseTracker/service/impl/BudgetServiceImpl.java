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
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

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

        boolean isOverallRequest =
                category == null || category.isBlank() || category.equalsIgnoreCase("OVERALL");

        BigDecimal spentAmount = isOverallRequest
                ? analyticsService.getMonthlyTotal(year, month)
                : analyticsService
                .getCategoryWiseMonthlySummary(year, month)
                .getOrDefault(category, BigDecimal.ZERO);


        Optional<Budget> optionalBudget = isOverallRequest
                ? budgetRepository.findByUserIdAndCategoryIsNullAndYearAndMonth(userId, year, month)
                : budgetRepository.findByUserIdAndCategoryIgnoreCaseAndYearAndMonth(
                userId, category, year, month);

        // No budget exists (but spending may exist)
        if (!isOverallRequest && optionalBudget.isEmpty()) {

            BudgetUsageResponse response = new BudgetUsageResponse();
            response.setCategory(category);
            response.setSpentAmount(spentAmount);
            response.setBudgetAmount(null);
            response.setRemainingAmount(null);
            response.setUsagePercentage(null);
            response.setOverSpent(false);
            return response;
        }

        //  OVERALL budget
        Budget budget = optionalBudget
                .orElseThrow(() -> new RuntimeException("Overall budget not found"));


        BigDecimal remaining = budget.getAmount().subtract(spentAmount);

        Integer usagePercentage = budget.getAmount().compareTo(BigDecimal.ZERO) == 0
                ? null
                : spentAmount
                .multiply(BigDecimal.valueOf(100))
                .divide(budget.getAmount(), 0, RoundingMode.HALF_UP)
                .intValue();

        boolean overSpent = remaining.compareTo(BigDecimal.ZERO) < 0;

        BudgetUsageResponse response = new BudgetUsageResponse();
        response.setCategory("OVERALL");
        response.setBudgetAmount(budget.getAmount());
        response.setSpentAmount(spentAmount);
        response.setRemainingAmount(remaining);
        response.setUsagePercentage(usagePercentage);
        response.setOverSpent(overSpent);

        return response;
    }



}
