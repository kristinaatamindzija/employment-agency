package com.bankservice.dto;

public class StartPaymentRequest {
    public Double amount;
    public Boolean qr;
    public String merchantUuid;
    public String merchantOrderId;
}
