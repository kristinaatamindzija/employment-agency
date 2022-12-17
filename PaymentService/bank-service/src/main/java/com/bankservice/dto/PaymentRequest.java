package com.bankservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PaymentRequest {
    public String merchantId;
    public String merchantPassword;
    public Double amount;
    public String merchantOrderId;
    public Date merchantTimestamp;
    public String successUrl;
    public String failUrl;
    public String errorUrl;
    public Boolean qr;
}
