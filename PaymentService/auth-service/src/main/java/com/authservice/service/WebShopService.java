package com.authservice.service;

import com.authservice.dto.LoginResponseDTO;
import com.authservice.dto.WebShopDTO;
import com.authservice.model.WebShop;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface WebShopService extends UserDetailsService {
    WebShop register(String username, String password, String currency);
    LoginResponseDTO login(WebShopDTO webShopDTO);
}
