package com.bank.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @SequenceGenerator(
            name = "payment_sequence_generator",
            sequenceName = "payment_sequence"
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_sequence_generator")
    private Long id;

    @Column
    private String merchantPan;

    @Column
    private String buyerPan = "0000000000000000";

    @Column
    private Double amount;

    @Column
    private Long paymentId;

    @Column
    private String successUrl;

    @Column
    private String failedUrl;

    @Column
    private String errorUrl;



    public Payment(String merchantPan, Double amount, Long paymentId) {
        this.merchantPan = merchantPan;
        this.amount = amount;
        this.paymentId = paymentId;
    }

    public Payment(Long paymentId, Double amount, String pan) {
        this.buyerPan = buyerPan;
        this.amount = amount;
        this.paymentId = paymentId;
    }
}
