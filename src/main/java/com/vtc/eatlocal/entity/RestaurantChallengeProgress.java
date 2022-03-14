package com.vtc.eatlocal.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RestaurantChallengeProgress {

    // customerId-challengeId-restaurantId
    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
