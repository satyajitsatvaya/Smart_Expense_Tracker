package com.projects.ExpenseTracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "budgets",
        uniqueConstraints = { @UniqueConstraint(  columnNames = {
                                "user_id",  "category",  "budget_year", "budget_month"  }  )
        }  )
@Getter
@Setter
public class Budget extends BaseEntity {

    @Column(nullable = false)
    private String category;

    @Column(name = "budget_year", nullable = false)
    private int year;

    @Column(name = "budget_month", nullable = false)
    private int month;

    @Column(name = "budget_amount", nullable = false)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
