package com.projects.ExpenseTracker.service;


import java.math.BigDecimal;
import java.util.Map;

public interface AnalyticsService {

    Map<String, BigDecimal> getCategoryWiseSummary();
    BigDecimal getMonthlyTotal(int year, int month);
}
