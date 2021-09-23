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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usr_id")
    private User user;

    public Expense(LocalDate date, LocalTime time, String description, BigDecimal price, String comment, /*Long userId*/ User user) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.price = price;
        this.comment = comment;
        this.user = user;
    }

}
