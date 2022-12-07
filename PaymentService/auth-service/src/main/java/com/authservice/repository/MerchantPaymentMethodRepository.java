package com.authservice.repository;

import com.authservice.model.MerchantPaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantPaymentMethodRepository extends JpaRepository<MerchantPaymentMethod, Long> {
}
