package com.example.paypalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@AllArgsConstructor
public class MerchantDTO {
    private String merchantUuid;

    private String merchantPaypalId;

    private String email;
}
