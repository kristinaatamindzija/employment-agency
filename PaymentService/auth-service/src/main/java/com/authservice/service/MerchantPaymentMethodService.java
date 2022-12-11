package com.authservice.service;

import com.authservice.model.MerchantPaymentMethod;

public interface MerchantPaymentMethodService {
    MerchantPaymentMethod save(MerchantPaymentMethod merchantPaymentMethod);
    void delete(MerchantPaymentMethod merchantPaymentMethod);
}
