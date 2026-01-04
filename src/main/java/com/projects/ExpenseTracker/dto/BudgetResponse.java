package com.projects.ExpenseTracker.dto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class BudgetResponse {

    private long id;
    private String category;
    private int year;
    private int month;
    private BigDecimal budgetAmount;
}
