package com.projects.ExpenseTracker.service;

import com.projects.ExpenseTracker.model.User;
import com.projects.ExpenseTracker.repository.ExpenseRepository;
import com.projects.ExpenseTracker.repository.UserRepository;
import com.projects.ExpenseTracker.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;

    @Override
    public Map<String, BigDecimal> getCategoryWiseSummary() {

        String email= SecurityUtil.getCurrentUserEmail();
        User user=userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("User not found"));


        List<Object[]> result= expenseRepository.getCategoryWiseSummary(user);

        Map<String, BigDecimal> summary = new HashMap<>();

        for(Object[] row : result){
            String category=(String)  row[0];
            BigDecimal amount=(BigDecimal) row[1];
            summary.put(category, amount);

        }

        return summary;
    }

    @Override
    public BigDecimal getMonthlyTotal(int year, int month) {
        String email=SecurityUtil.getCurrentUserEmail();
        User user= userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("User not found"));

        return expenseRepository.getMonthlyTotal(user,year,month);
    }

    @Override
    public BigDecimal getTotalBetweenDates(LocalDate from, LocalDate to) {
        String email=SecurityUtil.getCurrentUserEmail();

        User user= userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("User not found"));

            return expenseRepository.getTotalBetweenDates(user,from,to);
    }

    @Override
    public Map<String, BigDecimal> getCategoryWiseMonthlySummary(int year, int month) {
        String email=SecurityUtil.getCurrentUserEmail();

        User user= userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("User not found"));

        List<Object[]> results= expenseRepository.getCategoryWiseMonthlySummary(user,year,month);

            Map<String, BigDecimal> summary = new HashMap<>();

            for(Object[] row : results){
                String category=(String)  row[0];
                BigDecimal amount=(BigDecimal) row[1];
                summary.put(category, amount);
            }

            return summary;
    }
}
