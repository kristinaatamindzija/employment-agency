package com.authservice.service;

import com.authservice.model.WebShop;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface WebShopService extends UserDetailsService {
    WebShop registerUser(String username, String password, String currency);
}
