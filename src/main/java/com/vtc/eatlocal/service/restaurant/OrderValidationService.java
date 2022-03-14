package com.vtc.eatlocal.service.restaurant;

import com.vtc.eatlocal.entity.*;
import com.vtc.eatlocal.model.OrderValidationResponse;
import com.vtc.eatlocal.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class OrderValidationService {

    @Autowired
    OrderValidationRepository orderValidationRepository;

    @Autowired
    RewardProgressRepository rewardProgressRepository;

    @Autowired
    RewardRepository rewardRepository;

    @Autowired
    LiveChallengeRepository liveChallengeRepository;

    @Autowired
    RestaurantChallengeService restaurantChallengeService;

    @Autowired
    RestaurantChallengeProgressRepository restaurantChallengeProgressRepository;


    public OrderValidationResponse validateOrderWithOrderValue(OrderValidation orderValidation) {

        // Check if order value meets criteria for validation
        OrderValidationResponse orderValidationResponse = new OrderValidationResponse();

        Optional<LiveChallenge> byId = liveChallengeRepository.findById(orderValidation.getChallengeId());
        if(byId.isPresent()) {
            LiveChallenge liveChallenge = byId.get();
            if(orderValidation.getOrderValue() >= liveChallenge.getPurchase() ) {
                return validateOrder(orderValidation);
            } else {
                orderValidationResponse.setMessage("Order value didnot meet minimum purchase value for the challenge");
                orderValidationResponse.setValidationStatus(false);
            }
        }

        return orderValidationResponse;

    }

    public OrderValidationResponse validateOrder(OrderValidation orderValidation) {
        OrderValidationResponse orderValidationResponse = new OrderValidationResponse();

        if(!doesRestaurantExistInChallenge(orderValidation.getRestaurantId(), orderValidation.getChallengeId())) {
            orderValidationResponse.setValidationStatus(false);
            orderValidationResponse.setMessage("VALIDATION FAILED. \n This restaurant is not participating in challenge: " + orderValidation.getChallengeName());
            return orderValidationResponse;
        }

        if(isOrderAlreadyValidated(orderValidation)) {
            orderValidationResponse.setValidationStatus(false);
            orderValidationResponse.setMessage("This order was already validated for challenge: " + orderValidation.getChallengeName());
            return orderValidationResponse;
        }


        // Generate timestamp
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy'T'HH:mm:ss");
        String timestamp = formatter.format(date);

        orderValidation.setTimestamp(timestamp);

        populateRewardProgress(orderValidation);

       // populateRestaurantChallengeProgress(orderValidation);

        orderValidationRepository.save(orderValidation);

        orderValidationResponse.setValidationStatus(true);
        orderValidationResponse.setMessage("Validation Success");
        return orderValidationResponse;
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

    private boolean isOrderAlreadyValidated(OrderValidation orderValidation) {
        String key = createRestaurantChallengeProgressKey(orderValidation.getCustomerId(), orderValidation.getChallengeId(), orderValidation.getRestaurantId());
        return restaurantChallengeProgressRepository.findById(key).isPresent();
    }

    private boolean populateRewardProgress(OrderValidation orderValidation) {
        // Check if there is a record in this table for the customer and challenge
        String key = createRewardCompositeKey(orderValidation.getCustomerId(), orderValidation.getChallengeId());
        Optional<RewardProgress> progress_optional = rewardProgressRepository.findById(key);
        if(progress_optional.isPresent()){
            RewardProgress rewardProgress = progress_optional.get();
            rewardProgress.setProgress(rewardProgress.getProgress() + 1);
            rewardProgressRepository.save(rewardProgress);
            populateRestaurantChallengeProgress(orderValidation);
            if(rewardProgress.getProgress() == rewardProgress.getMinForReward()) {
                populateReward(orderValidation);
                rewardProgressRepository.deleteById(key);
                //delete from restaurantChallengeProgress table
                deleteFromRestaurantChallengeProgress(orderValidation.getCustomerId(), orderValidation.getChallengeId());
            }

        } else {
            populateRestaurantChallengeProgress(orderValidation);
            RewardProgress rewardProgress = new RewardProgress();
            String id = createRewardCompositeKey(orderValidation.getCustomerId(), orderValidation.getChallengeId());
            rewardProgress.setId(id);
            rewardProgress.setChallengeId(orderValidation.getChallengeId());
            rewardProgress.setCustomerId(orderValidation.getCustomerId());
            rewardProgress.setCustomerName(orderValidation.getCustomerName());
            rewardProgress.setChallengeName(orderValidation.getChallengeName());
            rewardProgress.setTimestamp(orderValidation.getTimestamp());

            Integer minimum = liveChallengeRepository.findById(orderValidation.getChallengeId()).get().getMinimum();
            rewardProgress.setMinForReward(minimum);
            rewardProgress.setProgress(1);

            rewardProgressRepository.save(rewardProgress);
        }

        return  true;
    }

    private boolean deleteFromRestaurantChallengeProgress(int customerId, int challengeId) {
        String prefix = customerId + "-" + challengeId;
        List<RestaurantChallengeProgress> all = restaurantChallengeProgressRepository.findAll();
        for(RestaurantChallengeProgress restaurantChallengeProgress : all) {
            if(restaurantChallengeProgress.getId().startsWith(prefix)) {
                restaurantChallengeProgressRepository.delete(restaurantChallengeProgress);
            }
        }

        return  true;
    }

    private boolean populateRestaurantChallengeProgress(OrderValidation orderValidation) {
        String id = createRestaurantChallengeProgressKey(orderValidation.getCustomerId(), orderValidation.getChallengeId(), orderValidation.getRestaurantId());
        RestaurantChallengeProgress restaurantChallengeProgress = new RestaurantChallengeProgress();
        restaurantChallengeProgress.setId(id);
        restaurantChallengeProgressRepository.save(restaurantChallengeProgress);
        return true;
    }

    private boolean populateReward(OrderValidation orderValidation) {

        String id = createRewardCompositeKey(orderValidation.getCustomerId(), orderValidation.getChallengeId());
        Optional<Reward> reward_db = rewardRepository.findById(id);
        if(reward_db.isPresent()) {
            Reward rewardRecord = reward_db.get();
            rewardRecord.setRewardCount(rewardRecord.getRewardCount() + 1);
            rewardRepository.save(rewardRecord);
        } else {
            Reward reward = new Reward();
            reward.setId(id);
            reward.setCustomerId(orderValidation.getCustomerId());
            reward.setCustomerName(orderValidation.getCustomerName());
            reward.setChallengeId(orderValidation.getChallengeId());
            reward.setChallengeName(orderValidation.getChallengeName());
            reward.setRewardCount(1);
            rewardRepository.save(reward);
        }

        return true;
    }


    private String createRewardCompositeKey(int customerId, int challengeId) {
        return  customerId + "-" + challengeId;
    }

    private String createRestaurantChallengeProgressKey(int customerId, int challengeId, int restaurantId) {
        return customerId + "-" + challengeId + "-" + restaurantId;
    }
}
