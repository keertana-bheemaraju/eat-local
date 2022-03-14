package com.vtc.eatlocal.service.restaurant;

import com.vtc.eatlocal.entity.Restaurant;
import com.vtc.eatlocal.entity.Reward;
import com.vtc.eatlocal.entity.RewardHistory;
import com.vtc.eatlocal.model.RedeemRewardResponse;
import com.vtc.eatlocal.repository.RewardHistoryRepository;
import com.vtc.eatlocal.repository.RewardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;

@Service
public class RedeemRewardService {

    @Autowired
    RewardHistoryRepository rewardHistoryRepository;

    @Autowired
    RestaurantChallengeService restaurantChallengeService;

    @Autowired
    RewardRepository rewardRepository;

    public RedeemRewardResponse redeemReward(int customerId, String customerName, int challengeId, String challengeName, int restaurantId, String restaurantName) {

        RedeemRewardResponse redeemRewardResponse = new RedeemRewardResponse();

        if(!doesRestaurantExistInChallenge(restaurantId, challengeId)) {
            redeemRewardResponse.setRedemptionStatus(false);
            redeemRewardResponse.setMessage("REWARD ISSUE FAILED. \n Restaurant is not part of the challenge: " + challengeName);
            return redeemRewardResponse;
        }

        if(!doesCustomerHaveReward(challengeId, customerId)) {
            redeemRewardResponse.setRedemptionStatus(true);
            redeemRewardResponse.setMessage("REWARD ISSUE FAILED. \n No rewards available for this customer for the challenge : " + challengeName);
            return redeemRewardResponse;
        }


        populateRewardHistory(customerId, customerName, challengeId, challengeName, restaurantId, restaurantName);

        // Manage reward counts
        Reward rewardRecord = rewardRepository.findById(createRewardCompositeKey(customerId, challengeId)).get();
        rewardRecord.setRewardCount(rewardRecord.getRewardCount()-1);
        if(rewardRecord.getRewardCount() == 0) {
            rewardRepository.deleteById(createRewardCompositeKey(customerId, challengeId));
        } else {
            rewardRepository.save(rewardRecord);
        }

        redeemRewardResponse.setRedemptionStatus(true);
        redeemRewardResponse.setMessage("Reward Issued Successfully!");

        return redeemRewardResponse;

    }

    private boolean doesCustomerHaveReward(int challengeId, int customerId) {
        String key = createRewardCompositeKey(customerId, challengeId);
        return  rewardRepository.findById(key).isPresent();
    }

    private boolean doesRestaurantExistInChallenge(int restaurantId, int challengeId) {
        Iterable<Restaurant> allRestaurantsForChallenge = restaurantChallengeService.getAllRestaurantsForChallenge(challengeId);
        Iterator<Restaurant> iterator = allRestaurantsForChallenge.iterator();
        boolean restaurantExistsInChallenge = false;
        while (iterator.hasNext()) {
            Restaurant restaurant = iterator.next();
            if(restaurant.getId() == restaurantId) {
                restaurantExistsInChallenge = true;
                break;
            }
        }

        return restaurantExistsInChallenge;
    }


    private boolean populateRewardHistory(int customerId, String customerName, int challengeId, String challengeName, int restaurantId, String restaurantName) {

        RewardHistory rewardHistory = new RewardHistory();
        // Generate timestamp
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy'T'HH:mm:ss");
        String timestamp = formatter.format(date);
        String id = createRewardHistoryCompositeKey(customerId, challengeId, timestamp);
        rewardHistory.setId(id);
        rewardHistory.setChallengeId(challengeId);
        rewardHistory.setCustomerId(customerId);
        rewardHistory.setCustomerName(customerName);
        rewardHistory.setChallengeName(challengeName);
        rewardHistory.setRestaurantId(restaurantId);
        rewardHistory.setRestaurantName(restaurantName);
        rewardHistory.setRedeemDate(timestamp);

        rewardHistoryRepository.save(rewardHistory);
        return true;
    }

    private String createRewardHistoryCompositeKey(int customerId, int challengeId, String timestamp) {
        return customerId + "-" + challengeId + "-" + timestamp;
    }

    private String createRewardCompositeKey(int customerId, int challengeId) {
        return  customerId + "-" + challengeId;
    }
}
