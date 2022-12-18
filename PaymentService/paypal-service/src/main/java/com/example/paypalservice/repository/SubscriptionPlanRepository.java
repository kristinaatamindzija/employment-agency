package com.example.paypalservice.repository;

import com.example.paypalservice.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
    SubscriptionPlan findByMerchantIdAndProductId(Long merchantId, Long productId);

}
