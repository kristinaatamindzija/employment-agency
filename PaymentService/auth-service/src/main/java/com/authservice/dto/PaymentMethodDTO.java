package com.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PaymentMethodDTO {
    public Long paymentMethodId;
    public String merchantUuid;
    public String merchantId;
    public String merchantPassword;
    //public String PaypalUsername;
    //public String BitcoinWalletId;
}
