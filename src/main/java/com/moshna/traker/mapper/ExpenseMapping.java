package com.moshna.traker.mapper;

import com.moshna.traker.dto.ExpenseDto;
import com.moshna.traker.model.Expense;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ExpenseMapping {

    public static ExpenseDto toExpenseDTO(Expense expense) {
        return ExpenseDto.builder()
                .id(expense.getId())
                .comment(expense.getComment())
                .date(expense.getDate())
                .time(expense.getTime())
                .price(expense.getPrice())
                .description(expense.getDescription())
                .build();
    }

    public static Expense toExpense(ExpenseDto expenseDto) {
        return Expense.builder()
                .id(expenseDto.getId())
                .comment(expenseDto.getComment())
                .date(expenseDto.getDate())
                .time(expenseDto.getTime())
                .price(expenseDto.getPrice())
                .description(expenseDto.getDescription())
                .build();
    }

    public  static  Expense toExpense(Expense expense, ExpenseDto dto) {
        expense.setComment(dto.getComment());
        expense.setDescription(dto.getDescription());
        expense.setPrice(dto.getPrice());
        expense.setTime(dto.getTime());
        expense.setDate(dto.getDate());
        return expense;
    }
}
