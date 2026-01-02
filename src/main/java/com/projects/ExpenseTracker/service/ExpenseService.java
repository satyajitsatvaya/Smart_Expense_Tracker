package com.projects.ExpenseTracker.service;

import com.projects.ExpenseTracker.dto.CreateExpenseRequest;
import com.projects.ExpenseTracker.dto.ExpenseResponse;
import com.projects.ExpenseTracker.dto.UpdateExpenseRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ExpenseService {
    void addExpense(CreateExpenseRequest createExpenseRequest);
    Page<ExpenseResponse> getExpenses(int page, int size);
    void updateExpense(Long expenseId, UpdateExpenseRequest request);
    void deleteExpense(Long expenseId);

    Page<ExpenseResponse> getFilteredExpenses(
            String category,
            LocalDate from,
            LocalDate to,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            Pageable pageable
    );
}
