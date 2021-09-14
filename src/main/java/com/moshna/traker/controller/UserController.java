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
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public String userList(Model model) {
        return userService.getUserList(model);
    }

    @GetMapping("{user}/expenses")
    public String userExpense(@AuthenticationPrincipal User userAuth,
                              Model model) {
        //userAuth is null
        return userService.getUserExpenses(model);
    }

    @GetMapping("{user}")
    public String userEdit(
            @PathVariable User user,

            Model model) {
        return userService.userEdit(user, model);
    }

    @PostMapping("{user}/update")
    public String userUpdate(
                            @PathVariable(value = "user") long id,
                            @RequestParam String userName,
                            @RequestParam String userRole,
                            @RequestParam Map<String, String> form,
                            Model model) throws Exception {
        return userService.userUpdate(id, userName, userRole, form, model);
    }

    @PostMapping("/addExpenseToUser")
    public String addExpenseToUser(
                             @RequestParam String description,
                             @RequestParam BigDecimal price,
                             @RequestParam String comment
                             ) {
        //TODO: {userId}/expense
        //TODO: сделать возможность добавления и с админа любому юзеру
        return userService.addExpense(description,price, comment);
    }

    @PostMapping("{user}/remove")
    public String removeUser(@PathVariable(value = "user") long id,
                                Model model) {
        return userService.removeUser(id, model);
    }

    @GetMapping("{user}/expenses/{id}")
    public String userExpenseDetails(@PathVariable(value = "id") long idExpense, Model model) throws Exception {
        return userService.userExpenseDetails(idExpense, model);
    }

    @PostMapping("{user}/expenses/{id}/update")
    public String updateExpense(@PathVariable long id,
                                @RequestParam String description,
                                @RequestParam BigDecimal price,
                                @RequestParam String comment,
                                Model model) throws Exception {
        return userService.expenseUpdate(id, description, comment, price, model);
    }

    @PostMapping("{user}/expenses/{id}/remove")
    public String removeExpense(@PathVariable long id,
                                Model model) throws Exception {
        return userService.removeExpense(id, model);
    }
}
