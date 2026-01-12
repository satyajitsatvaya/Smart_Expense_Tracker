package com.projects.ExpenseTracker.controller;

import com.projects.ExpenseTracker.dto.BudgetResponse;
import com.projects.ExpenseTracker.dto.BudgetUsageResponse;
import com.projects.ExpenseTracker.dto.CreateBudgetRequest;
import com.projects.ExpenseTracker.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    private void validateMonth(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
    }
    private void validateYear(int year) {
        int currentYear = LocalDate.now().getYear();
        if (year < 2000 || year > currentYear + 1) {
            throw new IllegalArgumentException("Invalid year");
        }
    }


    @PostMapping
    public ResponseEntity<BudgetResponse> createOrUpdate(
            @RequestBody @Valid CreateBudgetRequest request) {

        return ResponseEntity.ok(
                budgetService.createOrUpdateBudget(request)
        );
    }

    @GetMapping
    public ResponseEntity<BudgetResponse> getBudget(
            @RequestParam String category,
            @RequestParam int year,
            @RequestParam int month) {

        validateMonth(month);
        validateYear(year);
        return ResponseEntity.ok(
                budgetService.getBudget(category, year, month)
        );
    }

    @GetMapping("/month")
    public ResponseEntity<List<BudgetResponse>> getBudgetsForMonth(
            @RequestParam int year,
            @RequestParam int month) {

        validateMonth(month);
        validateYear(year);

        return ResponseEntity.ok(
                budgetService.getBudgetsForMonth(year, month)
        );
    }

    @GetMapping("/usage")
    public ResponseEntity<BudgetUsageResponse> getBudgetUsage(
            @RequestParam(required = false ) String category,
            @RequestParam int year,
            @RequestParam int month ) {

        validateMonth(month);
        validateYear(year);
        return ResponseEntity.ok(
                budgetService.getMonthlyBudgetUsage(category, year, month) );
    }

}

