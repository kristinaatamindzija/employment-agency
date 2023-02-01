package com.pcc.feign;

import com.pcc.dto.PaymentRequest;
import com.pcc.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "issuer-bank", url = "https://localhost:8002/payment")
public interface IssuerBankFeignClient {
    @PostMapping("/pcc")
    PaymentResponse payment(PaymentRequest pccRequest);
}
