package com.example.paypalservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlan {

    @Id
    @SequenceGenerator(name = "plan_id_gen", sequenceName = "plan_id_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plan_id_gen")
    private Long id;

    @Column
    private Long merchantId;

    @Column
    private Long productId;

    @Column
    private String planPaypalId;

    public SubscriptionPlan(Long merchantId, Long productId, String planPaypalId) {
        this.merchantId = merchantId;
        this.productId = productId;
        this.planPaypalId = planPaypalId;
    }

}
