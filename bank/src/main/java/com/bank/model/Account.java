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
public class Account {
    @Id
    @SequenceGenerator(
            name = "account_sequence_generator",
            sequenceName = "account_sequence"
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_sequence_generator")
    @Column
    public Long id;

    @Column
    public String name;

    @Column
    public String surname;

    @Column
    public String password;

    @Column
    public String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_card_id", referencedColumnName = "id")
    public CreditCard creditCard;

    @Column(unique = true, columnDefinition = "VARCHAR(32) DEFAULT '00000000000000000000000000000000'")
    public String merchantId;

    @Column
    public String accountNumber;
}

