package com.bankservice.dto;

import java.util.Date;

public class ProcessedPaymentRequest {
    public Long paymentId;
    public String acquirerOrderId;
    public Date acquirerOrderTimeStamp;
}
