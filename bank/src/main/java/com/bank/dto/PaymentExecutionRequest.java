package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
public class PaymentExecutionRequest {
    @NotBlank(message = "PAN cannot be empty!")
    @Pattern(regexp = "^[0-9]{16}", message = "Please use just numbers for PAN!")
    public String pan;

    @NotBlank(message = "Security Code cannot be empty!")
    @Pattern(regexp = "^[0-9]{3}", message = "Please use just numbers for security code!")
    public String securityCode;

    @NotBlank(message = "Card Holder Name cannot be empty!")
    public String cardHolderName;

    public Date validThru = new Date();

    public Long paymentId;

    @Override
    public String toString() {
        return "ExecutePaymentRequest(pan='" + pan + "', securityCode='" + securityCode +
                "', cardHolderName='" + cardHolderName + "', validThru=" + validThru +
                ", paymentId='" + paymentId + "')";
    }
}

