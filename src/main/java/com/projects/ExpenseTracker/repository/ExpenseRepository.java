package com.projects.ExpenseTracker.repository;

import com.projects.ExpenseTracker.model.Expense;
import com.projects.ExpenseTracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(User user);
}
