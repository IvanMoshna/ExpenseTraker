package com.moshna.traker.service;

import com.moshna.traker.dto.ExpenseDto;
import com.moshna.traker.dto.UserDto;
import com.moshna.traker.model.Expense;
import com.moshna.traker.model.Role;
import com.moshna.traker.model.User;
import com.moshna.traker.repo.ExpenseRepo;
import com.moshna.traker.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;
import java.util.stream.Collectors;

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
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER")) ||
                auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("MANAGER"))) {
            List<UserDto> userDtoList = getUserDtoList(getUserList());
            model.addAttribute("users", userDtoList);
            model.addAttribute("userId", getCurrentlyUser().getId());
            return "userList";
        } else {
            String message = "You are haven't permissions";
            model.addAttribute("errorMessage", message);
            return "error";
        }

    }

    public String getUserExpenses(Model model) {
        User user = getCurrentlyUser();
        model.addAttribute("expenseSum", expenseService.getSumOfExpense(
                expenseService.getExpenseDtoList(expenseService.getExpenseList(getCurrentlyUser().getId()))));
        model.addAttribute("averageExpense", expenseService.getAverageExpense(
                expenseService.getExpenseDtoList(expenseService.getExpenseList(getCurrentlyUser().getId()))));
        model.addAttribute("user", user);
        model.addAttribute("userId", getCurrentlyUser().getId());
        model.addAttribute("expenses",
                expenseService.getExpenseDtoList(expenseService.getExpenseList(getCurrentlyUser().getId())));
        return "userExpense";
    }

    public String userEdit(User user, Model model) {
        List<ExpenseDto> expenseDtoList =
                expenseService.getExpenseDtoList(expenseRepo.findAllByUserId(getCurrentlyUser().getId()));

        Set<Role> roleSelected = user.getRoles();

        //Set<String> roles = new HashSet<>();
        Set<String> roles= Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        /*user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }*/

        model.addAttribute("roles", roles);
        model.addAttribute("user", user);
        model.addAttribute("userId", getCurrentlyUser().getId());
        model.addAttribute("expenses", expenseDtoList);

        return "userEdit";
    }

    public String userUpdate(long id, String userName, String userRole, Map<String, String> form, Model model) throws Exception {

        User updatedUser = userRepo.findById(id).orElseThrow(()->
                new Exception("User not found - " + id));

       Set<String> roles= Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        updatedUser.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                updatedUser.getRoles().add(Role.valueOf(key));
            }
        }

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
        return expenseService.addExpense(description,comment, price, getCurrentlyUser().getId());
    }

    public String userExpenseDetails(long id, Model model) throws Exception {

        Expense expense = expenseRepo.findById(id).orElseThrow(()->new Exception("Expense not found - " + id));
        model.addAttribute("userId", getCurrentlyUser().getId());
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

        model.addAttribute("expenses", expenseRepo.findAll());
        model.addAttribute("userId", getCurrentlyUser().getId());
        //TODO: how to redirect in normal page?
        getUserExpenses(model);
        return "userExpense";
    }

    public String removeUser(Long id, Model model) {
        List<Expense> expenses = expenseRepo.findAllByUserId(id);
        for (Expense e: expenses) {
            expenseRepo.deleteById(e.getId());
        }
        userRepo.deleteById(id);

        model.addAttribute("userId", getCurrentlyUser().getId());
        model.addAttribute("expenses", expenseService.getExpenseDtoList(
                expenseService.getExpenseList(getCurrentlyUser().getId())));
        return "userExpense";
    }

    public String removeExpense(long id, Model model) {
        expenseRepo.deleteById(id);

        model.addAttribute("userId", getCurrentlyUser().getId());
        model.addAttribute("expenses", expenseService.getExpenseDtoList(expenseService.getExpenseList(getCurrentlyUser().getId())));
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

    public List<UserDto> getUserDtoList(List<User> userList) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user: userList) {
            userDtoList.add(new UserDto(user.getId(), user.getUsername(), user.getPassword(), user.getRoles()));
        }
        return userDtoList;
    }

    public List<User> getUserList() {
        try {
            Iterable<User> users = userRepo.findAll();
            List<User> userList = new ArrayList<>();
            users.forEach(user -> userList.add(user));
            return userList;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }


}
