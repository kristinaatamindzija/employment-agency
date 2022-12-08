package com.authservice.service;

import com.authservice.model.PaymentMethod;

public interface PaymentMethodService {
    PaymentMethod findById(Long id);
}
