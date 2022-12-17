package com.example.paypalservice.service.implementations;

import com.example.paypalservice.dto.MerchantResponseDTO;
import com.example.paypalservice.repository.MerchantRepository;
import com.example.paypalservice.service.PaypalService;
import org.springframework.stereotype.Service;

@Service
public class PaypalServiceImpl implements PaypalService {

    private final MerchantRepository merchantRepository;

    public PaypalServiceImpl(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    @Override
    public MerchantResponseDTO getMerchant(Long merchantId) {
        return merchantRepository.findById(merchantId).map(merchant -> new MerchantResponseDTO(merchant.getMerchantPaypalId(), merchant.getEmail())).orElse(null);
    }
}
