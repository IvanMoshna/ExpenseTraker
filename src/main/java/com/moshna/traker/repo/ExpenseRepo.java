package com.moshna.traker.repo;

import com.moshna.traker.model.Expense;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepo extends CrudRepository<Expense, Long> {

    List<Expense> findAllByUserId(Long userId);
}
