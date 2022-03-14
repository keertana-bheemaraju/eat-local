package com.vtc.eatlocal.composition;

import com.vtc.eatlocal.entity.LiveChallenge;
import com.vtc.eatlocal.entity.User;

public class CustomerLiveChallengeComposition {

    private User customer;

    private LiveChallenge liveChallenge;

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public LiveChallenge getLiveChallenge() {
        return liveChallenge;
    }

    public void setLiveChallenge(LiveChallenge liveChallenge) {
        this.liveChallenge = liveChallenge;
    }
}
