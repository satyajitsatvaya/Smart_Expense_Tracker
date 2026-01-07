package com.projects.ExpenseTracker.service;

import com.projects.ExpenseTracker.dto.BudgetResponse;
import com.projects.ExpenseTracker.dto.BudgetUsageResponse;
import com.projects.ExpenseTracker.dto.CreateBudgetRequest;

import java.util.List;


public interface BudgetService {

    BudgetResponse createOrUpdateBudget(CreateBudgetRequest budgetRequest);

    BudgetResponse getBudget(String category, int year, int month);

    List<BudgetResponse> getBudgetsForMonth(int year, int month);

    BudgetUsageResponse getMonthlyBudgetUsage(String category, int year, int month);
}
