package com.pcc.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

public class PaymentRequest {
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

    public String acquirerOrderId;

    public Date acquirerTimestamp = new Date();

    @NotNull(message = "Amount cannot be empty!")
    public Double amount;

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "pan='" + pan + '\'' +
                ", securityCode='" + securityCode + '\'' +
                ", cardHolderName='" + cardHolderName + '\'' +
                ", validThru=" + validThru +
                ", paymentId=" + paymentId +
                ", acquirerOrderId='" + acquirerOrderId + '\'' +
                ", acquirerTimestamp=" + acquirerTimestamp +
                ", amount=" + amount +
                '}';
    }
}
