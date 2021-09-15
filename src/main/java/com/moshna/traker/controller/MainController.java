package com.moshna.traker.controller;

import com.moshna.traker.model.User;
import com.moshna.traker.repo.ExpenseRepo;
import com.moshna.traker.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {


    private final UserRepo userRepo;
    private final ExpenseRepo expenseRepo;

    @GetMapping("/")
    public String home(Model model) {
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
        Long userId = currentUser.getId();
        model.addAttribute("userId", userId.toString());
        return "/home";
    }
}
