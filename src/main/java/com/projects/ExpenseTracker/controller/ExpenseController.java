package com.projects.ExpenseTracker.controller;

import com.projects.ExpenseTracker.dto.ExpenseRequest;
import com.projects.ExpenseTracker.model.Expense;
import com.projects.ExpenseTracker.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<String> addExpense(@RequestBody ExpenseRequest request) {
        expenseService.addExpense(request);
        return ResponseEntity.ok("Expense Added Successfully");
    }
}
