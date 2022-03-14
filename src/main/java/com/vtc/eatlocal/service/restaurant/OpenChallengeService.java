package com.vtc.eatlocal.service.restaurant;


import com.vtc.eatlocal.entity.OpenChallenge;
import com.vtc.eatlocal.repository.OpenChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class OpenChallengeService {

    @Autowired
    OpenChallengeRepository openChallengeRepository;

    public boolean doesOpenChallengeExist(String challengeName) {

        List<OpenChallenge> all = openChallengeRepository.findAll();

        for(OpenChallenge openChallenge : all) {
            if(StringUtils.deleteAny(openChallenge.getChallengeTitle(), " ")
                    .equalsIgnoreCase(StringUtils.deleteAny(challengeName, " "))) {
                return true;
            }
        }

        return false;
    }
}
