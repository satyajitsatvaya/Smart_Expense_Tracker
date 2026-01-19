package com.projects.ExpenseTracker.controller;

import com.projects.ExpenseTracker.dto.AlertResponse;
import com.projects.ExpenseTracker.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {
    public final AlertService alertService;

    @GetMapping
    public ResponseEntity<List<AlertResponse>> getMonthlyAlerts(
            @RequestParam int year,  @RequestParam int month ) {
        return ResponseEntity.ok(alertService.getMonthlyAlerts(year, month));
    }
}
