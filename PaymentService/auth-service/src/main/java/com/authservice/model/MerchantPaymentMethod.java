package com.authservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CascadeType;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MerchantPaymentMethod {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "merchant_payment_method_id_gen", sequenceName = "merchant_payment_method_id_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "merchant_payment_method_id_gen")
    private Long id;

    @OneToOne
    @JoinColumn(name = "payment_method_id", referencedColumnName = "id")
    private PaymentMethod paymentMethod;

    @Column(name = "merchant_id")
    private String merchantId;

    @Column(name = "merchant_password")
    private String merchantPassword;

}
