package com.projects.ExpenseTracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter @Getter
public class AlertResponse {
    private AlertType alertType;
    private String category;
    private BigDecimal spentAmount;
    private BigDecimal BudgetAmount;
    private String message;
}
