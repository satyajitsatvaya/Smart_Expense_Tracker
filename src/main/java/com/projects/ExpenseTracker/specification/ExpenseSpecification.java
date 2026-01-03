package com.projects.ExpenseTracker.specification;

import com.projects.ExpenseTracker.model.Expense;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseSpecification {

    public static Specification<Expense> withFilters(
            Long userId,
            String category,
            LocalDate from,
            LocalDate to,
            BigDecimal minAmount,
            BigDecimal maxAmount
    ){

        return ((root, query, criteriaBuilder) -> {

            Predicate predicate=criteriaBuilder.conjunction();

            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.equal(root.get("user").get("id"), userId)  );

            // Category
            if(category!=null && !category.isBlank()){
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(root.get("category"), category)  );
            }

            // Start Date
            if(from!=null){
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("expenseDate"), from) );
            }

            //End Date
            if(to!=null){
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("expenseDate"), to) );
            }

            //Min Amount
            if(minAmount!=null){
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), minAmount) );
            }

            //Max Amount
            if(maxAmount!=null){
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("amount"), maxAmount) );
            }


            return predicate;
        });
    }
}
