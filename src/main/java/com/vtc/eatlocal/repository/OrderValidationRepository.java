package com.vtc.eatlocal.repository;


import com.vtc.eatlocal.entity.OrderValidation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderValidationRepository extends JpaRepository<OrderValidation, Integer> {
}
