package com.projects.ExpenseTracker.controller;

import com.projects.ExpenseTracker.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/category-summary")
    public ResponseEntity<Map<String, BigDecimal>> getCategorySummary() {
        return ResponseEntity.ok(
                analyticsService.getCategoryWiseSummary()
        );
    }

    @GetMapping("/monthly")
    public ResponseEntity<Map<String, Object>> getMonthly(@RequestParam int year, @RequestParam int month) {
        BigDecimal monthlyTotal = analyticsService.getMonthlyTotal(year, month);

        Map<String, Object> response = new HashMap<>();

        response.put("year", year);
        response.put("Month", Month.of(month).name());
        response.put("Total",  monthlyTotal);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date-range")
    public ResponseEntity<Map<String, Object>> getDateRange(
            @RequestParam LocalDate from, @RequestParam LocalDate to) {

        BigDecimal total =analyticsService.getTotalBetweenDates(from ,to);

        Map<String, Object> response = new HashMap<>();
        response.put("from", from);
        response.put("to", to);
        response.put("totalExpense", total);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/category-monthly")
    public ResponseEntity<Map<String, Object>> getMonthlyExpense(@RequestParam int year, @RequestParam int month) {
        Map<String, BigDecimal> summary = analyticsService.getCategoryWiseMonthlySummary(year, month);

        Map<String ,Object> response = new HashMap<>();
        response.put("Year",year);
        response.put("Month",Month.of(month).name());
        response.put("Total",  summary);

        return ResponseEntity.ok(response);
    }
}
