package com.moshna.traker.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String date;
    private String time;
    private String description;
    private double price;
    private String comment;

    /*@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;*/

    //TODO: foreign key
    private Long userId;

   /* //TODO: remove after add userId
    public Expense(LocalDate date, LocalTime time, String description, BigDecimal price, String comment) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.price = price;
        this.comment = comment;
    }

    //TODO: remove after add userId
    public Expense(Integer id, LocalDate date, LocalTime time, String description, BigDecimal price, String comment) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.description = description;
        this.price = price;
        this.comment = comment;
    }*/

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
