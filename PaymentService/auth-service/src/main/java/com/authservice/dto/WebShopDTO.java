package com.authservice.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WebShopDTO {
    public Long id;

    public String currency;

    public String username;

    public String password;

    public String apiToken;

    public String successUrl;

    public String errorUrl;

    public String failureUrl;

}
