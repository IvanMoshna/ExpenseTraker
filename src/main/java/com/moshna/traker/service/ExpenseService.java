package com.moshna.traker.service;

import com.moshna.traker.dto.ExpenseDto;
import com.moshna.traker.mapper.ExpenseMapping;
import com.moshna.traker.model.Expense;
import com.moshna.traker.model.User;
import com.moshna.traker.repo.ExpenseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepo expenseRepo;

    public void addExpense(String description, String comment, BigDecimal price, User user) {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        Expense expense = new Expense(date, time, description, price, comment, user);
        expenseRepo.save(expense);
    }

    public List<ExpenseDto> getExpenseDtoList(List<Expense> expenseList) {
        try {
                List<ExpenseDto> expenseDtoList = new ArrayList<>();
                for (Expense expense: expenseList) {
                    expenseDtoList.add(ExpenseMapping.toExpenseDTO(expense));
            }
            return expenseDtoList;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<Expense> getExpenseList(Long userId) {
        try {
                Iterable<Expense> expenses = expenseRepo.findAllByUserId(userId);
                List<Expense> expenseList = new ArrayList<>();
                expenses.forEach(expense -> expenseList.add(expense));
                return expenseList;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public float getSumOfExpense(List<ExpenseDto> expenseDtoList) {
        float result = 0;
        for (ExpenseDto expenseDto: expenseDtoList) {
            result += expenseDto.getPrice().floatValue();
        }
        return result;
    }

    public float getAverageExpense(List<ExpenseDto> expenseDtoList) {
        if(expenseDtoList.isEmpty()) return 0;
        else return getSumOfExpense(expenseDtoList)/expenseDtoList.size();
    }

    public List<ExpenseDto> filterByDates(Long id, String fromDate, String toDate) {
        List<ExpenseDto> expenseAllDtoList = getExpenseDtoList(getExpenseList(id));
        List<ExpenseDto> expenseDtoFilteredList = new ArrayList<>();
        for (ExpenseDto expenseDto: expenseAllDtoList) {
            if(expenseDto.getDate().isAfter(LocalDate.parse(fromDate)) &&
                    expenseDto.getDate().isBefore(LocalDate.parse(toDate))) {
                expenseDtoFilteredList.add(expenseDto);
            }
        }
        if(expenseDtoFilteredList.isEmpty()){
            return Collections.emptyList();
        }
        else {
            return expenseDtoFilteredList;
        }
    }
}
