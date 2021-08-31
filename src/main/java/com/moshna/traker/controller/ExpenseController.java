package com.moshna.traker.controller;

import com.moshna.traker.service.ExpenseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/login")
    public String main() {return "/login"; }

    @GetMapping("/expense")
    public String mainExpense(Model model) {
        return expenseService.getExpenses(model);
    }

    @PostMapping("/addExpense")
    public String addUnfulfilledOrder(@RequestParam String description,
                                      @RequestParam float price,
                                      @RequestParam String comment) {
        return expenseService.addExpense(description, comment, price);
    }

    @GetMapping("{id}")
    public String expenseDetails(@PathVariable(value = "id") long id, Model model) throws Exception {

        return "";
        //return expenseService.expenseDetails(id, model);
    }

    @PostMapping("{id}/update")
    public String updateExpense(@PathVariable long id,
                                @RequestParam String description,
                                @RequestParam float price,
                                @RequestParam String comment,
                                Model model) throws Exception {
        return expenseService.expenseUpdate(id, description, comment, price, model);
    }

    @PostMapping("{id}/remove")
    public String removeExpense(@PathVariable(value = "id") long id,
                                Model model) {
        return expenseService.expenseRemove(id, model);
    }

    @PostMapping("/filteredByDates")
    public String filteredByDates(@RequestParam String fromDate,
                                  @RequestParam String toDate,
                                  Model model) {
        return expenseService.filterByDates(fromDate, toDate, model);
    }


}
