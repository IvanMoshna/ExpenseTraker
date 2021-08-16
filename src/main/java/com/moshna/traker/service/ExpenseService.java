package com.moshna.traker.service;

import com.moshna.traker.dto.ExpenseDto;
import com.moshna.traker.model.Expense;
import com.moshna.traker.repo.ExpenseRepo;
import org.hibernate.type.LocalDateTimeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.jws.WebParam;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private final ExpenseRepo expenseRepo;

    private static final String EXPENSE_PAGE = "expense";

    public ExpenseService(ExpenseRepo expenseRepo) {
        this.expenseRepo = expenseRepo;
    }

    public String addExpense(String description, String comment, float price) {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        String timeString = time.toString();
        String timeToExpense = timeString.split("\\.")[0];

        //TODO: check for double
        Expense expense = new Expense(date.toString(), timeToExpense, description, price, comment);
        expenseRepo.save(expense);

        return "redirect:/" + EXPENSE_PAGE;
    }

    public String getExpenses(Model model) {

        List<ExpenseDto> expensesDtoList = getExpenseDtoList(getExpenseList());
        model.addAttribute("expenses", expensesDtoList);
        model.addAttribute("expenseSum", getSumOfExpense(expensesDtoList));
        model.addAttribute("averageExpense", getAverageExpense(expensesDtoList));

        return EXPENSE_PAGE;
    }

    public List<ExpenseDto> getExpenseDtoList(List<Expense> expenseList) {
        List<ExpenseDto> expenseDtoList = new ArrayList<>();
        for (Expense expense: expenseList) {
            expenseDtoList.add(new ExpenseDto(expense.getId(),
                expense.getDate(), expense.getTime(), expense.getDescription(),
                expense.getPrice(), expense.getComment()));
        }
        return expenseDtoList;
    }

    public List<Expense> getExpenseList() {
        try {
            Iterable<Expense> expenses = expenseRepo.findAll();
            List<Expense> expenseList = new ArrayList<>();
            expenses.forEach(unfulfilledOrder -> expenseList.add(unfulfilledOrder));
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

    public String expenseDetails(long id, Model model) throws Exception {

        Expense expense = expenseRepo.findById(id).orElseThrow(()->new Exception("Expense not found - " + id));
        model.addAttribute("expense", expense);
        return "expense_details";
    }

    public String expenseUpdate(long id, String description, String comment,
                                float price, Model model) throws Exception {
        Expense expense = expenseRepo.findById(id).orElseThrow(()->new Exception("Expense not found - " + id));

        expense.setDescription(description);
        expense.setComment(comment);
        expense.setPrice(price);
        expenseRepo.save(expense);

        List<ExpenseDto> expenseDtoList = getExpenseDtoList(getExpenseList());
        model.addAttribute("expenses", expenseDtoList);

        return "redirect:/" + EXPENSE_PAGE;
    }

    public String expenseRemove(long id, Model model) {
        expenseRepo.deleteById(id);

        List<ExpenseDto> expenseDtoList = getExpenseDtoList(getExpenseList());
        model.addAttribute("expense", expenseDtoList);
        return "redirect:/" + EXPENSE_PAGE;
    }

    //TODO: filter by period
}
