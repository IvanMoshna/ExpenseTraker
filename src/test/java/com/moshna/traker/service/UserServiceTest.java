package com.moshna.traker.service;

import com.moshna.traker.TestConfiguration;
import com.moshna.traker.dto.UserDto;
import com.moshna.traker.mapper.UserMapping;
import com.moshna.traker.model.User;
import com.moshna.traker.repo.ExpenseRepo;
import com.moshna.traker.repo.UserRepo;
import com.moshna.traker.utils.UserUtils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo repoMockUser;
    @Autowired
    private ExpenseRepo repoMockExpense;

    @Test
    public void loadUserByUsername() {
        //CastClassException
        User usr = UserUtils.TEST_USER;
        Mockito.when(repoMockUser.findByUsername(usr.getUsername())).thenReturn((User) Collections.singletonList(usr));

        User user = (User)userService.loadUserByUsername(usr.getUsername());

        Assertions.assertNotNull(user);
        Assertions.assertEquals(usr.getUsername(), user.getUsername());
    }

    @Test
    public void getUserList() {

    }

    @Test
    public void getUserExpenses() {
    }

    @Test
    public void userEdit() {
    }

    @Test
    public void userUpdate() {
    }

    @Test
    public void addExpense() {
    }

    @Test
    public void userExpenseDetails() {
    }

    @Test
    public void expenseUpdate() {
    }

    @Test
    public void removeUser() {
    }

    @Test
    public void removeExpense() {

    }

    @Test
    public void filterByDates() {
    }

    @Test
    public void testGetUserDtoList() {
        User user = UserUtils.TEST_USER;
        Mockito.when(repoMockUser.findAll()).thenReturn(Collections.singletonList(user));

        List<User> userList = userService.getUserList();

        List<UserDto> userDtoList = new ArrayList<>();
        for (User u: userList) {
            userDtoList.add(UserMapping.toUserDto(u));
        }
        Assertions.assertNotNull(userDtoList);
        Assertions.assertEquals(userList.size(), userDtoList.size());
        Assertions.assertEquals(userList.get(0).getId(), userDtoList.get(0).getId());
    }

    @Test
    public void testGetUserList() {
        User user = UserUtils.TEST_USER;
        Mockito.when(repoMockUser.findAll()).thenReturn(Collections.singletonList(user));

        List<User> userList = userService.getUserList();

        Assertions.assertNotNull(userList);
        Assertions.assertEquals(1, userList.size());
    }

    @Test
    public void testGetUserList_ReturnNull() {
        Mockito.when(repoMockUser.findAll()).thenReturn(null);

        List<User> userList = userService.getUserList();

        Assertions.assertNotNull(userList);
        Assertions.assertEquals(0, userList.size());
    }
}