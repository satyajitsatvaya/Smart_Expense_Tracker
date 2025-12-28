package com.projects.ExpenseTracker.service;

import com.projects.ExpenseTracker.dto.ExpenseRequest;
import com.projects.ExpenseTracker.model.Expense;
import com.projects.ExpenseTracker.model.User;
import com.projects.ExpenseTracker.repository.ExpenseRepository;
import com.projects.ExpenseTracker.repository.UserRepository;
import com.projects.ExpenseTracker.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService{

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    @Override
    public void addExpense(ExpenseRequest request) {

        String email= SecurityUtil.getCurrentUserEmail();

        User user= userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not found"));

        Expense expense= new  Expense();
        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setExpenseDate(request.getExpenseDate());

        expense.setUser(user);

        expenseRepository.save(expense);


    }
}
