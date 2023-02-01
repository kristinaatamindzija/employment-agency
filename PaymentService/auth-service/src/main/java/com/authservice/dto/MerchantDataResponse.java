package com.authservice.dto;

import com.authservice.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
public class MerchantDataResponse {
    public String merchantId;
    public String merchantPassword;
    public String successUrl;
    public String failUrl;
    public String errorUrl;
    public String bankUrl;
    public Set<PaymentMethod> paymentMethods;
    public String bitcoinApiToken;
}
