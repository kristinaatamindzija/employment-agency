package com.authservice.dto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class WebShopDTO {
    public Long id;

    public String currency;

    public String username;

    public String password;

    public String merchantID;

    public String merchantPassword;

    public String successUrl;

    public String errorUrl;

    public String failureUrl;

}
