package com.example.paypalservice.service;

public interface SubscriptionPlanService {
    void createSubscriptionPlan(String merchantUuid, Long productId, String planPaypalId);
    String getSubscriptionPlanId(String merchantUuid, Long productId);
}
