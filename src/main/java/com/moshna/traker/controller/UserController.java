package com.moshna.traker.controller;

import com.moshna.traker.model.Expense;
import com.moshna.traker.model.User;
import com.moshna.traker.repo.ExpenseRepo;
import com.moshna.traker.repo.UserRepo;
import com.moshna.traker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepo userRepo;
    @Autowired
    ExpenseRepo expenseRepo;
    @Autowired
    ExpenseService expenseService;

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "userList";
    }

   /* @GetMapping("/")
    public String home(Model model) {
        //TODO:can i get user id correctly?
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = "";
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        }
        User currentUser = new User();
        List<User> userList =  userRepo.findAll();
        for (User user: userList) {
            if(user.getUsername().equals(currentUserName)) {
                currentUser = user;
                break;
            }
        }
        //TODO: if currentUser is empty do exception
        Long userId = currentUser.getId();
        model.addAttribute("expenses", expenseRepo.findAll());
        model.addAttribute("userId", userId.toString());
        return "/userExpense";
    }*/

    @GetMapping("{user}/expenses")
    public String userExpense(@AuthenticationPrincipal User userAuth,
                              Model model) {
        //TODO: доставать по id юзера только его траты
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = "";
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        }
        User currentUser = new User();
        List<User> userList =  userRepo.findAll();
        for (User user: userList) {
            if(user.getUsername().equals(currentUserName)) {
                currentUser = user;
                break;
            }
        }
        //TODO: if currentUser is empty do exception
        Long userId = currentUser.getId();
        //model.addAttribute("userId", user.getId().toString());
        model.addAttribute("userId", userId.toString());
        model.addAttribute("expenses", expenseRepo.findAll());
        //model.addAttribute("user", user);
        return "userExpense";
    }

    @GetMapping("{user}")
    public String userEdit(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        return "userEdit";
    }

    @PostMapping("/addExpenseToUser")
    public String addExpenseToUser(
                             @RequestParam String description,
                             @RequestParam String price,
                             @RequestParam String comment,
                             @RequestParam String userId
                             ) {
        //return expenseService.addExpense(description,comment, price, userId);
        return "userEdit";
    }

    @PostMapping("testPost")
    public String testPost( @ModelAttribute Expense expense) {
        String test = "";
        return "login";
    }

    @PostMapping("testPost2")
    public String testPost2(@RequestParam String test) {
        String test2 = test;
        return "login";
    }



}
