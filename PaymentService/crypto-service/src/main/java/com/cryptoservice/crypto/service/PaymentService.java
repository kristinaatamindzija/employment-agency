package com.cryptoservice.crypto.service;

import com.cryptoservice.crypto.dto.CryptoPaymentDTO;
import com.cryptoservice.crypto.model.Payment;
import org.springframework.util.MultiValueMap;

public interface PaymentService {
    String createPayment(CryptoPaymentDTO paymentDto);
    void updatePayment(MultiValueMap<String, String> body);
}
