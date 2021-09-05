package com.moshna.traker.service;

import com.moshna.traker.dto.ExpenseDto;
import com.moshna.traker.model.Expense;
import com.moshna.traker.model.User;
import com.moshna.traker.repo.ExpenseRepo;
import com.moshna.traker.repo.UserRepo;
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
/*    //TODO:remove userRepo
    @Autowired
    private final UserRepo userRepo;*/


    private static final String EXPENSE_PAGE = "expense";

    public ExpenseService(ExpenseRepo expenseRepo) {
        this.expenseRepo = expenseRepo;
        //this.userRepo = userRepo;
    }

    public String addExpense( String description, String comment, float price, double userId) {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        String timeString = time.toString();
        String timeToExpense = timeString.split("\\.")[0];

        //TODO: check for double
        Expense expense = new Expense(date.toString(), timeToExpense, description, price, comment);
        expenseRepo.save(expense);

            //return EXPENSE_PAGE;
        return "redirect:/home";
    }

    /*public String getExpenses(User user, Model model) {
        List<ExpenseDto> expensesDtoList = getExpenseDtoList(getExpenseList());
        model.addAttribute("userId", user.getId().toString());
        model.addAttribute("expenses", expensesDtoList);
        model.addAttribute("expenseSum", getSumOfExpense(expensesDtoList));
        model.addAttribute("averageExpense", getAverageExpense(expensesDtoList));

        return EXPENSE_PAGE;
    }*/

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

    /*public String expenseDetails(long id, Model model) throws Exception {

        Expense expense = expenseRepo.findById(id).orElseThrow(()->new Exception("Expense not found - " + id));
        model.addAttribute("expense", expense);
        return "expense_details";
    }*/

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

    //TODO: check that we are got date or create a calendar
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
    }
}
