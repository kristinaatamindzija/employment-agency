package com.example.paypalservice.service;

public interface SubscriptionPlanService {
    void createSubscriptionPlan(Long merchantId, Long productId, String planPaypalId);
    String getSubscriptionPlanId(Long merchantId, Long productId);
}
