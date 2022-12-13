package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
public class ProcessedPaymentRequest {
    public Long paymentId;
    public Long acquirerOrderId;
    public Date acquirerOrderTimeStamp;
}
