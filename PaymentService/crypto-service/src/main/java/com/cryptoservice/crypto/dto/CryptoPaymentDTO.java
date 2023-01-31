package com.cryptoservice.crypto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class CryptoPaymentDTO {
    public String title;
    public String priceAmount;
    public String priceCurrency;
    public String receiveCurrency;
    public String callbackUrl;
    public String successUrl;
    public String cancelUrl;
    public String orderId;
    public String description;
    public String merchantUuid;
}
