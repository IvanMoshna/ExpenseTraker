package com.moshna.traker.service;

import com.moshna.traker.dto.ExpenseDto;
import com.moshna.traker.model.Expense;
import com.moshna.traker.repo.ExpenseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private final ExpenseRepo expenseRepo;

    public ExpenseService(ExpenseRepo expenseRepo) {
        this.expenseRepo = expenseRepo;
    }

    public String addExpense(String description, String comment, double price, long userId) {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        String timeString = time.toString();
        String timeToExpense = timeString.split("\\.")[0];

        //TODO: check for double
        Expense expense = new Expense(date.toString(), timeToExpense, description, price, comment, userId);
        expenseRepo.save(expense);

        //TODO: return to page where expense added succsesfully
        //return "redirect: /user/"+ userId + "/expenses";
        return "userExpense";
    }

    public List<ExpenseDto> getExpenseDtoList(List<Expense> expenseList) {
        try {
                List<ExpenseDto> expenseDtoList = new ArrayList<>();
                for (Expense expense: expenseList) {
                    expenseDtoList.add(new ExpenseDto(expense.getId(),
                        expense.getDate(), expense.getTime(), expense.getDescription(),
                        expense.getPrice(), expense.getComment(), expense.getUserId()));
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
            result += expenseDto.getPrice();
        }
        return result;
    }

    public float getAverageExpense(List<ExpenseDto> expenseDtoList) {
        if(expenseDtoList.size() == 0) return 0;
        else return getSumOfExpense(expenseDtoList)/expenseDtoList.size();
    }
    /*TODO: check that we are got date or create a calendar
    public String filterByDates(String fromDate, String toDate, Model model) {
        List<ExpenseDto> expenseAllDtoList = getExpenseDtoList(getExpenseList());
        List<ExpenseDto> expenseDtoFilteredList = new ArrayList<>();
        for (ExpenseDto expenseDto: expenseAllDtoList) {
            if(LocalDate.parse(expenseDto.getDate()).isAfter(LocalDate.parse(fromDate)) &&
                    LocalDate.parse(expenseDto.getDate()).isBefore(LocalDate.parse(toDate))) {
                expenseDtoFilteredList.add(expenseDto);
            }
        }
        model.addAttribute("expenses", expenseDtoFilteredList);
        return EXPENSE_PAGE;
    }*/
}
