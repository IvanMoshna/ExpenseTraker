package com.moshna.traker;

import com.moshna.traker.repo.ExpenseRepo;
import com.moshna.traker.repo.UserRepo;
import com.moshna.traker.service.ExpenseService;
import com.moshna.traker.service.UserService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

    @Bean
    public ExpenseService expenseService() {return new ExpenseService(repoMockExpense()); }

    /*@Bean
    public UserService userService() {return new UserService(repoMockUser());}*/

    @Bean
    public ExpenseRepo repoMockExpense() {return Mockito.mock(ExpenseRepo.class);}

    @Bean
    public UserRepo repoMockUser() {return Mockito.mock(UserRepo.class);}
}
