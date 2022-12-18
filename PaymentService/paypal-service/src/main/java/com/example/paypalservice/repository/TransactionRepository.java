package com.example.paypalservice.repository;

import com.example.paypalservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByMerchantUuidAndProductUuid(String merchantUuid, String productUuid);
    Transaction findByProductUuid(String productUuid);
}