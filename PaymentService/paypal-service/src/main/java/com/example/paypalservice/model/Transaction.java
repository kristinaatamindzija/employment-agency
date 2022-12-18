package com.example.paypalservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.lang.reflect.Type;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private StatusTransaction status;

    @Column
    private Date timestamp;

    @Column
    private String merchantUuid;

    @Column
    private String productUuid;

    @Column
    private String payerId;

    public Transaction(StatusTransaction status, Date timestamp, String merchantUuid, String productUuid, String payerId) {
        this.status = status;
        this.timestamp = timestamp;
        this.merchantUuid = merchantUuid;
        this.productUuid = productUuid;
        this.payerId = payerId;
    }
}
