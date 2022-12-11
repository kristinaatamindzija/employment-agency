package com.authservice.service.imp;

import com.authservice.model.MerchantPaymentMethod;
import com.authservice.repository.MerchantPaymentMethodRepository;
import com.authservice.service.MerchantPaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantPaymentMethodServiceImpl implements MerchantPaymentMethodService {

    @Autowired
    private MerchantPaymentMethodRepository merchantPaymentMethodRepository;

    @Override
    public MerchantPaymentMethod save(MerchantPaymentMethod merchantPaymentMethod) {
        return merchantPaymentMethodRepository.save(merchantPaymentMethod);
    }

    @Override
    public void delete(MerchantPaymentMethod merchantPaymentMethod) {
        merchantPaymentMethodRepository.delete(merchantPaymentMethod);
    }
}
