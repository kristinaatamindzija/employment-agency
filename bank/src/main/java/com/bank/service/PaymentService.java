package com.bank.service;

import com.bank.dto.*;
import org.springframework.http.ResponseEntity;

public interface PaymentService {
    PaymentStartResponse startPayment(PaymentStartRequest paymentStartRequest);
    PaymentExecutionResponse executePayment(PaymentExecutionRequest paymentExecutionRequest);
    PaymentInfoResponse getPaymentInfo(Long id);
    PccResponse processPccRequest(PccRequest pccRequest);
    PaymentExecutionResponse sendPccRequest(PaymentExecutionRequest paymentExecutionRequest);
    void paymentResult(PccResponse pccResponse);
}
