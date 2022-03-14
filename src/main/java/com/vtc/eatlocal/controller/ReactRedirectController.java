package com.vtc.eatlocal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.1.7:3000","http://192.168.1.7:8080/", "http://eat-local.us-east-1.elasticbeanstalk.com"})
public class ReactRedirectController {

    @RequestMapping(value={"/customer-signin", "/customer-homescreen", "/create-account", "/account-success", "/customer-signin", "/restaurant-login",
            "/customer-hereshelp", "/restaurant-createaccount", "/restaurant-hereshelp", "/challenge-preferences", "/restaurant-homescreen", "/restaurant-proposechallenge",
            "/restaurant-reportreward", "/restaurant-openchallenges", "/live-challenges", "/customer-participate-challenge", "/restaurant-exit-challenge",
            "/customer-password-reset", "/validate-order", "/customer-rewards", "/issue-reward", "/view-report", "/view-reward-report", "/landing-screen",
            "/navbar-header", "/reset-restaurant-password", "/customer-progress"
    })
    public String redirect() {
        return "index.html";
    }

}
