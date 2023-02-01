package com.example.paypalservice.service.implementations;

import com.example.paypalservice.dto.MerchantDTO;
import com.example.paypalservice.dto.MerchantResponseDTO;
import com.example.paypalservice.model.Merchant;
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
    public MerchantResponseDTO getMerchant(String merchantUuid) {
        return merchantRepository.findByMerchantUuid(merchantUuid).map(merchant -> new MerchantResponseDTO(merchant.getMerchantPaypalId(), merchant.getEmail())).orElse(null);
    }

    @Override
    public void addPayPal(MerchantDTO merchantDTO) {
        Merchant merchant = merchantRepository.findByMerchantUuid(merchantDTO.getMerchantUuid()).orElse(null);
        if(merchant == null) throw new RuntimeException("Merchant not found");
        merchant.setMerchantPaypalId(merchantDTO.getMerchantPaypalId());
        merchant.setEmail(merchantDTO.getEmail());
        merchantRepository.save(merchant);
    }

    @Override
    public void deletePayPal(String merchantUuid) {
        Merchant merchant = merchantRepository.findByMerchantUuid(merchantUuid).orElse(null);
        if(merchant == null) throw new RuntimeException("Merchant not found");
        merchant.setMerchantPaypalId(null);
        merchant.setEmail(null);
        merchantRepository.save(merchant);
    }
}
