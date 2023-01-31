package com.cryptoservice.crypto.repository;

import com.cryptoservice.crypto.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}
