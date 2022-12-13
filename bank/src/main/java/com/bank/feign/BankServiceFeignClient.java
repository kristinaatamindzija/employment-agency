package com.bank.feign;

import com.bank.dto.PccRequest;
import com.bank.dto.PccResponse;
import com.bank.dto.ProcessedPaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "bank-service", url = "http://localhost:7703/bank-service/payment")
public interface BankServiceFeignClient {
    @PostMapping("/process-payment")
    void updatePayment(ProcessedPaymentRequest processedPaymentRequest);
}
