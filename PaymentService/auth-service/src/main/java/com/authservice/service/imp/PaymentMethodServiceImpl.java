package com.authservice.service.imp;

import com.authservice.model.PaymentMethod;
import com.authservice.repository.PaymentMethodRepository;
import com.authservice.service.PaymentMethodService;
import org.springframework.stereotype.Service;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    public PaymentMethod findById(Long id) {
        return paymentMethodRepository.findById(id).orElse(null);
    }
}
