package com.authservice.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private String password;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "web_shop_payment_method",
            joinColumns = { @JoinColumn(name = "web_shop_id") },
            inverseJoinColumns = { @JoinColumn(name = "merchant_payment_id") }
    )
    private Set<MerchantPaymentMethod> merchantPaymentMethods;
}
