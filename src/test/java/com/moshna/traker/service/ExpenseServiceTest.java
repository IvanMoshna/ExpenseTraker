package com.moshna.traker.service;

import com.moshna.traker.TestConfiguration;
import com.moshna.traker.dto.ExpenseDto;
import com.moshna.traker.model.Expense;
import com.moshna.traker.repo.ExpenseRepo;
import com.moshna.traker.utils.ExpenseUtils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class ExpenseServiceTest {

    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private ExpenseRepo repoMockExpense;

    /*@Test
    void addExpense() {

        //String result = expenseService.addExpense("testDesc", "testComment", BigDecimal.ONE, 1L);
        //TODO: how testing that?

    }*/

    @Test
    public void getExpenseDtoList() {
        Expense expense = ExpenseUtils.TEST_EXPENSE;

        List<Expense> expenseList = Collections.singletonList(expense);
        List<ExpenseDto> expenseDtoList = expenseService.getExpenseDtoList(expenseList);
        ExpenseDto expenseDto = expenseDtoList.get(0);

        Assertions.assertNotNull(expenseList);
        Assertions.assertNotNull(expenseDtoList);
        Assertions.assertNotNull(expenseDto);
        Assertions.assertEquals(expenseList.size(), expenseDtoList.size());
        Assertions.assertEquals(expenseDto.getId(), expense.getId());
        Assertions.assertEquals(expenseDto.getComment(), expense.getComment());
        Assertions.assertEquals(expenseDto.getPrice(), expense.getPrice());
    }

    @Test
    public void getExpenseList() {
        Expense expense = ExpenseUtils.TEST_EXPENSE;
        Mockito.when(repoMockExpense.findAllByUserId(expense.getUserId())).thenReturn(Collections.singletonList(expense));

        List<Expense> expenseList = expenseService.getExpenseList(expense.getUserId());

        Assertions.assertNotNull(expenseList);
        Assertions.assertEquals(1, expenseList.size());
    }

    @Test
    public void getExpenseList_ReturnNull() {
        Expense expense = ExpenseUtils.TEST_EXPENSE;
        Mockito.when(repoMockExpense.findAllByUserId(expense.getUserId())).thenReturn(null);

        List<Expense> expenseList = expenseService.getExpenseList(expense.getUserId());

        Assertions.assertNotNull(expenseList);
        Assertions.assertEquals(0, expenseList.size());
    }

    @Test
    public void getSumOfExpense() {
    }

    @Test
    public void getAverageExpense() {
    }
}