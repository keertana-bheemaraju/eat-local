package com.vtc.eatlocal.service.user;

import com.vtc.eatlocal.entity.Reward;
import com.vtc.eatlocal.entity.RewardProgress;
import com.vtc.eatlocal.repository.RewardProgressRepository;
import com.vtc.eatlocal.repository.RewardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerRewardsService {

    @Autowired
    RewardRepository rewardRepository;

    @Autowired
    RewardProgressRepository rewardProgressRepository;

    public List<Reward> getCustomerRewards(int customerId) {
        List<Reward> customerRewards = new ArrayList<>();
        // key is of the form customerId-challengeId
        List<Reward> rewards = rewardRepository.findAll();

        for(Reward reward : rewards) {
            if(reward.getId().startsWith(Integer.toString(customerId))) {
                customerRewards.add(reward);
            }
        }

        return customerRewards;
    }

    public List<RewardProgress> getCustomerProgress(int customerId) {
        List<RewardProgress> customerProgress = new ArrayList<>();

        List<RewardProgress> all = rewardProgressRepository.findAll();
        for(RewardProgress rewardProgress : all) {
            if(rewardProgress.getCustomerId() == customerId) {
                customerProgress.add(rewardProgress);
            }
        }

        return customerProgress;
    }
}
