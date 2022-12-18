package com.example.paypalservice.service.implementations;

import com.example.paypalservice.model.SubscriptionPlan;
import com.example.paypalservice.repository.SubscriptionPlanRepository;
import com.example.paypalservice.service.SubscriptionPlanService;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionPlanServiceImpl(SubscriptionPlanRepository subscriptionPlanRepository) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    @Override
    public void createSubscriptionPlan(Long merchantId, Long productId, String planPaypalId) {
        subscriptionPlanRepository.save(new SubscriptionPlan(merchantId, productId, planPaypalId));
    }

    @Override
    public String getSubscriptionPlanId(Long merchantId, Long productId) {
        return subscriptionPlanRepository.findByMerchantIdAndProductId(merchantId, productId).getPlanPaypalId();
    }
}
