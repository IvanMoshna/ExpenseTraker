package com.moshna.traker.service;

import com.moshna.traker.model.Expense;
import com.moshna.traker.repo.ExpenseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    @Autowired
    private final ExpenseRepo expenseRepo;

    private static final String EXPENSE_PAGE = "expense";

    public ExpenseService(ExpenseRepo expenseRepo) {
        this.expenseRepo = expenseRepo;
    }

    public String addExpense(String date, String time, String description, String comment, float price) {
        //TODO: remove date and time from constructor, do it automatically
        Expense expense = new Expense(date,time, description, price, comment);
        expenseRepo.save(expense);

        return EXPENSE_PAGE;
    }
}
