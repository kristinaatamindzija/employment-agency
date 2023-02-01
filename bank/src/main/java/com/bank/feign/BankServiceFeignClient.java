package com.bank.feign;

import com.bank.dto.PccRequest;
import com.bank.dto.PccResponse;
import com.bank.dto.ProcessedPaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "bank-service", url = "https://localhost:7700/bank-service/payment")
public interface BankServiceFeignClient {
    @PostMapping("/process-payment")
    void updatePayment(ProcessedPaymentRequest processedPaymentRequest);
}
