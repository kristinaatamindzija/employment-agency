package com.bankservice.controller;

import com.bankservice.dto.*;
import com.bankservice.model.Payment;
import com.bankservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(value = "/bank-service/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("")
    public ResponseEntity<StartPaymentResponse> startPayment(@RequestBody StartPaymentRequest request) {
        return ResponseEntity.ok(paymentService.startPayment(request));
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
