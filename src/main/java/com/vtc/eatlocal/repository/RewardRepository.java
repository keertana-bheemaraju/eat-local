package com.vtc.eatlocal.repository;

import com.vtc.eatlocal.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardRepository extends JpaRepository<Reward, String> {
}
