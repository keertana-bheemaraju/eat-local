package com.vtc.eatlocal.repository;

import com.vtc.eatlocal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {
}
