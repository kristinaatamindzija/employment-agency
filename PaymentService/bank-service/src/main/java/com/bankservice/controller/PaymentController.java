package com.bankservice.controller;

import com.bankservice.dto.*;
import com.bankservice.feign.BankFeignClient;
import com.bankservice.service.PaymentService;
import com.bankservice.utils.TokenUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/bank-service/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final TokenUtils tokenUtils;
    private final BankFeignClient bankFeignClient;

    @Autowired
    public PaymentController(PaymentService paymentService, TokenUtils tokenUtils, BankFeignClient bankFeignClient) {
        this.paymentService = paymentService;
        this.tokenUtils = tokenUtils;
        this.bankFeignClient = bankFeignClient;
    }

    @PostMapping("")
    public ResponseEntity<StartPaymentResponse> startPayment(@RequestBody StartPaymentRequest request,
                                                             HttpServletRequest httpServletRequest,
                                                             HttpServletResponse httpServletResponse) {
        String token = httpServletRequest.getHeader("Authorization");
        StartPaymentResponse startPaymentResponse = paymentService.startPayment(token, request);
        return ResponseEntity.ok(startPaymentResponse);
    }

    @PostMapping("/process-payment")
    public void processPayment(@RequestBody ProcessedPaymentRequest processedPaymentRequest) {
        paymentService.updatePaymentData(processedPaymentRequest);
    }

    @GetMapping("/bankAccount/{merchantUuid}")
    public ResponseEntity<BankCredentials> getPaymentByBankAccount(@PathVariable String merchantUuid) {
        return ResponseEntity.ok(paymentService.getBankCredentials(merchantUuid));
    }
}
