package com.vtc.eatlocal.model;

public class RedeemRewardResponse {

    private boolean redemptionStatus;

    private String message;

    public boolean isRedemptionStatus() {
        return redemptionStatus;
    }

    public void setRedemptionStatus(boolean redemptionStatus) {
        this.redemptionStatus = redemptionStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
