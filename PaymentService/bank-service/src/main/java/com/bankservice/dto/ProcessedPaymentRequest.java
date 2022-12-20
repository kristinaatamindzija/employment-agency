package com.bankservice.dto;

import com.bankservice.model.PaymentStatus;

import java.util.Date;

public class ProcessedPaymentRequest {
    public Long paymentId;
    public String acquirerOrderId;
    public Date acquirerOrderTimeStamp;
    public PaymentStatus status;
}
