package com.vtc.eatlocal.service.user;

import com.vtc.eatlocal.entity.LiveChallenge;
import com.vtc.eatlocal.entity.OrderValidation;
import com.vtc.eatlocal.entity.Restaurant;
import com.vtc.eatlocal.entity.RestaurantChallengeProgress;
import com.vtc.eatlocal.repository.LiveChallengeRepository;
import com.vtc.eatlocal.repository.OrderValidationRepository;
import com.vtc.eatlocal.repository.RestaurantChallengeProgressRepository;
import com.vtc.eatlocal.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChallengeStatusService {

    @Autowired
    private OrderValidationRepository orderValidationRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private LiveChallengeRepository liveChallengeRepository;

    @Autowired
    private RestaurantChallengeProgressRepository restaurantChallengeProgressRepository;

    public List<Restaurant> getValidatedRestaurants(int customerId, int challengeId) {
        List<Restaurant> validatedRestaurants = new ArrayList<>();

        List<RestaurantChallengeProgress> all = restaurantChallengeProgressRepository.findAll();

        String prefix = customerId + "-" + challengeId;
        for(RestaurantChallengeProgress restaurantChallengeProgress : all) {
            if(restaurantChallengeProgress.getId().startsWith(prefix)) {
                String restaurantId = restaurantChallengeProgress.getId().split("-")[2];
                Restaurant restaurant = restaurantRepository.findById(Integer.parseInt(restaurantId)).get();
                validatedRestaurants.add(restaurant);
            }
        }

        return validatedRestaurants;
    }

    public LiveChallenge getLiveChallengeByName(String challengeName) {
        List<LiveChallenge> liveChallenges = liveChallengeRepository.findAll();

        for(LiveChallenge liveChallenge : liveChallenges) {
            if(liveChallenge.getChallengeTitle().equals(challengeName)) {
                return liveChallenge;
            }
        }

        return new LiveChallenge();
    }
}
