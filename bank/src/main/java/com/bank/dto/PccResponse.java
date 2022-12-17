package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PccResponse {
    public Long paymentId;
    public Boolean isAuthenticationSuccessful;
    public Boolean isAuthorizationSuccessful;

    public Long acquirerOrderId;

    public Date acquirerTimestamp;

    public Long issuerOrderId;

    public Date issuerTimestamp;

    public PccResponse(Boolean isAuthenticationSuccessful, Boolean isAuthorizationSuccessful) {
        this.isAuthenticationSuccessful = isAuthenticationSuccessful;
        this.isAuthorizationSuccessful = isAuthorizationSuccessful;
    }
}
