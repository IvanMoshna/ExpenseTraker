package com.moshna.traker.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private LocalDate date;
    private LocalTime time;
    private String description;
    private BigDecimal price;
    private String comment;

    //TODO: foreign key
    private Long userId;

    public Expense(LocalDate date, LocalTime time, String description, BigDecimal price, String comment, Long userId) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.description = description;
        this.price = price;
        this.comment = comment;
        this.userId = userId;
    }
}
