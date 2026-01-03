package com.projects.ExpenseTracker.controller;

import com.projects.ExpenseTracker.dto.CreateExpenseRequest;
import com.projects.ExpenseTracker.dto.ExpenseResponse;
import com.projects.ExpenseTracker.dto.UpdateExpenseRequest;
import com.projects.ExpenseTracker.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

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

//    @GetMapping
//    public ResponseEntity<Page<ExpenseResponse>> getAllExpenses(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        return ResponseEntity.ok(expenseService.getExpenses(page,size));
//
//    }

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

    @GetMapping
    public ResponseEntity<Page<ExpenseResponse>> getFilteredExpenses(
            @RequestParam(required = false) String category,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to,

            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,

            @PageableDefault(size = 10, sort = "expenseDate")
            Pageable pageable
    ){
        Page<ExpenseResponse> expenses= expenseService.getFilteredExpenses(
                category, from, to, minAmount, maxAmount, pageable);

        return ResponseEntity.ok(expenses);
    }

}
