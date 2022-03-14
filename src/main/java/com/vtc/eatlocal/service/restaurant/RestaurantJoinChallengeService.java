package com.vtc.eatlocal.service.restaurant;

import com.vtc.eatlocal.entity.LiveChallenge;
import com.vtc.eatlocal.entity.OpenChallenge;
import com.vtc.eatlocal.entity.RestaurantChallenges;
import com.vtc.eatlocal.model.JoinChallengeInfo;
import com.vtc.eatlocal.repository.LiveChallengeRepository;
import com.vtc.eatlocal.repository.OpenChallengeRepository;
import com.vtc.eatlocal.repository.RestaurantChallengesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class RestaurantJoinChallengeService {

    @Autowired
    private OpenChallengeRepository openChallengeRepository;

    @Autowired
    private LiveChallengeRepository liveChallengeRepository;

    @Autowired
    private RestaurantChallengesRepository restaurantChallengesRepository;

    private static final String DELIMITTER_COMMA = ",";


    public Boolean joinChallenge(JoinChallengeInfo joinChallengeInfo) {
        Optional<OpenChallenge> byId = openChallengeRepository.findById(Integer.parseInt(joinChallengeInfo.getChallengeId()));
        if(byId.isPresent()) {
            // Decrement vacancy by 1 if there is atleast one spot left
            OpenChallenge openChallenge = byId.get();
            openChallenge.setVacancy(openChallenge.getVacancy()-1);
            openChallengeRepository.save(openChallenge);

            if(openChallenge.getVacancy() == 0) {
                // Delete from propose openChallenge table
                openChallengeRepository.delete(openChallenge);
            } else if(openChallenge.getMaximum() - openChallenge.getVacancy() >= openChallenge.getMinimum()) {
                // move to live openChallenge
                LiveChallenge liveChallenge = buildLiveChallenge(openChallenge);
                liveChallengeRepository.save(liveChallenge);
            }

            // Store this openChallenge against the restaurantId
            Optional<RestaurantChallenges> resChallenge_db = restaurantChallengesRepository.findById(joinChallengeInfo.getRestaurantId());
            if(resChallenge_db.isPresent()) {
                String challengeList_from_db = resChallenge_db.get().getChallengeList();
                String updated_challenge_list = challengeList_from_db + DELIMITTER_COMMA + joinChallengeInfo.getChallengeId();

                String challengeNameList_from_db = resChallenge_db.get().getChallengeNameList();
                String updated_challengeName_list = challengeNameList_from_db + DELIMITTER_COMMA + joinChallengeInfo.getChallengeName();

                RestaurantChallenges restaurantChallenges = new RestaurantChallenges();
                restaurantChallenges.setRestaurantId(joinChallengeInfo.getRestaurantId());
                restaurantChallenges.setChallengeList(updated_challenge_list);
                restaurantChallenges.setChallengeNameList(updated_challengeName_list);
                restaurantChallengesRepository.save(restaurantChallenges);

            } else {
                RestaurantChallenges restaurantChallenges = new RestaurantChallenges();
                restaurantChallenges.setRestaurantId(joinChallengeInfo.getRestaurantId());
                restaurantChallenges.setChallengeList(joinChallengeInfo.getChallengeId());
                restaurantChallenges.setChallengeNameList(joinChallengeInfo.getChallengeName());
                restaurantChallengesRepository.save(restaurantChallenges);
            }
            return true;
        }
        return false;
    }

    private LiveChallenge buildLiveChallenge(OpenChallenge openChallenge) {
        LiveChallenge liveChallenge = new LiveChallenge();
        liveChallenge.setChallengeTitle(openChallenge.getChallengeTitle());
        liveChallenge.setChallengeId(openChallenge.getId());
        liveChallenge.setCounty(openChallenge.getCounty());
        liveChallenge.setCuisine(openChallenge.getCuisine());
        liveChallenge.setMaximum(openChallenge.getMaximum());
        liveChallenge.setMinimum(openChallenge.getMinimum());
        liveChallenge.setReward(openChallenge.getReward());
        liveChallenge.setUniqueVisits(openChallenge.getUniqueVisits());
        liveChallenge.setZipcode(openChallenge.getZipcode());
        liveChallenge.setPurchase(openChallenge.getPurchase());

        return liveChallenge;
    }

}
