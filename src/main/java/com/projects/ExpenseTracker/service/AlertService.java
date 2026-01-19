package com.projects.ExpenseTracker.service;

import com.projects.ExpenseTracker.dto.AlertResponse;

import java.util.List;

public interface AlertService {
    List<AlertResponse> getMonthlyAlerts(int year, int month);
}
