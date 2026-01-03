package com.projects.ExpenseTracker.service;

import com.projects.ExpenseTracker.dto.CreateExpenseRequest;
import com.projects.ExpenseTracker.dto.ExpenseResponse;
import com.projects.ExpenseTracker.dto.UpdateExpenseRequest;
import com.projects.ExpenseTracker.model.Expense;
import com.projects.ExpenseTracker.model.User;
import com.projects.ExpenseTracker.repository.ExpenseRepository;
import com.projects.ExpenseTracker.repository.UserRepository;
import com.projects.ExpenseTracker.specification.ExpenseSpecification;
import com.projects.ExpenseTracker.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService{

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    @Override
    public void addExpense(CreateExpenseRequest request) {

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


    @Override
    public Page<ExpenseResponse> getExpenses(int page, int size) {
        String email= SecurityUtil.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not found"));

        Pageable pageable= PageRequest.of(page,size, Sort.by("expenseDate").descending());
        return expenseRepository.findByUser(user,pageable)
                .map(this::mapToResponse);
    }

    //   Map to Response Generator

    private ExpenseResponse mapToResponse(Expense expense) {

        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setTitle(expense.getTitle());
        response.setAmount(expense.getAmount());
        response.setCategory(expense.getCategory());
        response.setExpenseDate(expense.getExpenseDate());

        return response;
    }


    @Override
    public void updateExpense(Long expenseId, UpdateExpenseRequest request) {
        String email=SecurityUtil.getCurrentUserEmail();
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not found"));

        Expense expense= expenseRepository.findByIdAndUser(expenseId,user)
                .orElseThrow(()->new RuntimeException("Expense not found"));

        if (request.getTitle() != null) {
            expense.setTitle(request.getTitle());
        }
        if (request.getAmount() != null) {
            expense.setAmount(request.getAmount());
        }
        if (request.getCategory() != null) {
            expense.setCategory(request.getCategory());
        }
        if (request.getExpenseDate() != null) {
            expense.setExpenseDate(request.getExpenseDate());
        }

        expenseRepository.save(expense);
    }

    @Override
    public void deleteExpense(Long expenseId) {

        String email = SecurityUtil.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Expense expense = expenseRepository
                .findByIdAndUser(expenseId, user)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        expenseRepository.delete(expense);
    }

    @Override
    public Page<ExpenseResponse> getFilteredExpenses(String category, LocalDate from, LocalDate to, BigDecimal minAmount, BigDecimal maxAmount, Pageable pageable) {
        String email= SecurityUtil.getCurrentUserEmail();

        User user= userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        var specification = ExpenseSpecification.withFilters(
                user.getId(),
                category, from , to, minAmount, maxAmount
        );

        Page<Expense> expenses=expenseRepository.findAll(specification, pageable);

        return expenses.map(this::mapToResponse);
    }


}
