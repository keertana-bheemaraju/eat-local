package com.vtc.eatlocal.repository;

import com.vtc.eatlocal.entity.CustomerCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerCredentialsRepository extends JpaRepository<CustomerCredentials, String> {


}
