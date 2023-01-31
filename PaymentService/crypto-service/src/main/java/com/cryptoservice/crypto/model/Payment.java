package com.cryptoservice.crypto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @Column
    private String id;

    @Column
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column
    private LocalDateTime createdAt;

    @Column
    private String merchantUuid;

    @Column
    private String orderId;

    @Column
    private Double payAmount;

    @Column
    private String payCurrency;

    @Column
    private Double priceAmount;

    @Column
    private String priceCurrency;

    @Column
    private String receiveCurrency;

    @Column
    private Double receiveAmount;

}
