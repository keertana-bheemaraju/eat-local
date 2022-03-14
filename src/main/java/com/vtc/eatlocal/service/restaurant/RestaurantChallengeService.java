package com.vtc.eatlocal.service.restaurant;

import com.vtc.eatlocal.entity.ChallengeRestaurantAssociation;
import com.vtc.eatlocal.entity.Restaurant;
import com.vtc.eatlocal.entity.RestaurantChallenges;
import com.vtc.eatlocal.model.CustomerChallenegeAssociation;
import com.vtc.eatlocal.model.JoinChallengeInfo;
import com.vtc.eatlocal.repository.ChallengeRestaurantAssociationRepository;
import com.vtc.eatlocal.repository.RestaurantChallengesRepository;
import com.vtc.eatlocal.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantChallengeService {

    public static final String DELIMITTER_COMMA = ",";

    @Autowired
    private ChallengeRestaurantAssociationRepository challengeRestaurantAssociationRepository;

    @Autowired
    private RestaurantChallengesRepository restaurantChallengesRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

//
//    public Iterable<String> getAllRestaurantsForChallenge(CustomerChallenegeAssociation cca) {
//        Optional<ChallengeRestaurantAssociation> cra_db = challengeRestaurantAssociationRepository.findById(cca.getChallengeId());
//        List<String> op = new ArrayList<>();
//        if(cra_db.isPresent()) {
//            for(String s : cra_db.get().getRestaurantIdList().split(DELIMITTER_COMMA)) {
//                int restaurantId = Integer.parseInt(s.trim());
//                restaurantRepository.getById(restaurantId);
//            }
//        }
//
//        return op;
//    }


    public Iterable<Restaurant> getAllRestaurantsForChallenge(int challengeId) {

        Optional<ChallengeRestaurantAssociation> cra_db = challengeRestaurantAssociationRepository.findById(challengeId);
        List<Restaurant> op = new ArrayList<>();
        if(cra_db.isPresent()) {
            for(String s : cra_db.get().getRestaurantIdList().split(DELIMITTER_COMMA)) {
                op.add(restaurantRepository.findById(Integer.parseInt(s.trim())).get());
            }
        }

        return op;
    }

    public List<String> getAllRestaurantNamesForChallenge(int challengeId) {

     List<String> names = new ArrayList<>();
     Iterable<Restaurant> allRestaurantsForChallenge = getAllRestaurantsForChallenge(challengeId);
     Iterator<Restaurant> iterator = allRestaurantsForChallenge.iterator();
     while (iterator.hasNext()) {
         names.add(iterator.next().getRestaurantName());
     }

     return names;
    }



    public  Iterable<String> getEnrolledChallengeIds(JoinChallengeInfo joinChallengeInfo) {
        Optional<RestaurantChallenges> resChallengesFromDb = restaurantChallengesRepository.findById(joinChallengeInfo.getRestaurantId());
        List<String> enrolledChallenges = new ArrayList<>();
        if(resChallengesFromDb.isPresent()) {
            for(String challenge : resChallengesFromDb.get().getChallengeList().split(DELIMITTER_COMMA)) {
                enrolledChallenges.add(challenge.trim());
            }
        }

        return  enrolledChallenges;
    }


    public Iterable<String> getEnrolledChallengeNames(JoinChallengeInfo joinChallengeInfo) {
        Optional<RestaurantChallenges> resChallengesFromDb = restaurantChallengesRepository.findById(joinChallengeInfo.getRestaurantId());
        List<String> enrolledChallenges = new ArrayList<>();
        if(resChallengesFromDb.isPresent()) {
            for(String challenge : resChallengesFromDb.get().getChallengeNameList().split(DELIMITTER_COMMA)) {
                enrolledChallenges.add(challenge.trim());
            }
        }

        return  enrolledChallenges;
    }


}
