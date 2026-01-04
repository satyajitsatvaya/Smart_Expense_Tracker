package com.projects.ExpenseTracker.service.impl;

import com.projects.ExpenseTracker.dto.BudgetResponse;
import com.projects.ExpenseTracker.dto.CreateBudgetRequest;
import com.projects.ExpenseTracker.model.Budget;
import com.projects.ExpenseTracker.model.User;
import com.projects.ExpenseTracker.repository.BudgetRepository;
import com.projects.ExpenseTracker.repository.UserRepository;
import com.projects.ExpenseTracker.util.SecurityUtil;
import com.projects.ExpenseTracker.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    @Override
    public BudgetResponse createOrUpdateBudget(CreateBudgetRequest request) {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

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

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

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
        response.setCategory(budget.getCategory());
        response.setYear(budget.getYear());
        response.setMonth(budget.getMonth());
        response.setBudgetAmount(budget.getAmount());
        return response;
    }

    @Override
    public List<BudgetResponse> getBudgetsForMonth(int year, int month) {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Budget> budgets = budgetRepository
                .findByUserIdAndYearAndMonth(user.getId(), year, month);

        return budgets.stream()
                .map(this::mapToResponse)
                .toList();
    }

}
