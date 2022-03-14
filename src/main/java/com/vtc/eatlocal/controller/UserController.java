package com.vtc.eatlocal.controller;

import com.vtc.eatlocal.composition.CustomerLiveChallengeComposition;
import com.vtc.eatlocal.entity.*;
import com.vtc.eatlocal.model.CustomerChallenegeAssociation;
import com.vtc.eatlocal.model.CustomerLoginResponse;
import com.vtc.eatlocal.repository.*;
import com.vtc.eatlocal.model.CustomerCreateAccountResponse;
import com.vtc.eatlocal.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path="/user-path") // This means URL's start with /demo (after Application path)
public class UserController {

    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserRepository userRepository;

    @Autowired
    private CustomerCredentialsRepository customerCredentialsRepository;

    @Autowired
    private OpenChallengeRepository openChallengeRepository;

//    @Autowired
//    private CustomerChallengesRepository customerChallengesRepository;

    @Autowired
    private ChallengeCustomerRestaurantAssociationRepository challengeCustomerRestaurantAssociationRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    LiveChallengeRepository liveChallengeRepository;



    @PostMapping(path = "/save-data") // Map ONLY POST Requests
    public @ResponseBody
    CustomerCreateAccountResponse createAccount(@RequestBody User user) {

        CustomerCreateAccountResponse response = new CustomerCreateAccountResponse();

        // return error if email already exists
        String email = user.getEmail();
        boolean emailExists = customerCredentialsRepository.findById(email).isPresent();
        if (emailExists) {
            response.setCreateAccountStatus(false);
            response.setCustomerCreateAccountMessage("Email already exists");
        } else {
            // Save user data
            response.setCreateAccountStatus(true);
            user = userRepository.save(user);
            response.setCustomerId(user.getCustomerId());

            CustomerCredentials customerCredentials = new CustomerCredentials();
            customerCredentials.setUsername(user.getEmail());
            customerCredentials.setPassword(user.getPassword());

            customerCredentialsRepository.save(customerCredentials);
        }


        return response;
    }


//    @PostMapping (path="/restaurant-proposechallenge")  // Map ONLY POST Requests
//    public @ResponseBody


    @PostMapping(path = "/signin") // Map ONLY POST Requests
    public @ResponseBody
    CustomerLoginResponse validateCredentials(@RequestBody CustomerCredentials customerCredentialsEntered) {

        String username = customerCredentialsEntered.getUsername();
        Optional<CustomerCredentials> credentials_db = customerCredentialsRepository.findById(username);

        CustomerLoginResponse response = new CustomerLoginResponse();

        if (credentials_db.isPresent()) {
            String passwordFromDB = credentials_db.get().getPassword();
            String passwordEntered = customerCredentialsEntered.getPassword();

            if (passwordEntered.equals(passwordFromDB)) {
                response.setLoginStatus(true);
                List<User> all = userRepository.findAll();
                for(User user : all) {
                    if(user.getEmail().equals(customerCredentialsEntered.getUsername())) {
                        response.setCustomerName(user.getFname());
                        response.setCustomerId(user.getCustomerId());
                    }
                }
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

    @GetMapping(path = "/get-all-users")
    public @ResponseBody
    Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }

    @PostMapping(path = "/get-county-list")
    public @ResponseBody
    Set<String> getCountyList(@RequestBody List<String> cusineList) {
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

    @PostMapping(path = "/get-cuisine-list")
    public @ResponseBody
    Set<String> getCuisineList(@RequestBody List<String> countyList) {
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


    @PostMapping(path = "/save-customer-res-challenge")
    public  @ResponseBody  void saveCustomerRestaurantChallenges(@RequestBody ChallengeCustomerRestaurantAssociation ccra) {

        Optional<ChallengeCustomerRestaurantAssociation> ccra_db = challengeCustomerRestaurantAssociationRepository.findById(ccra.getCustomerId());

        if(ccra_db.isPresent()) {
            String new_list = ccra_db.get().getValidatedRestaurantsList() + ", " + ccra.getValidatedRestaurantsList();
            ccra.setValidatedRestaurantsList(new_list);
            challengeCustomerRestaurantAssociationRepository.save(ccra);
        } else {
            challengeCustomerRestaurantAssociationRepository.save(ccra);
        }

    }

    @PostMapping(path = "/get-validated-restaurants-for-challenge")
    public @ResponseBody
    Iterable<String> getAllRestaurantsForChallenge(@RequestBody CustomerChallenegeAssociation cca) {

        Optional<ChallengeCustomerRestaurantAssociation> cca_db = challengeCustomerRestaurantAssociationRepository.findById(cca.getCustomerId());

        if(cca_db.isPresent()) {
            ChallengeCustomerRestaurantAssociation challengeCustomerRestaurantAssociation = cca_db.get();
            List<String> output = new ArrayList<>();
            String restaurants = challengeCustomerRestaurantAssociation.getValidatedRestaurantsList();
            String[] split = restaurants.split(",");
            for(String s : split) {
                output.add(s.trim());
            }

          return output;

        } else {
            return new ArrayList<>();
        }

    }




    @PostMapping(path = "/send-password-reset-email")
    public @ResponseBody
    CustomerLoginResponse sendPasswordResetEmail(@RequestBody User user) {

        // if email address exists, send reset email to that address // else return error message
        Optional<CustomerCredentials> credsRecord_db = customerCredentialsRepository.findById(user.getEmail());

        CustomerLoginResponse customerLoginResponse = new CustomerLoginResponse();

        if(credsRecord_db.isPresent()) {
            emailService.sendCustomerPasswordResetEmail(user.getEmail());
            customerLoginResponse.setLoginStatus(true);
            customerLoginResponse.setMessage("password reset email sent");

        } else {
            customerLoginResponse.setLoginStatus(false);
            customerLoginResponse.setMessage("email doesn't exist");
        }

        return customerLoginResponse;

    }

    @PostMapping(path = "/reset-customer-password")
    public @ResponseBody
    boolean resetCustomerPassword(@RequestBody CustomerCredentials customerCredentials) {

        customerCredentialsRepository.save(customerCredentials);

        return true;
    }

    @GetMapping(path="/all-live-challenges")
    public @ResponseBody Iterable<LiveChallenge> getAllLiveChallenges() {
        // This returns a JSON or XML with the users
        return liveChallengeRepository.findAll();
    }

//    @PostMapping(path="/save-customer-challenge")
//    public @ResponseBody boolean saveCustomerChallenge(CustomerChallenges customerChallenges) {
//        // This returns a JSON or XML with the users
//         customerChallengesRepository.save(customerChallenges);
//         return true;
//    }

//    @PostMapping(path="/join-challenge")
//    public @ResponseBody
//    Boolean joinChallenge (@RequestBody CustomerChallenegeAssociation customerChallenegeAssociation) {
//        Optional<LiveChallenge> byId = liveChallengeRepository.findById(customerChallenegeAssociation.getChallengeId());
//
//
//            // Store this challenge against the customerId
//            Optional<CustomerChallenges> resChallenge_db = restaurantChallengesRepository.findById(joinChallengeInfo.getRestaurantId());
//            if(resChallenge_db.isPresent()) {
//                String challengeList_from_db = resChallenge_db.get().getChallengeList();
//                String updated_challenge_list = challengeList_from_db + ", " + joinChallengeInfo.getChallengeId();
//
//                String challengeNameList_from_db = resChallenge_db.get().getChallengeNameList();
//                String updated_challengeName_list = challengeNameList_from_db + ", " + joinChallengeInfo.getChallengeName();
//
//
//                RestaurantChallenges restaurantChallenges = new RestaurantChallenges();
//                restaurantChallenges.setRestaurantId(joinChallengeInfo.getRestaurantId());
//                restaurantChallenges.setChallengeList(updated_challenge_list);
//                restaurantChallenges.setChallengeNameList(updated_challengeName_list);
//                restaurantChallengesRepository.save(restaurantChallenges);
//
//            } else {
//                RestaurantChallenges restaurantChallenges = new RestaurantChallenges();
//                restaurantChallenges.setRestaurantId(joinChallengeInfo.getRestaurantId());
//                restaurantChallenges.setChallengeList(joinChallengeInfo.getChallengeId());
//                restaurantChallenges.setChallengeNameList(joinChallengeInfo.getChallengeName());
//                restaurantChallengesRepository.save(restaurantChallenges);
//            }
//
//
//            return true;
//        }
//
//        return false;
//
//    }


    @PostMapping(path="/join-challenge")
    public @ResponseBody boolean joinChallenge(@RequestBody CustomerLiveChallengeComposition composition) {

        User customer = composition.getCustomer();
        LiveChallenge challenge = composition.getLiveChallenge();
        Optional<User> byId = userRepository.findById(customer.getCustomerId());
        if(!byId.isPresent()) {
            return false;
        }

        customer = byId.get();

        Set<LiveChallenge> customerChallenges = customer.getCustomerChallenges();

        if(customerChallenges == null) {
            customerChallenges = new HashSet<>();
        }

        customerChallenges.add(challenge);

        customer.setCustomerChallenges(customerChallenges);

        userRepository.save(customer);

        return true;
    }


    @GetMapping("/get-customer-challenges")
    @ResponseBody
    public Set<LiveChallenge> getCustomerChallenges(@RequestParam Integer customerId) {

        Set<LiveChallenge> customerChallenges = new HashSet<>();

        Optional<User> byId = userRepository.findById(customerId);
        if(byId.isPresent()) {
            customerChallenges = byId.get().getCustomerChallenges();
        }

        return customerChallenges;

    }

}
