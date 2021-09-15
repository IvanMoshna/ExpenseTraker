package com.moshna.traker.controller;

import com.moshna.traker.model.User;
import com.moshna.traker.repo.ExpenseRepo;
import com.moshna.traker.repo.UserRepo;
import com.moshna.traker.service.ExpenseService;
import com.moshna.traker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public String userList(Model model) {
        return userService.getUserList(model);
    }

    @GetMapping("{userId}/expenses")
    public String userExpense(Model model) {
        return userService.getUserExpenses(model);
    }

    @GetMapping("{user}")
    public String userEdit(
                           @PathVariable User user,
                           Model model) {
        return userService.userEdit(user, model);
    }

    @PostMapping("{userId}/update")
    public String userUpdate(
                            @PathVariable(value = "userId") long id,
                            @RequestParam String userName,
                            @RequestParam Map<String, String> form,
                            Model model) throws Exception {
        return userService.userUpdate(id, userName, form, model);
    }

    @PostMapping("{user}/expense")
    public String addExpenseToUser(
                             @PathVariable(value = "user") long id,//current user id
                             @RequestParam String description,
                             @RequestParam BigDecimal price,
                             @RequestParam String comment,
                             Model model
                             ) {
        return userService.addExpense(id, description,price, comment, model);
    }

    @PostMapping("{userId}/remove")
    public String removeUser(
                                @PathVariable(value = "user") long id,
                                Model model) {
        return userService.removeUser(id, model);
    }

    @GetMapping("{userId}/expenses/{expenseId}")
    public String userExpenseDetails(
                                     @PathVariable(value = "expenseId") long idExpense,
                                     Model model) throws Exception {
        return userService.userExpenseDetails(idExpense, model);
    }

    @PostMapping("{userId}/expenses/{expenseId}/update")
    public String updateExpense(
                                @PathVariable(value = "expenseId") long expenseId,
                                @RequestParam String description,
                                @RequestParam BigDecimal price,
                                @RequestParam String comment,
                                Model model) throws Exception {
        return userService.expenseUpdate(expenseId, description, comment, price, model);
    }

    @PostMapping("{userId}/expenses/{expenseId}/remove")
    public String removeExpense(@PathVariable(value = "expenseId") long expenseId,
                                Model model) throws Exception {
        return userService.removeExpense(expenseId, model);
    }

    @PostMapping("{userId}/filterByDates")
    public String filterByDates(
                                  @PathVariable(value = "userId") long id,
                                  @RequestParam String fromDate,
                                  @RequestParam String toDate,
                                  Model model) {
        return userService.filterByDates(id, fromDate, toDate, model);
    }
}
