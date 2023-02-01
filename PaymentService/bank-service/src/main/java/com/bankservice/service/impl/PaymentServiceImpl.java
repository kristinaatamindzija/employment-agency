package com.bankservice.service.impl;

import com.bankservice.dto.*;
import com.bankservice.feign.AuthServiceFeignClient;
import com.bankservice.feign.BankFeignClient;
import com.bankservice.model.Payment;
import com.bankservice.repository.PaymentRepository;
import com.bankservice.service.PaymentService;
import com.bankservice.utils.TokenUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final AuthServiceFeignClient authServiceFeignClient;

    private final BankFeignClient bankFeignClient;

    private final TokenUtils tokenUtils;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, AuthServiceFeignClient authServiceFeignClient, BankFeignClient bankFeignClient, TokenUtils tokenUtils) {
        this.paymentRepository = paymentRepository;
        this.authServiceFeignClient = authServiceFeignClient;
        this.bankFeignClient = bankFeignClient;
        this.tokenUtils = tokenUtils;
    }

    @Override
    public StartPaymentResponse startPayment(String token, StartPaymentRequest startPaymentRequest) {
        Payment payment = new Payment(startPaymentRequest.merchantUuid, startPaymentRequest.merchantOrderId, startPaymentRequest.amount);
        paymentRepository.save(payment);
        log.info("Payment started | Merchant UUID: {}, Merchant Order ID: {}", payment.merchantUuid, payment.merchantOrderId);
        AuthServiceResponse authServiceResponse = authServiceFeignClient.getMerchantData(payment.merchantUuid);
        log.info("Merchant with id {} found on Auth Service", authServiceResponse.merchantId);
        PaymentRequest paymentStartRequest = new PaymentRequest(authServiceResponse.merchantId,
                authServiceResponse.merchantPassword, payment.getAmount(), payment.merchantOrderId, new Date(),
                authServiceResponse.successUrl + "/" + payment.merchantOrderId,
                authServiceResponse.failUrl  + "/" +  payment.merchantOrderId,
                authServiceResponse.errorUrl + "/" + payment.merchantOrderId, startPaymentRequest.qr);
        StartPaymentResponse paymentResponse = bankFeignClient.startPayment(token, paymentStartRequest);
        log.info("Payment response received from Bank Service | Payment ID: {}, Payment URL: {}",
                paymentResponse.paymentId, paymentResponse.paymentUrl);
        payment.setPaymentId(paymentResponse.paymentId);
        paymentRepository.save(payment);
        log.info("Payment with database ID {} updated with generated PaymentID {}",
                payment.getId(), payment.getPaymentId());
        if(!paymentStartRequest.qr || token == null) {
            return paymentResponse;
        }
        Map<String, Object> newClaims = new HashMap<>();
        Claims claims = tokenUtils.getAllClaimsFromToken(token.replace("Bearer ", ""));
        for(String key : claims.keySet()) {
            newClaims.put(key, claims.get(key));
        }
        newClaims.put("merchantId", authServiceResponse.merchantId);
        paymentResponse.paymentUrl = paymentResponse.paymentUrl + "?token=" + tokenUtils.generateToken("", newClaims);
        return paymentResponse;
    }

    @Override
    public BankCredentials getBankCredentials(String merchantUuid) {
        AuthServiceResponse authServiceResponse = authServiceFeignClient.getMerchantData(merchantUuid);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<BankCredentials> response = restTemplate.getForEntity(authServiceResponse.bankUrl + "/payment/bankCredentials/" + authServiceResponse.merchantId, BankCredentials.class);
        return response.getBody();
    }

    @Override
    public void updatePaymentData(ProcessedPaymentRequest processedPaymentRequest) {
        Payment payment = paymentRepository.findByPaymentId(processedPaymentRequest.paymentId);
        payment.setAcquirerOrderId(processedPaymentRequest.acquirerOrderId);
        payment.setAcquirerTimestamp(processedPaymentRequest.acquirerOrderTimeStamp);
        payment.setStatus(processedPaymentRequest.status);
        paymentRepository.save(payment);
        log.info("Payment with database ID {} updated with Acquirer Order ID {}, Acquirer Timestamp {} and Status {}",
                payment.getId(), payment.getAcquirerOrderId(), payment.getAcquirerTimestamp(), payment.getStatus());
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
