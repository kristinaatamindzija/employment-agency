package com.example.paypalservice.dto;

import com.example.paypalservice.model.StatusTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class TransactionRequestDTO {
    private StatusTransaction status;
    private Date timestamp;
    private String merchantUuid;
    private String productUuid;
    private String payerId;
}
