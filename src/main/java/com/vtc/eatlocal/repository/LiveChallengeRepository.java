package com.vtc.eatlocal.repository;


import com.vtc.eatlocal.entity.LiveChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiveChallengeRepository extends JpaRepository<LiveChallenge, Integer> {
}
