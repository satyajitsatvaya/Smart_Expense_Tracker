package com.projects.ExpenseTracker.repository;

import com.projects.ExpenseTracker.model.Expense;
import com.projects.ExpenseTracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {

    Optional<Expense> findByIdAndUser(Long id, User user);

    @Query("SELECT e.category, SUM(e.amount) FROM Expense e WHERE e.user = :user GROUP BY e.category")
    List<Object[]> getCategoryWiseSummary(User user);


    @Query("""
    SELECT COALESCE(SUM(e.amount), 0)
    FROM Expense e
    WHERE e.user = :user
      AND YEAR(e.expenseDate) = :year
      AND MONTH(e.expenseDate) = :month
""")
    BigDecimal getMonthlyTotal(User user, int year, int month);

    @Query("""
    SELECT COALESCE(SUM(e.amount), 0)
    FROM Expense e
    WHERE e.user = :user
      AND e.expenseDate BETWEEN :from AND :to
""")
    BigDecimal getTotalBetweenDates( User user, LocalDate from, LocalDate to);

    @Query("""
    SELECT e.category, COALESCE(SUM(e.amount), 0)
    FROM Expense e
    WHERE e.user = :user
      AND YEAR(e.expenseDate) = :year
      AND MONTH(e.expenseDate) = :month
    GROUP BY e.category
""")
    List<Object[]> getCategoryWiseMonthlySummary(
            User user,
            int year,
            int month
    );


}
