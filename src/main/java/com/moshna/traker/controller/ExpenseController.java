package com.moshna.traker.controller;

import com.moshna.traker.service.ExpenseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/expense")
    public String mainExpense() {
        return "expense";
    }

    @PostMapping("/addExpense")
    public String addUnfulfilledOrder(@RequestParam String date,
                                      @RequestParam String time,
                                      @RequestParam String description,
                                      @RequestParam float price,
                                      @RequestParam String comment) {
        return expenseService.addExpense(date, time, description, comment, price);
    }

}
