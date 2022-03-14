package com.vtc.eatlocal.entity;

import org.springframework.util.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity // This tells Hibernate to make a table out of this class
@Table(name= "restaurant_challenges")
public class RestaurantChallenges {

    private String challengeList;

    @Id
    private Integer restaurantId;

    private String challengeNameList;

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getChallengeList() {
        return challengeList;
    }

    public void setChallengeList(String challengeList) {
        this.challengeList = challengeList;
    }

    public String getChallengeNameList() {
        return challengeNameList;
    }

    public void setChallengeNameList(String challengeNameList) {
        this.challengeNameList = challengeNameList;
    }
}
