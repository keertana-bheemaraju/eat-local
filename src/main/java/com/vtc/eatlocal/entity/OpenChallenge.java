package com.vtc.eatlocal.entity;

import javax.persistence.*;

@Entity
@Table(name= "propose_challenge",
        indexes= @Index(name = "county_index", columnList = "county")
)
public class OpenChallenge {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String challengeTitle;

    private String restaurantName;

    private String cuisine;

    private Integer purchase;

    private String reward;

    private String visits;

    private String uniqueVisits;

    private String zipcode;

    private String county;

    private Integer minimum;

    private Integer maximum;

    private Integer vacancy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getChallengeTitle() {
        return challengeTitle;
    }

    public void setChallengeTitle(String challengeTitle) {
        this.challengeTitle = challengeTitle;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public Integer getPurchase() {
        return purchase;
    }

    public void setPurchase(Integer purchase) {
        this.purchase = purchase;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getVisits() {
        return visits;
    }

    public void setVisits(String visits) {
        this.visits = visits;
    }

    public String getUniqueVisits() {
        return uniqueVisits;
    }

    public void setUniqueVisits(String uniqueVisits) {
        this.uniqueVisits = uniqueVisits;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public Integer getMinimum() {
        return minimum;
    }

    public void setMinimum(Integer minimum) {
        this.minimum = minimum;
    }

    public Integer getMaximum() {
        return maximum;
    }

    public void setMaximum(Integer maximum) {
        this.maximum = maximum;
    }

    public Integer getVacancy() {
        return vacancy;
    }

    public void setVacancy(Integer vacancy) {
        this.vacancy = vacancy;
    }
}
