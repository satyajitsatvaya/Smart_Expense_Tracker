package com.projects.ExpenseTracker.controller;

import com.projects.ExpenseTracker.dto.CreateExpenseRequest;
import com.projects.ExpenseTracker.dto.ExpenseResponse;
import com.projects.ExpenseTracker.dto.UpdateExpenseRequest;
import com.projects.ExpenseTracker.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<String> addExpense(@Valid @RequestBody CreateExpenseRequest request) {
        expenseService.addExpense(request);
        return ResponseEntity.ok("Expense Added Successfully");
    }

    @GetMapping
    public ResponseEntity<Page<ExpenseResponse>> getAllExpenses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(expenseService.getExpenses(page,size));

    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateExpense(
            @PathVariable Long id,
            @RequestBody UpdateExpenseRequest request) {

        expenseService.updateExpense(id, request);
        return ResponseEntity.ok("Expense updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id) {

        expenseService.deleteExpense(id);
        return ResponseEntity.ok("Expense deleted");
    }

}
