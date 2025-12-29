package com.projects.ExpenseTracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class UpdateExpenseRequest {
    private String title;
    private BigDecimal amount;
    private String category;
    private LocalDate expenseDate;

}


