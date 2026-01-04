package com.projects.ExpenseTracker.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateBudgetRequest {

    @NotBlank
    private String category;

    @NotNull
    private BigDecimal amount;

    @Min(2025)
    private int year;

    @Min(1)
    @Max(12)
    private int month;
}
