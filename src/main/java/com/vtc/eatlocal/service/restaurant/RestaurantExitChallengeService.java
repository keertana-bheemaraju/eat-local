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
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class RestaurantExitChallengeService {

    @Autowired
    private OpenChallengeRepository openChallengeRepository;

    @Autowired
    private LiveChallengeRepository liveChallengeRepository;

    @Autowired
    private RestaurantChallengesRepository restaurantChallengesRepository;

    private static final String DELIMITTER_COMMA = ",";

    public Boolean exitChallenge (@RequestBody JoinChallengeInfo joinChallengeInfo) {


        Optional<RestaurantChallenges> resChallenge_db = restaurantChallengesRepository.findById(joinChallengeInfo.getRestaurantId());

        if(resChallenge_db.isPresent()) {

            String challengeNameList_from_db = resChallenge_db.get().getChallengeNameList();
            String updated_challengeName_list = "";
            for(String challengeName : challengeNameList_from_db.split(DELIMITTER_COMMA)) {
                if(!challengeName.trim().equals(joinChallengeInfo.getChallengeName())) {
                    if(updated_challengeName_list.isEmpty()) {
                        updated_challengeName_list = challengeName;
                    } else {
                        updated_challengeName_list =  updated_challengeName_list + DELIMITTER_COMMA + challengeName;
                    }

                }
            }

            String challenge_ID_List_from_db = resChallenge_db.get().getChallengeList();
            String updated_challenge_ID_list = "";
            String currentChallengeId = null;
            for(String challengeId : challenge_ID_List_from_db.split(DELIMITTER_COMMA)) {
                Optional<OpenChallenge> proposeChallengeRepositoryById = openChallengeRepository.findById(Integer.parseInt(challengeId.trim()));
                if (proposeChallengeRepositoryById.isPresent() &&
                        !proposeChallengeRepositoryById.get().getChallengeTitle().trim().equals(joinChallengeInfo.getChallengeName())) {
                    if(updated_challenge_ID_list.isEmpty()) {
                        updated_challenge_ID_list = challengeId;
                    } else {
                        updated_challenge_ID_list = updated_challenge_ID_list + DELIMITTER_COMMA + challengeId;
                    }
                } else {
                    currentChallengeId = challengeId;
                }
            }

            if(!updated_challenge_ID_list.isEmpty()) {
                RestaurantChallenges restaurantChallenges = new RestaurantChallenges();
                restaurantChallenges.setRestaurantId(joinChallengeInfo.getRestaurantId());
                restaurantChallenges.setChallengeList(updated_challenge_ID_list);
                restaurantChallenges.setChallengeNameList(updated_challengeName_list);
                restaurantChallengesRepository.save(restaurantChallenges);
            } else {
                restaurantChallengesRepository.deleteById(joinChallengeInfo.getRestaurantId());
            }

            adjustOpenChallenges(currentChallengeId);

        }
        return true;
    }

    private void adjustOpenChallenges(String challengeIdString) {

        int challengeId = Integer.parseInt(challengeIdString);
        Optional<OpenChallenge> openChallenge_db = openChallengeRepository.findById(challengeId);
        if(openChallenge_db.isPresent()) {
            OpenChallenge openChallenge = openChallenge_db.get();
            openChallenge.setVacancy(openChallenge.getVacancy() + 1);
            openChallengeRepository.save(openChallenge);
            if(openChallenge.getMaximum() - openChallenge.getVacancy() < openChallenge.getMinimum()) {
                if(liveChallengeRepository.findById(challengeId).isPresent()) {
                    liveChallengeRepository.deleteById(challengeId);
                }

            }
        } else {
            LiveChallenge liveChallenge = liveChallengeRepository.getById(challengeId);
            OpenChallenge openChallenge = buildOpenChallenge(liveChallenge);
            openChallenge.setVacancy(1);
            openChallengeRepository.save(openChallenge);
            if(openChallenge.getMaximum() - openChallenge.getVacancy() < openChallenge.getMinimum()) {
                if(liveChallengeRepository.findById(challengeId).isPresent()) {
                    liveChallengeRepository.deleteById(challengeId);
                }
            }
        }
    }

    private OpenChallenge buildOpenChallenge(LiveChallenge liveChallenge) {
        OpenChallenge openChallenge = new OpenChallenge();
        openChallenge.setChallengeTitle(liveChallenge.getChallengeTitle());
        openChallenge.setId(liveChallenge.getChallengeId());
        openChallenge.setCounty(liveChallenge.getCounty());
        openChallenge.setCuisine(liveChallenge.getCuisine());
        openChallenge.setMaximum(liveChallenge.getMaximum());
        openChallenge.setMinimum(liveChallenge.getMinimum());
        openChallenge.setReward(liveChallenge.getReward());
        openChallenge.setUniqueVisits(liveChallenge.getUniqueVisits());
        openChallenge.setZipcode(liveChallenge.getZipcode());
        openChallenge.setPurchase(liveChallenge.getPurchase());
        return openChallenge;
    }

}
