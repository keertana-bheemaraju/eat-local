package com.vtc.eatlocal.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "challenge_restaurant_association")
public class ChallengeRestaurantAssociation {

    @Id
    private String challengeTitle;

    private String restaurantList;

    public String getChallengeTitle() {
        return challengeTitle;
    }

    public void setChallengeTitle(String challengeTitle) {
        this.challengeTitle = challengeTitle;
    }

    public String getRestaurantList() {
        return restaurantList;
    }

    public void setRestaurantList(String restaurantList) {
        this.restaurantList = restaurantList;
    }
}
