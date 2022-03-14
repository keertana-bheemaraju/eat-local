package com.vtc.eatlocal.service.user;


import com.vtc.eatlocal.entity.CustomerCredentials;
import com.vtc.eatlocal.entity.User;
import com.vtc.eatlocal.model.CustomerCreateAccountResponse;
import com.vtc.eatlocal.model.CustomerLoginResponse;
import com.vtc.eatlocal.repository.CustomerCredentialsRepository;
import com.vtc.eatlocal.repository.UserRepository;
import com.vtc.eatlocal.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {

    @Autowired
    CustomerCredentialsRepository customerCredentialsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    public CustomerCreateAccountResponse createAccount(@RequestBody User user) {

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


    public CustomerLoginResponse validateCredentials(CustomerCredentials customerCredentialsEntered) {

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


    public CustomerLoginResponse resetCustomerPassword( CustomerCredentials customerCredentials) {
        // if email address exists, reset password, otherwise send error message
        Optional<CustomerCredentials> credsRecord_db = customerCredentialsRepository.findById(customerCredentials.getUsername());

        CustomerLoginResponse customerLoginResponse = new CustomerLoginResponse();

        if(credsRecord_db.isPresent()) {
            customerCredentialsRepository.save(customerCredentials);
            customerLoginResponse.setLoginStatus(true);
            customerLoginResponse.setMessage("success");
        } else {
            customerLoginResponse.setMessage("email doesn't exist");
            customerLoginResponse.setLoginStatus(false);
        }


        return customerLoginResponse;
    }


    public CustomerLoginResponse sendPasswordResetEmail(User user) {

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
}
