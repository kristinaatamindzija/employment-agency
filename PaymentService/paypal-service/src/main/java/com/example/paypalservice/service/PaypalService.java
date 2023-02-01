package com.example.paypalservice.service;

import com.example.paypalservice.dto.MerchantDTO;
import com.example.paypalservice.dto.MerchantResponseDTO;

public interface PaypalService {
    MerchantResponseDTO getMerchant(String merchantUuid);
    void addPayPal(MerchantDTO merchantDTO);
    void deletePayPal(String merchantUuid);
}
