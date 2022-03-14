package com.vtc.eatlocal.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "challenge_restaurant_association")
public class ChallengeRestaurantAssociation {

    @Id
    private Integer challengeId;

    private String challengeTitle;

    private String restaurantIdList;

    private String restaurantNameList;

    public String getChallengeTitle() {
        return challengeTitle;
    }

    public void setChallengeTitle(String challengeTitle) {
        this.challengeTitle = challengeTitle;
    }

    public Integer getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Integer challengeId) {
        this.challengeId = challengeId;
    }

    public String getRestaurantIdList() {
        return restaurantIdList;
    }

    public void setRestaurantIdList(String restaurantIdList) {
        this.restaurantIdList = restaurantIdList;
    }

    public String getRestaurantNameList() {
        return restaurantNameList;
    }

    public void setRestaurantNameList(String restaurantNameList) {
        this.restaurantNameList = restaurantNameList;
    }
}
