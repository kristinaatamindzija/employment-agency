package com.bank.feign;

import com.bank.dto.PccRequest;
import com.bank.dto.PccResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "pcc", url = "http://localhost:8008/pcc")
public interface PccFeignClient {
    @PostMapping("")
    PccResponse payment(PccRequest pccRequest);

    @PostMapping("/paymentResult")
    void sendPaymentResult(PccResponse paymentResult);
}
