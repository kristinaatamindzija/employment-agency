package com.bankcardservice.model;
import jakarta.persistence.*;

@Entity
public class Payment {
    @Id
    @SequenceGenerator( name = "merchant_account_sequence_generator", sequenceName = "merchant_account_sequence",
            initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "merchant_account_sequence_generator")
    @Column(  name = "id", unique = true)
    public Long id;
    @Column
    public String orderId;
    @Column
    public String merchantUuid;
    @Column
    public Double amount;
    @Column
    public Date merchantTimestamp;

}
