package com.example.paypalservice.dto;

import lombok.Getter;

@Getter
public class AuthServiceResponse {
    public String merchantId;
    public String successUrl;
    public String failUrl;
    public String errorUrl;
}
