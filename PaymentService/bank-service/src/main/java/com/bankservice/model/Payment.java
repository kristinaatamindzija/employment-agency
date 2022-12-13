package com.bankservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @SequenceGenerator(name = "merchant_account_sequence_generator",
            sequenceName = "merchant_account_sequence"
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "merchant_account_sequence_generator")
    @Column
    public Long id;
    @Column
    public Long paymentId;
    @Column
    public String merchantUuid;
    @Column
    public Double amount;
    @Column
    public String merchantOrderId;
    @Column
    public Date merchantTimestamp;
    @Column
    public String acquirerOrderId;
    @Column
    public Date acquirerTimestamp;
    @Column
    public PaymentStatus status;

    public Payment(String merchantUuid, String merchantOrderId, Double amount) {
        this.merchantUuid = merchantUuid;
        this.amount = amount;
        this.merchantOrderId = merchantOrderId;
        this.merchantTimestamp = new Date();
        setStatus(PaymentStatus.PENDING);
    }
}
