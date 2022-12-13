package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
public class MerchantRegistrationRequest {
    @NotBlank(message = "PAN cannot be empty!")
    @Pattern(regexp = "^[0-9]{16}", message = "Please use just numbers for PAN!")
    public String pan;

    @NotBlank(message = "Credit card holder cannot be empty!")
    public String creditCardHolder;
}
