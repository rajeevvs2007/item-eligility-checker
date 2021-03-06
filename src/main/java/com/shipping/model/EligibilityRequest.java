package com.shipping.model;


import javax.validation.constraints.*;
import java.util.Objects;

public class EligibilityRequest {

    @NotEmpty(message = "validation.error.title.empty")
    private String title;

    @NotEmpty(message = "validation.error.seller.empty")
    private String seller;

    @PositiveOrZero(message = "validation.error.category.invalid")
    private int category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EligibilityRequest)) return false;
        EligibilityRequest that = (EligibilityRequest) o;
        return getCategory() == that.getCategory() &&
                Double.compare(that.getPrice(), getPrice()) == 0 &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getSeller(), that.getSeller());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getSeller(), getCategory(), getPrice());
    }

    @PositiveOrZero(message = "validation.error.price.positiveorzero")
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
