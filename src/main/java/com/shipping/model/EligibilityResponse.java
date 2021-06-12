package com.shipping.model;

public class EligibilityResponse {

    private boolean itemEligible;


    public EligibilityResponse(boolean itemEligible) {
        this.itemEligible = itemEligible;
    }

    public void setItemEligible(boolean itemEligible) {
        this.itemEligible = itemEligible;
    }

    public boolean isItemEligible() {
        return itemEligible;
    }

    @Override
    public String toString() {
        return "EligibilityResponse{" +
                "itemEligible=" + itemEligible +
                '}';
    }

}
