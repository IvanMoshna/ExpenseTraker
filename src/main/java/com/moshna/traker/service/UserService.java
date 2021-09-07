package com.moshna.traker.service;

import com.moshna.traker.dto.ExpenseDto;
import com.moshna.traker.model.Expense;
import com.moshna.traker.model.User;
import com.moshna.traker.repo.ExpenseRepo;
import com.moshna.traker.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class UserService  implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    ExpenseRepo expenseRepo;
    @Autowired
    ExpenseService expenseService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public String getUserList(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        /*if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))) {
            System.out.println("hi");
        }*/
        model.addAttribute("users", userRepo.findAll());
        return "userList";
    }

    public String getUserExpenses(Model model) {
        //TODO: корректно вытаскивать пользователя
        User user = getCurrentlyUser();
        model.addAttribute("user", user);
        model.addAttribute("userId", getCurrentlyUser().getId());
        model.addAttribute("expenses", expenseRepo.findAllByUserId(getCurrentlyUser().getId()));
        return "userExpense";
    }

    public String userEdit(User user, Model model) {
        model.addAttribute("user", user);
        return "userEdit";
    }

    public String userUpdate(long id, String userName, String userRole, Model model) throws Exception {

        //Long id = getCurrentlyUser().getId();
        User updatedUser = userRepo.findById(id).orElseThrow(()->
                new Exception("User not found - " + id));
        //updatedUser.setId(id);
        updatedUser.setUsername(userName);
        //updatedUser.setRoles(userRole);
        userRepo.save(updatedUser);
        return "userExpense";
    }

    public String addExpense(
            String description,
            Double price,
            String comment
    ) {
        //TODO: are this method correct?
        return expenseService.addExpense(description,comment, price, getCurrentlyUser().getId());
    }

    public String userExpenseDetails(long id, Model model) throws Exception {

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

        //List<ExpenseDto> expenseDtoList = getExpenseDtoList(getExpenseList());
        model.addAttribute("expenses", expenseRepo.findAll());
        model.addAttribute("userId", getCurrentlyUser().getId());
        //TODO: how to redirect in normal page?
        getUserExpenses(model);
        return "userExpense";
    }

    public User getCurrentlyUser() {
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
        return  currentUser;

    }


}
