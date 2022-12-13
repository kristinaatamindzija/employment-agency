package com.authservice.repository;

import com.authservice.model.WebShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

public interface WebShopRepository extends JpaRepository<WebShop, Long> {
    WebShop findByUsername(String username);
    WebShop findByApiToken(String apiToken);

    @Query("select w from WebShop w where w.username = ?1")
    UserDetails loadByUsername(String username);

    WebShop findByMerchantUuid(String merchantUuid);
}
