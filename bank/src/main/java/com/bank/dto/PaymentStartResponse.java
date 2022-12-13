package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class PaymentStartResponse {
    public Long paymentId;
    public String paymentUrl;

    @Override
    public String toString(){
        return "StartPaymentProcessResponse(paymentUrl=" + paymentUrl + ", paymentId=" + paymentId + ")";
    }
}
