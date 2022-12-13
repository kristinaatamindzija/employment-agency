package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
public class PaymentStartRequest {
    @NotBlank(message = "Merchant ID cannot be empty!")
    public String merchantId;

    @NotBlank(message = "Merchant password cannot be empty!")
    public String merchantPassword;

    @NotNull(message = "Amount cannot be empty!")
    public Double amount;

    @NotBlank(message = "Merchant order id cannot be empty!")
    public String merchantOrderId;

    @NotNull(message = "Timestamp cannot be empty!")
    public Date merchantTimestamp;

    @NotBlank(message = "Success URL cannot be empty!")
    public String successUrl;

    @NotBlank(message = "Failed URL cannot be empty!")
    public String failUrl;

    @NotBlank(message = "Error URL cannot be empty!")
    public String errorUrl;

    @NotNull
    public Boolean qr;
}
