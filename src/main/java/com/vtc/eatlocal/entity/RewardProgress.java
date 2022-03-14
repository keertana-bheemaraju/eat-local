package com.vtc.eatlocal.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RewardProgress {

    //Composite key like customerId-challengeId
    @Id
    private String id;

    private int customerId;

    private String customerName;

    private int challengeId;

    private String challengeName;

    private int minForReward;

    private int progress;

    private String timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    public int getMinForReward() {
        return minForReward;
    }

    public void setMinForReward(int minForReward) {
        this.minForReward = minForReward;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
