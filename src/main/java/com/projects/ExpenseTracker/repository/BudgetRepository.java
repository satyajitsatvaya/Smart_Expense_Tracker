package com.projects.ExpenseTracker.repository;

import com.projects.ExpenseTracker.model.Budget;
import com.projects.ExpenseTracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget,Long> {
    Optional<Budget> findByUserIdAndCategoryAndYearAndMonth(
                Long userId,
                String category,
                int year,
                int month
        );

    List<Budget> findByUserIdAndYearAndMonth(
            Long userId,
            int year,
            int month
    );


    boolean existsByUserAndCategoryAndYearAndMonth(User user, String category, int year, int month);
}
