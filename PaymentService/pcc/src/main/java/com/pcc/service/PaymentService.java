package com.pcc.service;

import com.pcc.dto.PaymentResponse;
import com.pcc.dto.PaymentRequest;

public interface PaymentService {
    PaymentResponse executePayment(PaymentRequest paymentRequest);
    void paymentResult(PaymentResponse paymentResponse);
}
