package com.vtc.eatlocal.repository;

import com.vtc.eatlocal.entity.ChallengeCustomerRestaurantAssociation;
import com.vtc.eatlocal.entity.ChallengeRestaurantAssociation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRestaurantAssociationRepository extends JpaRepository<ChallengeRestaurantAssociation, String> {
}
