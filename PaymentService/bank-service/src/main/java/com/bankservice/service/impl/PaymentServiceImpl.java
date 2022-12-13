package com.bankservice.service.impl;

import com.bankservice.dto.*;
import com.bankservice.feign.AuthServiceFeignClient;
import com.bankservice.feign.BankFeignClient;
import com.bankservice.model.Payment;
import com.bankservice.repository.PaymentRepository;
import com.bankservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final AuthServiceFeignClient authServiceFeignClient;

    private final BankFeignClient bankFeignClient;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, AuthServiceFeignClient authServiceFeignClient, BankFeignClient bankFeignClient) {
        this.paymentRepository = paymentRepository;
        this.authServiceFeignClient = authServiceFeignClient;
        this.bankFeignClient = bankFeignClient;
    }

    @Override
    public StartPaymentResponse startPayment(StartPaymentRequest startPaymentRequest) {
        Payment payment = new Payment(startPaymentRequest.merchantUuid, generateMerchantOrderId(), startPaymentRequest.amount);
        paymentRepository.save(payment);
        AuthServiceResponse authServiceResponse = authServiceFeignClient.getMerchantData(payment.merchantUuid);
        PaymentRequest paymentStartRequest = new PaymentRequest(authServiceResponse.merchantId,
                authServiceResponse.merchantPassword, payment.getAmount(), generateMerchantOrderId(), new Date(),
                authServiceResponse.successUrl + "/" + payment.merchantOrderId,
                authServiceResponse.failUrl  + "/" +  payment.merchantOrderId,
                authServiceResponse.failUrl + "/" + payment.merchantOrderId, startPaymentRequest.qr);
        StartPaymentResponse paymentResponse = bankFeignClient.startPayment(paymentStartRequest);
        payment.setPaymentId(paymentResponse.paymentId);
        paymentRepository.save(payment);
        return paymentResponse;
    }

    @Override
    public void updatePaymentData(ProcessedPaymentRequest processedPaymentRequest) {
        Payment payment = paymentRepository.findByPaymentId(processedPaymentRequest.paymentId);
        payment.setAcquirerOrderId(processedPaymentRequest.acquirerOrderId);
        payment.setAcquirerTimestamp(processedPaymentRequest.acquirerOrderTimeStamp);
        paymentRepository.save(payment);
    }

    private String generateMerchantOrderId() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder paymentId = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            paymentId.append(random.nextInt(10));
        }
        return paymentId.toString();
    }


}
