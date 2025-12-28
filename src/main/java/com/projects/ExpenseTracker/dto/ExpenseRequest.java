package com.projects.ExpenseTracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ExpenseRequest {
    private String title;
    private BigDecimal amount;
    private String category;
    private LocalDate expenseDate;

}
