package com.projects.ExpenseTracker.service;

import com.projects.ExpenseTracker.dto.ExpenseRequest;
import com.projects.ExpenseTracker.model.Expense;

public interface ExpenseService {
    void addExpense(ExpenseRequest expenseRequest);
}
