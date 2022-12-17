package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfoResponse {
    public String merchantName;
    public String merchantSurname;
    public String merchantPAN;
    public Double amount;
}
