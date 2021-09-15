package com.moshna.traker.service;

import com.moshna.traker.repo.ExpenseRepo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
class ExpenseServiceTest {

    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private ExpenseRepo repoMockExpense;

    @Test
    void addExpense() {
    }

    @Test
    void getExpenseDtoList() {
    }

    @Test
    void getExpenseList() {
    }

    @Test
    void getSumOfExpense() {
    }

    @Test
    void getAverageExpense() {
    }
}