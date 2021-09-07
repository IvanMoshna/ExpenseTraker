package com.moshna.traker.controller;

import com.moshna.traker.model.Expense;
import com.moshna.traker.model.User;
import com.moshna.traker.repo.ExpenseRepo;
import com.moshna.traker.repo.UserRepo;
import com.moshna.traker.service.ExpenseService;
import com.moshna.traker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepo userRepo;
    @Autowired
    ExpenseRepo expenseRepo;
    @Autowired
    UserService userService;
    @Autowired
    ExpenseService expenseService;

    @GetMapping
    public String userList(Model model) {
        /*model.addAttribute("users", userRepo.findAll());
        return "userList";*/
        return userService.getUserList(model);
    }

    @GetMapping("{user}/expenses")
    public String userExpense(@AuthenticationPrincipal User userAuth,
                              Model model) {
        //userAuth is null
        return userService.getUserExpenses(model);
    }

    @GetMapping("{user}")
    public String userEdit(@PathVariable User user, Model model) {
        /*model.addAttribute("user", user);
        return "userEdit";*/
        return userService.userEdit(user, model);
    }

    @PostMapping("{user}/update")
    public String userUpdate(
            @PathVariable(value = "user") long id,
                            @RequestParam String userName,
                            @RequestParam String userRole,
                            Model model) throws Exception {
        return userService.userUpdate(id, userName, userRole, model);
    }

    @PostMapping("/addExpenseToUser")
    public String addExpenseToUser(
                             @RequestParam String description,
                             @RequestParam Double price,
                             @RequestParam String comment
                             ) {
        //return expenseService.addExpense(description,comment, price, getCurrentlyUser().getId());
        return userService.addExpense(description,price, comment);
    }

    @GetMapping("{user}/expenses/{id}")
    public String userExpenseDetails(@PathVariable(value = "id") long idExpense, Model model) throws Exception {

        /*Expense expense = expenseRepo.findById(idExpense).orElseThrow(()->new Exception("Expense not found - " + idExpense));
        model.addAttribute("expense", expense);
        return "expense_details";*/
        return userService.userExpenseDetails(idExpense, model);
    }

    @PostMapping("{user}/expenses/{id}/update")
    public String updateExpense(@PathVariable long id,
                                @RequestParam String description,
                                @RequestParam float price,
                                @RequestParam String comment,
                                Model model) throws Exception {
        return userService.expenseUpdate(id, description, comment, price, model);
    }


}
