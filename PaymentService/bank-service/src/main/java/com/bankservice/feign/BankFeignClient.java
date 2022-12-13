package com.bankservice.feign;

import com.bankservice.dto.StartPaymentResponse;
import com.bankservice.dto.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "bank", url = "http://localhost:8001/payment")
public interface BankFeignClient {
    @PostMapping("")
    StartPaymentResponse startPayment(PaymentRequest paymentStartRequest);
}
