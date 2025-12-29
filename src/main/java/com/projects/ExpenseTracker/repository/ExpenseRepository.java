package com.projects.ExpenseTracker.repository;

import com.projects.ExpenseTracker.model.Expense;
import com.projects.ExpenseTracker.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(User user);
    Page<Expense> findByUser(User user, Pageable pageable);

    Optional<Expense> findByIdAndUser(Long id, User user);

    void deleteByIdAndUser(Long id, User user);

}
