package com.bankservice.feign;

import com.bankservice.config.CustomFeignConfiguration;
import com.bankservice.dto.BankCredentials;
import com.bankservice.dto.StartPaymentResponse;
import com.bankservice.dto.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "bank", url = "https://localhost:8001/payment", configuration = CustomFeignConfiguration.class)
public interface BankFeignClient {
    @PostMapping("")
    StartPaymentResponse startPayment(@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                                      PaymentRequest paymentStartRequest);

    @GetMapping("/bankCredentials/{merchantUuid}")
    BankCredentials getBankCredentials(@PathVariable String merchantUuid);
}
