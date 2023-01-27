package com.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class MerchantDataResponse {
    public String merchantId;
    public String merchantPassword;
    public String successUrl;
    public String failUrl;
    public String errorUrl;
}
