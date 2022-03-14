package com.vtc.eatlocal.repository;

import com.vtc.eatlocal.entity.RestaurantCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantCredentialsRepository extends JpaRepository<RestaurantCredentials, String> {


}
