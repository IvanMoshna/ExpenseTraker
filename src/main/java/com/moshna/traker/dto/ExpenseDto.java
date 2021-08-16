package com.moshna.traker.dto;

public class ExpenseDto {
    private long id;
    private String date;
    private String time;
    private String description;
    private float price;
    private String comment;

    public ExpenseDto() {
    }

    public ExpenseDto(long id, String date, String time, String description, float price, String comment) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.description = description;
        this.price = price;
        this.comment = comment;
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
