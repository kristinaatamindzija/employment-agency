package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class MerchantRegistrationResponse {
    public String merchantId = "";
    public String merchantPassword = "";

    @Override
    public String toString() {
        return "MerchantRegistrationResponse(merchantId='" + merchantId + "', merchantPassword='" + merchantPassword + "')";
    }
}
