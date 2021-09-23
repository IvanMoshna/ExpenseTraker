package com.moshna.traker.utils;

import com.moshna.traker.model.Expense;
import com.moshna.traker.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class ExpenseUtils {

    public static final Expense TEST_EXPENSE = getExpense();

    public static Expense getExpense(){
        return  new Expense(LocalDate.now(), LocalTime.now(), "testDesc", BigDecimal.valueOf(10), "testComment", new User());
    }
}
