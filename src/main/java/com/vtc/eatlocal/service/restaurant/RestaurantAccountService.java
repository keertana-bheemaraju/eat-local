package com.vtc.eatlocal.service.restaurant;

import com.vtc.eatlocal.entity.CustomerCredentials;
import com.vtc.eatlocal.entity.Restaurant;
import com.vtc.eatlocal.entity.RestaurantCredentials;
import com.vtc.eatlocal.entity.User;
import com.vtc.eatlocal.model.CustomerLoginResponse;
import com.vtc.eatlocal.model.RestaurantLoginResponse;
import com.vtc.eatlocal.repository.RestaurantCredentialsRepository;
import com.vtc.eatlocal.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RestaurantAccountService {

    @Autowired
    RestaurantCredentialsRepository restaurantCredentialsRepository;

    @Autowired
    EmailService emailService;

    public RestaurantLoginResponse resetRestaurantPassword( RestaurantCredentials restaurantCredentials) {
        // if email address exists, reset password, otherwise send error message
        Optional<RestaurantCredentials> credsRecord_db = restaurantCredentialsRepository.findById(restaurantCredentials.getUsername());

        RestaurantLoginResponse restaurantLoginResponse = new RestaurantLoginResponse();

        if(credsRecord_db.isPresent()) {
            restaurantCredentialsRepository.save(restaurantCredentials);
            restaurantLoginResponse.setLoginStatus(true);
            restaurantLoginResponse.setMessage("success");
        } else {
            restaurantLoginResponse.setMessage("email doesn't exist");
            restaurantLoginResponse.setLoginStatus(false);
        }


        return restaurantLoginResponse;
    }


    public RestaurantLoginResponse sendPasswordResetEmail(Restaurant restaurant) {

        // if email address exists, send reset email to that address // else return error message
        Optional<RestaurantCredentials> credsRecord_db = restaurantCredentialsRepository.findById(restaurant.getEmail());

        RestaurantLoginResponse restaurantLoginResponse = new RestaurantLoginResponse();

        if(credsRecord_db.isPresent()) {
            emailService.sendRestaurantPasswordResetEmail(restaurant.getEmail());
            restaurantLoginResponse.setLoginStatus(true);
            restaurantLoginResponse.setMessage("password reset email sent");

        } else {
            restaurantLoginResponse.setLoginStatus(false);
            restaurantLoginResponse.setMessage("email doesn't exist");
        }

        return restaurantLoginResponse;

    }
}
