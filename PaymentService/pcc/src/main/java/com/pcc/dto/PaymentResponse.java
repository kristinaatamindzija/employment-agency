package com.pcc.dto;

import java.util.Date;

public class PaymentResponse {
    public Long paymentId;

    public Boolean isAuthenticationSuccessful;

    public Boolean isAuthorizationSuccessful;

    public String acquirerOrderId;

    public Date acquirerTimestamp =  new Date();

    public String issuerOrderId;

    public Date issuerTimestamp = new Date();
}
