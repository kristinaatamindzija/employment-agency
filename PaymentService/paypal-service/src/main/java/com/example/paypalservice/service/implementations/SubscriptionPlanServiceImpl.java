package com.example.paypalservice.service.implementations;

import com.example.paypalservice.model.SubscriptionPlan;
import com.example.paypalservice.repository.SubscriptionPlanRepository;
import com.example.paypalservice.service.SubscriptionPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionPlanServiceImpl(SubscriptionPlanRepository subscriptionPlanRepository) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    @Override
    public void createSubscriptionPlan(String merchantUuid, Long productId, String planPaypalId) {
        subscriptionPlanRepository.save(new SubscriptionPlan(merchantUuid, productId, planPaypalId));
        log.info("Subscription plan with paypal id: " + planPaypalId + " created");
    }

    @Override
    public String getSubscriptionPlanId(String merchantUuid, Long productId) {
        return subscriptionPlanRepository.findByMerchantUuidAndProductId(merchantUuid, productId).getPlanPaypalId();
    }
}
