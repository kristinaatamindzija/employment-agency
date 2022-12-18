package com.example.paypalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MerchantResponseDTO {
    private String merchantPaypalId;
    private String email;
}
