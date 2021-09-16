package com.moshna.traker.service;

import com.moshna.traker.dto.ExpenseDto;
import com.moshna.traker.dto.UserDto;
import com.moshna.traker.mapper.UserMapping;
import com.moshna.traker.model.Expense;
import com.moshna.traker.model.Role;
import com.moshna.traker.model.User;
import com.moshna.traker.repo.ExpenseRepo;
import com.moshna.traker.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService  implements UserDetailsService {

    private final UserRepo userRepo;
    private final ExpenseRepo expenseRepo;
    private final ExpenseService expenseService;

    public static final String USER_EXPENSE = "userExpense";
    public static final String USER_DETAILS = "userEdit";
    public static final String EXPENSE_DETAILS = "expenseDetails";
    public static final String ERROR = "error";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public String getUserList(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(/*Role.ADMIN.name()*/"USER")) || //TODO: использовать Role.USER.name()
                auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("MANAGER"))) {
            List<UserDto> userDtoList = getUserDtoList(getUserList());
            model.addAttribute("users", userDtoList);
            model.addAttribute("userId", getCurrentlyUser().getId());
            return "userList";
        } else {
            String message = "You are haven't permissions";
            model.addAttribute("errorMessage", message);
            return ERROR;
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
        return USER_EXPENSE;
    }

    public String userEdit(User user, Model model) {
        List<ExpenseDto> expenseDtoList =
                expenseService.getExpenseDtoList(expenseRepo.findAllByUserId(user.getId()));

        Set<Role> roleSelected = user.getRoles();

        List<String> roleSelectedList = new ArrayList<>();
        for (Role role: roleSelected) {
            roleSelectedList.add(role.name());
        }
        Set<String> roles= Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        model.addAttribute("roles", roleSelectedList);
        model.addAttribute("user", user);
        model.addAttribute("userId", getCurrentlyUser().getId());
        model.addAttribute("expenses", expenseDtoList);
        model.addAttribute("allRoles", roles);

        return USER_DETAILS;
    }

    public String userUpdate(Long id, String userName, Map<String, String> form, Model model)
            throws Exception {

        User updatedUser = userRepo.findById(id).orElseThrow(()->
                new Exception("User not found - " + id));

       Set<String> roles= Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        updatedUser.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(form.get(key))) {
                updatedUser.getRoles().add(Role.valueOf(form.get(key)));
            }
        }

        updatedUser.setUsername(userName);
        userRepo.save(updatedUser);
        return USER_EXPENSE;
    }

    public String addExpense(
            Long id,
            String description,
            BigDecimal price,
            String comment,
            Model model
    ) {
        expenseService.addExpense(description,comment, price, id);
        return getUserExpenses(model);
    }

    public String userExpenseDetails(Long id, Model model) throws Exception {

        Expense expense = expenseRepo.findById(id).orElseThrow(()->new Exception("Expense not found - " + id));
        model.addAttribute("userId", getCurrentlyUser().getId());
        model.addAttribute("expense", expense);
        return EXPENSE_DETAILS;
    }

    public String expenseUpdate(Long id, String description, String comment,
                                BigDecimal price, Model model) throws Exception {
        Expense expense = expenseRepo.findById(id).orElseThrow(()->new Exception("Expense not found - " + id));

        List<ExpenseDto> expenseDtoList =
                expenseService.getExpenseDtoList(expenseRepo.findAllByUserId(getCurrentlyUser().getId()));

        expense.setDescription(description);
        expense.setComment(comment);
        expense.setPrice(price);
        expenseRepo.save(expense);

        model.addAttribute("expenses", expenseDtoList);
        model.addAttribute("userId", getCurrentlyUser().getId());
        getUserExpenses(model);
        return USER_EXPENSE;
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
        return USER_EXPENSE;
    }

    public String removeExpense(Long id, Model model) {
        expenseRepo.deleteById(id);

        model.addAttribute("userId", id);
        model.addAttribute("expenses", expenseService.getExpenseDtoList(
                expenseService.getExpenseList(getCurrentlyUser().getId())));
        return USER_EXPENSE;
    }

    public String filterByDates(Long id, String fromDate, String toDate, Model model) {
        List<ExpenseDto> expenseFilteredDtoList = expenseService.filterByDates(id, fromDate, toDate);
        model.addAttribute("expenseSum", expenseService.getSumOfExpense(
                expenseFilteredDtoList));
        model.addAttribute("averageExpense", expenseService.getAverageExpense(
                expenseFilteredDtoList));
        model.addAttribute("expenses", expenseFilteredDtoList);
        model.addAttribute("userId",id);
        return USER_EXPENSE;
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
        return  currentUser;
    }

    public List<UserDto> getUserDtoList(List<User> userList) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user: userList) {
            userDtoList.add(UserMapping.toUserDto(user));
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
