package com.shipping.model;


import javax.validation.constraints.*;

public class EligibilityRequest {

    @NotEmpty(message = "validation.error.title.empty")
    private String title;

    @NotEmpty(message = "validation.error.seller.empty")
    private String seller;

    @PositiveOrZero
    private int category;

    @PositiveOrZero
    private double price;

    public EligibilityRequest(String title, String seller, int category, double price) {
        this.title = title;
        this.seller = seller;
        this.category = category;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
