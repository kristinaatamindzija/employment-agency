package com.pcc.service;

import com.pcc.dto.PaymentRequest;
import com.pcc.dto.PaymentResponse;

import com.pcc.feign.AcquirerBankFeignClient;
import com.pcc.feign.IssuerBankFeignClient;
import com.pcc.security.Encryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService{
    private final AcquirerBankFeignClient acquirerBankFeignClient;

    private final IssuerBankFeignClient issuerBankFeignClient;

    @Autowired
    public PaymentServiceImpl(IssuerBankFeignClient issuerBankFeignClient,
                              AcquirerBankFeignClient acquirerBankFeignClient) {
        this.acquirerBankFeignClient = acquirerBankFeignClient;
        this.issuerBankFeignClient = issuerBankFeignClient;
    }

    @Value("${ACQUIRER_BANK_PAN}")
    private String acquirerBankPan;

    @Override
    public PaymentResponse executePayment(PaymentRequest paymentRequest) {
        PaymentResponse paymentResponse;;
        if(paymentRequest.pan.substring(0, 3).equals(acquirerBankPan)) {
            log.info("Payment request is being sent to acquirer bank with pan prefix: "
                    + Encryptor.encrypt(paymentRequest.pan));
            paymentResponse = acquirerBankFeignClient.payment(paymentRequest);
        }
        else {
            log.info("Payment request is being sent to issuer bank with pan prefix: "
                    + Encryptor.encrypt(paymentRequest.pan));
            paymentResponse = issuerBankFeignClient.payment(paymentRequest);
        }
        return paymentResponse;
    }

    @Override
    public void paymentResult(PaymentResponse paymentResponse) {
        log.info("Payment result is being sent to acquirer bank with payment id: "
               + paymentResponse.paymentId);
        acquirerBankFeignClient.paymentResult(paymentResponse);
    }
}
