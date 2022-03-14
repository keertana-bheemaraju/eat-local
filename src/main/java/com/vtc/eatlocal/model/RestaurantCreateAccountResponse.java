package com.vtc.eatlocal.model;

public class RestaurantCreateAccountResponse {

    private String restaurantCreateAccountMessage;

    private boolean createAccountStatus;

    private Integer restaurantId;

    public String getRestaurantCreateAccountMessage() {
        return restaurantCreateAccountMessage;
    }

    public void setRestaurantCreateAccountMessage(String restaurantCreateAccountMessage) {
        this.restaurantCreateAccountMessage = restaurantCreateAccountMessage;
    }

    public boolean isCreateAccountStatus() {
        return createAccountStatus;
    }

    public void setCreateAccountStatus(boolean createAccountStatus) {
        this.createAccountStatus = createAccountStatus;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }
}
