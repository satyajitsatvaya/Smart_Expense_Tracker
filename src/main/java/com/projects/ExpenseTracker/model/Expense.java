package com.projects.ExpenseTracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "expenses")
public class Expense extends BaseEntity{

    private String title;
    private BigDecimal amount;
    private String category;
    private LocalDate  expenseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",  nullable = false)
    private User user;

}
