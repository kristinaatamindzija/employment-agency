package com.example.paypalservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Merchant {

    @Id
    @SequenceGenerator(name = "merchant_id_gen", sequenceName = "merchant_id_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "merchant_id_gen")
    private Long merchantId;

    @Column
    private String merchantPaypalId;

    @Column
    private String email;
}
