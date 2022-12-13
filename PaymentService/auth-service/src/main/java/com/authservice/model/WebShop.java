package com.authservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebShop {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "web_shop_id_gen", sequenceName = "web_shop_id_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "web_shop_id_gen")
    private Long id;

    @Column(name="currency")
    private String currency;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "api_token")
    private String apiToken;

    @Column(name = "merchant_uuid")
    private String merchantUuid;

    @Column(name = "merchant_id")
    private String merchantId;

    @Column(name = "merchant_password")
    private String merchantPassword;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "web_shop_payment_method",
            joinColumns = { @JoinColumn(name = "web_shop_id") },
            inverseJoinColumns = { @JoinColumn(name = "payment_method_id") }
    )
    private Set<PaymentMethod> paymentMethods;

    public WebShop(String username, String password, String currency) {
        this.username = username;
        this.password = password;
        this.currency = currency;
    }
}
