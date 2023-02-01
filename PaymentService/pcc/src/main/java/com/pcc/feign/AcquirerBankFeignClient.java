package com.pcc.feign;

import com.pcc.dto.PaymentRequest;
import com.pcc.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "acquirer-bank", url = "https://localhost:8001/payment")
public interface AcquirerBankFeignClient {
    @PostMapping("/pcc")
    PaymentResponse payment(PaymentRequest pccRequest);

    @PostMapping("/pcc/paymentResult")
    void paymentResult(PaymentResponse paymentResponse);
}
