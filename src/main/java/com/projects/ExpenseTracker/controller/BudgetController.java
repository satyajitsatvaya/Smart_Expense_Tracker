package com.projects.ExpenseTracker.controller;

import com.projects.ExpenseTracker.dto.BudgetResponse;
import com.projects.ExpenseTracker.dto.BudgetUsageResponse;
import com.projects.ExpenseTracker.dto.CreateBudgetRequest;
import com.projects.ExpenseTracker.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

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

        return ResponseEntity.ok(
                budgetService.getBudget(category, year, month)
        );
    }

    @GetMapping("/month")
    public ResponseEntity<List<BudgetResponse>> getBudgetsForMonth(
            @RequestParam int year,
            @RequestParam int month) {

        return ResponseEntity.ok(
                budgetService.getBudgetsForMonth(year, month)
        );
    }

    @GetMapping("/usage")
    public ResponseEntity<BudgetUsageResponse> getBudgetUsage(
            @RequestParam(required = false) String category,
            @RequestParam int year,
            @RequestParam int month ) {

        return ResponseEntity.ok(
                budgetService.getMonthlyBudgetUsage(category, year, month) );
    }

}

