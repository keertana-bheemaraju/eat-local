package com.vtc.eatlocal.model;

public class RestaurantLoginResponse {

    private String message;

    private boolean loginStatus;

    private Integer restaurantId;

    private String restaurantName;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String resturantName) {
        this.restaurantName = resturantName;
    }
}
