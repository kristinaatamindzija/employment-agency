package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class PaymentExecutionResponse {
    public String redirectionUrl = "";

    @Override
    public String toString() {
        return "ExecutePaymentResponse(redirectionUrl='" + redirectionUrl + "')";
    }
}
