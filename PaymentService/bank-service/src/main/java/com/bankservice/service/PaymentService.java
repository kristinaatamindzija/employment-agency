package com.bankservice.service;

import com.bankservice.dto.BankCredentials;
import com.bankservice.dto.ProcessedPaymentRequest;
import com.bankservice.dto.StartPaymentRequest;
import com.bankservice.dto.StartPaymentResponse;
import com.bankservice.model.Payment;


public interface PaymentService{

    void updatePaymentData(ProcessedPaymentRequest processedPaymentRequest);

    StartPaymentResponse startPayment(StartPaymentRequest request);

    BankCredentials getBankCredentials(String merchantUuid);
}
