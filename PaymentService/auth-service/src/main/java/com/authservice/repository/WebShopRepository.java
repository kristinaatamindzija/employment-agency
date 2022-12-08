package com.authservice.repository;

import com.authservice.model.WebShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface WebShopRepository extends JpaRepository<WebShop, Long> {
    UserDetails findByUsername(String username);
}
