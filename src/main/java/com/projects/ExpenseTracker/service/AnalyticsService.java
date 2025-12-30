package com.projects.ExpenseTracker.service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public interface AnalyticsService {

    Map<String, BigDecimal> getCategoryWiseSummary();
    BigDecimal getMonthlyTotal(int year, int month);
    BigDecimal getTotalBetweenDates(LocalDate from, LocalDate to);

    Map<String,BigDecimal> getCategoryWiseMonthlySummary(int year,int month);

}
