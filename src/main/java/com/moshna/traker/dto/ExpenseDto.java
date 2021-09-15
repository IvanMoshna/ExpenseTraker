package com.moshna.traker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@Builder
public class ExpenseDto {
    private long id;
    private LocalDate date;
    private LocalTime time;
    private String description;
    private BigDecimal price;
    private String comment;
    private Long userId;
}
