package com.vtc.eatlocal.service.user;

import com.vtc.eatlocal.entity.LiveChallenge;
import com.vtc.eatlocal.entity.Restaurant;
import com.vtc.eatlocal.repository.LiveChallengeRepository;
import com.vtc.eatlocal.service.restaurant.RestaurantChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Service
public class LiveChallengeService {

    @Autowired
    LiveChallengeRepository liveChallengeRepository;

    @Autowired
    RestaurantChallengeService restaurantChallengeService;

    public Set<String> getCountyList(List<String> cusineList) {
        System.out.println(cusineList);

        // Get all challenges from DB
        List<LiveChallenge> liveChallenges = liveChallengeRepository.findAll();

        // Create an empty list
        Set<String> filteredChallenges = new TreeSet<>();

        // Loop thru each challenge in liveChallenges and add it to filteredChallenge if the challenge's cuisine is present in input
        for (LiveChallenge challenge : liveChallenges) {
            if (cusineList.isEmpty() || cusineList.contains(challenge.getCuisine())) {
                filteredChallenges.add(challenge.getCounty());
            }
        }

        return filteredChallenges;
    }

    //getCountyList(List<String> restaurantList) {


    public  Set<String> getCuisineList(List<String> countyList) {
        System.out.println(countyList);

        List<LiveChallenge> liveChallenges = liveChallengeRepository.findAll();
        Set<String> filteredChallenges = new TreeSet<>();

        for (LiveChallenge challenge : liveChallenges) {
            if (countyList.isEmpty() || countyList.contains(challenge.getCounty())) {
                filteredChallenges.add(challenge.getCuisine());
            }

        }

        return filteredChallenges;
    }

    //getCuisineList(List<String> restaurantList) {


    public  Set<String> getRestaurantList() {

        List<LiveChallenge> liveChallenges = liveChallengeRepository.findAll();

        Set<String> restaurantNames = new HashSet<>();

        for (LiveChallenge challenge : liveChallenges) {
            Iterable<Restaurant> allRestaurantsForChallenge = restaurantChallengeService.getAllRestaurantsForChallenge(challenge.getChallengeId());
            Iterator<Restaurant> restaurantIterator = allRestaurantsForChallenge.iterator();
            while(restaurantIterator.hasNext()) {
                restaurantNames.add(restaurantIterator.next().getRestaurantName());
            }
        }

        return restaurantNames;
    }

}
