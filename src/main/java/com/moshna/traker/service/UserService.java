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
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final ExpenseRepo expenseRepo;
    private final ExpenseService expenseService;

    public static final String USER_EXPENSE = "userExpense";
    public static final String USER_DETAILS = "userEdit";
    public static final String EXPENSE_DETAILS = "expenseDetails";
    public static final String ERROR = "error";

    public static final String USER_ID = "userId";
    public static final String EXPENSES = "expenses";

    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String PERMISSION_MESSAGE = "You are haven't permissions";


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public String getUserList(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Role.ADMIN.name())) ||
                auth != null && auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals(Role.MANAGER.name()))) {
            List<UserDto> userDtoList = getUserDtoList(getUserList());

            Set<String> roles = Arrays.stream(Role.values())
                    .map(Role::name)
                    .collect(Collectors.toSet());

            model.addAttribute("users", userDtoList);
            model.addAttribute(USER_ID, getCurrentlyUser().getId());
            model.addAttribute("allRoles", roles);
            return "userList";
        } else {
            model.addAttribute(ERROR_MESSAGE, PERMISSION_MESSAGE);
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
        model.addAttribute(USER_ID, getCurrentlyUser().getId());
        model.addAttribute(EXPENSES,
                expenseService.getExpenseDtoList(expenseService.getExpenseList(getCurrentlyUser().getId())));
        return USER_EXPENSE;
    }

    public String userEdit(User user, Model model) {
        List<ExpenseDto> expenseDtoList =
                expenseService.getExpenseDtoList(expenseRepo.findAllByUserId(user.getId()));

        Set<Role> roleSelected = user.getRoles();

        List<String> roleSelectedList = new ArrayList<>();
        for (Role role : roleSelected) {
            roleSelectedList.add(role.name());
        }
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        model.addAttribute("roles", roleSelectedList);
        model.addAttribute("user", user);
        model.addAttribute(USER_ID, getCurrentlyUser().getId());
        model.addAttribute(EXPENSES, expenseDtoList);
        model.addAttribute("allRoles", roles);

        return USER_DETAILS;
    }

    public String userUpdate(Long id, String userName, Map<String, String> form, Model model)
            throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Role.ADMIN.name())) ||
                auth != null && auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals(Role.MANAGER.name()))) {

            User updatedUser = userRepo.findById(id).orElseThrow(() ->
                    new Exception("User not found - " + id));

            Set<String> roles = Arrays.stream(Role.values())
                    .map(Role::name)
                    .collect(Collectors.toSet());

            updatedUser.getRoles().clear();

            for (Map.Entry<String, String> entry : form.entrySet()) {
                String key = entry.getKey();
                if (roles.contains(key)) {
                    updatedUser.getRoles().add(Role.valueOf(form.get(key)));
                }
            }

            model.addAttribute(USER_ID, getCurrentlyUser().getId());
            model.addAttribute(EXPENSES, expenseService.getExpenseDtoList(expenseRepo.findAllByUserId(id)));

            updatedUser.setUsername(userName);
            userRepo.save(updatedUser);
            return USER_EXPENSE;
        } else {
            model.addAttribute(ERROR_MESSAGE, PERMISSION_MESSAGE);
            return ERROR;
        }
    }

    public String addExpense(
            Long id,
            String description,
            BigDecimal price,
            String comment,
            Model model
    ) {
        User user = getCurrentlyUser();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals(Role.ADMIN.name())) ||
                     id == user.getId()) {
            expenseService.addExpense(description, comment, price, user);
            return getUserExpenses(model);
        } else {
            model.addAttribute(ERROR_MESSAGE, PERMISSION_MESSAGE);
            return ERROR;
        }
    }

    public String userExpenseDetails(Long id, Model model) throws Exception {
        User user = getCurrentlyUser();
        Expense expense = expenseRepo.findById(id).orElseThrow(() -> new Exception("Expense not found - " + id));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Role.ADMIN.name())) ||
                expense.getUser().getId() == user.getId()) {


            model.addAttribute(USER_ID, user.getId());
            model.addAttribute("expense", expense);
            return EXPENSE_DETAILS;
        } else {
            model.addAttribute(ERROR_MESSAGE, PERMISSION_MESSAGE);
            return ERROR;
        }
    }

    public String expenseUpdate(Long id, String description, String comment,
                                BigDecimal price, Model model) throws Exception {
        Expense expense = expenseRepo.findById(id).orElseThrow(() -> new Exception("Expense not found - " + id));

        List<ExpenseDto> expenseDtoList =
                expenseService.getExpenseDtoList(expenseRepo.findAllByUserId(getCurrentlyUser().getId()));

        expense.setDescription(description);
        expense.setComment(comment);
        expense.setPrice(price);
        expenseRepo.save(expense);

        model.addAttribute(EXPENSES, expenseDtoList);
        model.addAttribute(USER_ID, getCurrentlyUser().getId());
        getUserExpenses(model);
        return USER_EXPENSE;
    }

    public String removeUser(Long id, Model model) {
        if(id == getCurrentlyUser().getId()) {
            model.addAttribute(ERROR_MESSAGE, "You can't remove yourself");
            return ERROR;
        } else {
            List<Expense> expenses = expenseRepo.findAllByUserId(id);
            for (Expense e : expenses) {
                expenseRepo.deleteById(e.getId());
            }
            userRepo.deleteById(id);

            model.addAttribute(USER_ID, getCurrentlyUser().getId());
            model.addAttribute(EXPENSES, expenseService.getExpenseDtoList(
                    expenseService.getExpenseList(getCurrentlyUser().getId())));
            return USER_EXPENSE;
        }
    }

    public String removeExpense(Long id, Model model) {
        User user = getCurrentlyUser();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Role.ADMIN.name())) ||
                id == user.getId()) {
            expenseRepo.deleteById(id);

            model.addAttribute(USER_ID, id);
            model.addAttribute(EXPENSES, expenseService.getExpenseDtoList(
                    expenseService.getExpenseList(getCurrentlyUser().getId())));
            return USER_EXPENSE;
        } else {
            model.addAttribute(ERROR_MESSAGE, PERMISSION_MESSAGE);
            return ERROR;
        }
    }

    public String filterByDates(Long id, String fromDate, String toDate, Model model) {
        List<ExpenseDto> expenseFilteredDtoList = expenseService.filterByDates(id, fromDate, toDate);
        model.addAttribute("expenseSum", expenseService.getSumOfExpense(
                expenseFilteredDtoList));
        model.addAttribute("averageExpense", expenseService.getAverageExpense(
                expenseFilteredDtoList));
        model.addAttribute(EXPENSES, expenseFilteredDtoList);
        model.addAttribute(USER_ID, id);
        return USER_EXPENSE;
    }

    public User getCurrentlyUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = "";
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        }
        User currentUser = new User();
        List<User> userList = userRepo.findAll();
        for (User user : userList) {
            if (user.getUsername().equals(currentUserName)) {
                currentUser = user;
                break;
            }
        }
        return currentUser;
    }

    public List<UserDto> getUserDtoList(List<User> userList) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
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
