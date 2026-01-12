package com.projects.ExpenseTracker.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BudgetUsageResponse {
    private  String category;
    private BigDecimal budgetAmount;
    private BigDecimal spentAmount;
    private BigDecimal remainingAmount;
    private Integer usagePercentage;
    private boolean overSpent;


}
