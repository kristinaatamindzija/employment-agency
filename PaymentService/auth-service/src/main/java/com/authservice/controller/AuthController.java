package com.authservice.controller;

import com.authservice.dto.LoginResponseDTO;
import com.authservice.dto.WebShopDTO;
import com.authservice.model.WebShop;
import com.authservice.service.WebShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    @Autowired
    private WebShopService webShopService;

    @PostMapping("/register")
    public ResponseEntity<WebShop> register(@RequestBody WebShopDTO registrationDTO) {
        UserDetails existUser = this.webShopService.loadUserByUsername(registrationDTO.getUsername());
        if (existUser != null) {
            throw new NullPointerException("Username already exists: " + registrationDTO.getUsername());
        }
        WebShop webShop = webShopService.register(registrationDTO.getUsername(), registrationDTO.getPassword(),
                registrationDTO.getCurrency());
        return new ResponseEntity<>(webShop, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody WebShopDTO loginDTO) {
        LoginResponseDTO response = webShopService.login(loginDTO);
        if (response == null) {
            throw new NullPointerException("Invalid username or password");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
