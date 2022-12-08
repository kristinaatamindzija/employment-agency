package com.authservice.model;


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
public class MerchantPaymentMethod {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "merchant_payment_method_id_gen", sequenceName = "merchant_payment_method_id_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "merchant_payment_method_id_gen")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_method_id", referencedColumnName = "id")
    private PaymentMethod paymentMethod;

    @Column(name = "merchant_id")
    private String merchantId;

    @Column(name = "merchant_password")
    private String merchantPassword;

    public MerchantPaymentMethod(PaymentMethod paymentMethod, String merchantId, String merchantPassword) {
        this.paymentMethod = paymentMethod;
        this.merchantId = merchantId;
        this.merchantPassword = merchantPassword;
    }
}
