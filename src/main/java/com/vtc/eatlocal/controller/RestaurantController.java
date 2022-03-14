package com.vtc.eatlocal.controller;

import com.vtc.eatlocal.entity.*;
import com.vtc.eatlocal.model.*;
import com.vtc.eatlocal.repository.*;
import com.vtc.eatlocal.service.restaurant.RestaurantExitChallengeService;
import com.vtc.eatlocal.service.restaurant.RestaurantJoinChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path="/restaurant-path") // This means URL's start with /demo (after Application path)
public class RestaurantController {

    public static final String DELIMITTER_COMMA = ",";

    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantCredentialsRepository restaurantCredentialsRepository;

    @Autowired
    private OpenChallengeRepository openChallengeRepository;

    @Autowired
    private LiveChallengeRepository liveChallengeRepository;

    @Autowired
    private ChallengeRestaurantAssociationRepository challengeRestaurantAssociationRepository;

    @Autowired
    private RestaurantChallengesRepository restaurantChallengesRepository;


    @Autowired
    private RestaurantJoinChallengeService restaurantJoinChallengeService;

    @Autowired
    private RestaurantExitChallengeService restaurantExitChallengeService;

    @PostMapping(path="/save-data") // Map ONLY POST Requests
    public @ResponseBody
    RestaurantCreateAccountResponse createAccount (@RequestBody Restaurant restaurant ) {

        RestaurantCreateAccountResponse response = new RestaurantCreateAccountResponse();

        // return error if email already exists
        String email = restaurant.getEmail();
        boolean emailExists = restaurantCredentialsRepository.findById(email).isPresent();
        if(emailExists) {
            response.setCreateAccountStatus(false);
            response.setRestaurantCreateAccountMessage("Email already exists");
        } else {
            // Save user data
            response.setCreateAccountStatus(true);
            restaurant  = restaurantRepository.save(restaurant);
            response.setRestaurantId(restaurant.getId());

            RestaurantCredentials restaurantCredentials = new RestaurantCredentials();
            restaurantCredentials.setUsername(restaurant.getEmail());
            restaurantCredentials.setPassword(restaurant.getPassword());

            restaurantCredentialsRepository.save(restaurantCredentials);
        }


        return response;
    }

    @PostMapping(path="/signin") // Map ONLY POST Requests
    public @ResponseBody
    RestaurantLoginResponse validateCredentials (@RequestBody RestaurantCredentials restaurantCredentialsEntered) {

        String username = restaurantCredentialsEntered.getUsername();
        Optional<RestaurantCredentials> credentials_db = restaurantCredentialsRepository.findById(username);

        RestaurantLoginResponse response = new RestaurantLoginResponse();

        if(credentials_db.isPresent()) {
            String passwordFromDB = credentials_db.get().getPassword();
            String passwordEntered = restaurantCredentialsEntered.getPassword();

            if(passwordEntered.equals(passwordFromDB)) {
                List<Restaurant> all = restaurantRepository.findAll();
                for(Restaurant restaurant : all) {
                    if(restaurant.getEmail().equals(restaurantCredentialsEntered.getUsername())) {
                        response.setRestaurantId(restaurant.getId());
                        response.setRestaurantName(restaurant.getRestaurantName());
                    }
                }
                response.setLoginStatus(true);
            } else {
                response.setLoginStatus(false);
                response.setMessage("Incorrect Password");
            }

        } else {
            response.setLoginStatus(false);
            response.setMessage("Account not found");
        }

        return response;
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Restaurant> getAllUsers() {
        // This returns a JSON or XML with the users
        return restaurantRepository.findAll();
    }


    @PostMapping(path="/propose-challenge")
    public @ResponseBody
    ProposeChallengeResponse proposeChallenge (@RequestBody OpenChallenge openChallenge) {

       ProposeChallengeResponse challengeResponse = new ProposeChallengeResponse();
       try {
           openChallenge.setVacancy(openChallenge.getMaximum());
           openChallengeRepository.save(openChallenge);
           challengeResponse.setProposeChallengeStatus(true);
       }
       catch(Exception e) {
           challengeResponse.setProposeChallengeStatus(false);
           challengeResponse.setProposeChallengeMessage(e.getMessage());
        }

       return  challengeResponse;
    }

    @GetMapping(path="/all-proposed-challenges")
    public @ResponseBody Iterable<OpenChallenge> getAllProposedChallenges() {
        // This returns a JSON or XML with the users
        return openChallengeRepository.findAll();
    }

    @PostMapping(path="/get-enrolled-challenges")
    public @ResponseBody Iterable<String> getEnrolledChallenges(@RequestBody JoinChallengeInfo joinChallengeInfo) {
        Optional<RestaurantChallenges> resChallengesFromDb = restaurantChallengesRepository.findById(joinChallengeInfo.getRestaurantId());
        List<String> enrolledChallenges = new ArrayList<>();
        if(resChallengesFromDb.isPresent()) {
            for(String challenge : resChallengesFromDb.get().getChallengeList().split(DELIMITTER_COMMA)) {
                enrolledChallenges.add(challenge.trim());
            }
        }

        return  enrolledChallenges;
    }

    @PostMapping(path="/get-enrolled-challenge-names")
    public @ResponseBody Iterable<String> getEnrolledChallengeNames(@RequestBody JoinChallengeInfo joinChallengeInfo) {
        Optional<RestaurantChallenges> resChallengesFromDb = restaurantChallengesRepository.findById(joinChallengeInfo.getRestaurantId());
        List<String> enrolledChallenges = new ArrayList<>();
        if(resChallengesFromDb.isPresent()) {
            for(String challenge : resChallengesFromDb.get().getChallengeNameList().split(DELIMITTER_COMMA)) {
                enrolledChallenges.add(challenge.trim());
            }
        }

        return  enrolledChallenges;
    }

    @GetMapping(path="/all-live-challenges")
    public @ResponseBody Iterable<LiveChallenge> getAllLiveChallenges() {
        // This returns a JSON or XML with the users
        return liveChallengeRepository.findAll();
    }


    @PostMapping(path="/join-challenge")
    public @ResponseBody
    Boolean joinChallenge (@RequestBody JoinChallengeInfo joinChallengeInfo) {
        return restaurantJoinChallengeService.joinChallenge(joinChallengeInfo);
    }

    @PostMapping(path="/exit-challenge")
    public @ResponseBody
    Boolean exitChallenge (@RequestBody JoinChallengeInfo joinChallengeInfo) {
        return restaurantExitChallengeService.exitChallenge(joinChallengeInfo);
    }

    @PostMapping(path = "/save-res-challenge")
    public  @ResponseBody  void saveRestaurantChallenges(@RequestBody ChallengeRestaurantAssociation cra) {
        Optional<ChallengeRestaurantAssociation> cra_db = challengeRestaurantAssociationRepository.findById(cra.getChallengeTitle());
        if(cra_db.isPresent()) {

            cra.setRestaurantList(cra.getRestaurantList() + DELIMITTER_COMMA + cra_db.get().getRestaurantList());
            challengeRestaurantAssociationRepository.save(cra);
        } else {
            challengeRestaurantAssociationRepository.save(cra);
        }
    }

    @PostMapping(path = "/get-all-restaurants-for-challenge")
    public @ResponseBody
    Iterable<String> getAllRestaurantsForChallenge(@RequestBody CustomerChallenegeAssociation cca) {
        Optional<ChallengeRestaurantAssociation> cra_db = challengeRestaurantAssociationRepository.findById(cca.getChallengeTitle());
        List<String> op = new ArrayList<>();
        if(cra_db.isPresent()) {
            for(String s : cra_db.get().getRestaurantList().split(DELIMITTER_COMMA)) {
                op.add(s.trim());
            }
        }

        return op;
    }

}