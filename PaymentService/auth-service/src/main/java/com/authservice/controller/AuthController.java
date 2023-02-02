package com.authservice.controller;

import com.authservice.dto.LoginResponseDTO;
import com.authservice.dto.MerchantDataResponse;
import com.authservice.dto.PaymentMethodDTO;
import com.authservice.dto.WebShopDTO;
import com.authservice.model.WebShop;
import com.authservice.service.WebShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AuthController {

    private final WebShopService webShopService;

    public AuthController(WebShopService webShopService) {
        this.webShopService = webShopService;
    }

    @PostMapping("/register")
    public ResponseEntity<WebShop> register(@RequestBody WebShopDTO registrationDTO) {
        UserDetails existUser = this.webShopService.loadUserByUsername(registrationDTO.getUsername());
        if (existUser != null) {
            log.error("User with username: " + registrationDTO.getUsername() + " already exists");
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
            log.error("User with username: " + loginDTO.getUsername() + " does not exist");
            throw new NullPointerException("Invalid username or password");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/addPaymentMethod")
    public ResponseEntity<?> addPaymentMethod(@RequestBody PaymentMethodDTO paymentMethodDTO) {
        webShopService.addPaymentMethod(paymentMethodDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/deletePaymentMethod")
    public ResponseEntity<?> deletePaymentMethod(@RequestBody PaymentMethodDTO paymentMethodDTO) {
        webShopService.deletePaymentMethod(paymentMethodDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/merchantData/{merchantUuid}")
    public ResponseEntity<MerchantDataResponse> getMerchantData(@PathVariable String merchantUuid) {
        return new ResponseEntity<>(webShopService.getMerchantData(merchantUuid), HttpStatus.OK);
    }
}
