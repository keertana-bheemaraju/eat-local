package com.vtc.eatlocal.model;

public class OrderValidationResponse {

    private boolean validationStatus;

    private String message;

    public boolean isValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(boolean validationStatus) {
        this.validationStatus = validationStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
