package com.projects.ExpenseTracker.service;

import com.projects.ExpenseTracker.dto.CreateExpenseRequest;
import com.projects.ExpenseTracker.dto.ExpenseResponse;
import com.projects.ExpenseTracker.dto.UpdateExpenseRequest;
import org.springframework.data.domain.Page;

public interface ExpenseService {
    void addExpense(CreateExpenseRequest createExpenseRequest);
    Page<ExpenseResponse> getExpenses(int page, int size);
    void updateExpense(Long expenseId, UpdateExpenseRequest request);
    void deleteExpense(Long expenseId);
}
