package com.projects.ExpenseTracker.service.impl;

import com.projects.ExpenseTracker.dto.AlertResponse;
import com.projects.ExpenseTracker.dto.AlertType;
import com.projects.ExpenseTracker.model.Budget;
import com.projects.ExpenseTracker.model.User;
import com.projects.ExpenseTracker.repository.BudgetRepository;
import com.projects.ExpenseTracker.repository.UserRepository;
import com.projects.ExpenseTracker.service.AlertService;
import com.projects.ExpenseTracker.service.AnalyticsService;
import com.projects.ExpenseTracker.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private static final BigDecimal WARNING_THRESHOLD = BigDecimal.valueOf(0.8);
    private final BudgetRepository budgetRepository;
    private final AnalyticsService analyticsService;
    private final UserRepository userRepository;

    @Override
    public List<AlertResponse> getMonthlyAlerts(int year, int month) {

        String email = SecurityUtil.getCurrentUserEmail();

        if (email == null) {
            throw new RuntimeException("Unauthorized");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Long userId = user.getId();

        List<AlertResponse> alerts= new ArrayList<>();

        List<Budget> budgets = budgetRepository.findByUserIdAndYearAndMonth(userId, year, month);

        Map<String,Budget> categoryBudgetMap = budgets.stream()
                .filter(b -> !b.isOverallBudget())
                .collect(Collectors.toMap(
                        Budget::getCategory,
                        b -> b
                ));

        // Total Spent in the month
        BigDecimal totalSpent = analyticsService.getMonthlyTotal(year, month);

        Map<String, BigDecimal> categorySpentMap =
                analyticsService.getCategoryWiseMonthlySummary(year, month);

        // Handles OverAll Budget
         Budget overallBudget = budgets.stream()
                 .filter(Budget::isOverallBudget)
                 .findFirst()
                 .orElse(null);

     if (overallBudget != null){
             evaluateBudget(
                     alerts,  "OVERALL",
                     totalSpent,  overallBudget.getAmount()  );
     }
     else if (totalSpent.compareTo(BigDecimal.ZERO) > 0){
         alerts.add(noBudgetAlert("OVERALL",totalSpent));
     }

     // Handles Category Budget
        for (Map.Entry<String,BigDecimal> entry : categorySpentMap.entrySet()) {
            String category = entry.getKey();
            BigDecimal spent = entry.getValue();

            Budget categoryBudget = categoryBudgetMap.get(category);
            if (categoryBudget != null) {
                evaluateBudget(alerts, category, spent, categoryBudget.getAmount());
            } else if (spent.compareTo(BigDecimal.ZERO) > 0) {
                alerts.add(noBudgetAlert(category, spent));
            }
        }

        return alerts;
    }


    // Helper Methods

    private void evaluateBudget
            (List<AlertResponse> alerts, String category, BigDecimal spent, BigDecimal budgetAmount) {

        if(budgetAmount == null || budgetAmount.compareTo(BigDecimal.ZERO) <= 0){
            if(spent.compareTo(BigDecimal.ZERO) > 0){
                alerts.add(noBudgetAlert(category, spent));
            }
            return;
        }
        if(spent.compareTo(budgetAmount) > 0){
            alerts.add(
                    buildAlert(
                            AlertType.BUDGET_EXCEEDED,
                            category,
                            spent,
                            budgetAmount,
                            "You Exceeded your " + category + " budget"  )
            );
            return;
        }

        // Warning
        BigDecimal usageRatio = spent.divide(budgetAmount,2, RoundingMode.HALF_UP);

        if(usageRatio.compareTo(WARNING_THRESHOLD) > 0){
            alerts.add (buildAlert(
                    AlertType.BUDGET_WARNING,
                    category,
                    spent,
                    budgetAmount,
                    "You have used over 80% of your " + category + " budget")
            );

        }
    }


    private AlertResponse noBudgetAlert(String category, BigDecimal spent) {
        return buildAlert(
                AlertType.NO_BUDGET_SET,
                category,
                spent,
                null,
                "No budget set for " + category
        );
    }

    private AlertResponse buildAlert
            (AlertType alertType, String category,
             BigDecimal spent, BigDecimal budgetAmount, String message) {

        AlertResponse alert = new AlertResponse();

        alert.setAlertType(alertType);
        alert.setCategory(category);
        alert.setSpentAmount(spent);
        alert.setBudgetAmount(budgetAmount);
        alert.setMessage(message);

        return alert;
    }
}
