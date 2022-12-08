package com.authservice.dto;

import com.authservice.model.WebShop;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    public String jwt;
    public int expiresIn;
    public WebShop webShop;
}
