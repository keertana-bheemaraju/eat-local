package com.vtc.eatlocal.repository;

import com.vtc.eatlocal.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {


}
