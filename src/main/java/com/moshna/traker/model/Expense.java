package com.moshna.traker.model;

import javax.persistence.*;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String date;
    private String time;
    private String description;
    private float price;
    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Expense() {
    }

    public Expense(String date, String time, String description, float price, String comment) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.price = price;
        this.comment = comment;
    }

    public Expense(Integer id, String date, String time, String description, float price, String comment) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.description = description;
        this.price = price;
        this.comment = comment;
    }

    public Expense(Integer id, String date, String time, String description, float price, String comment, User user) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.description = description;
        this.price = price;
        this.comment = comment;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
