package com.pcc.controller;

import com.pcc.dto.PaymentResponse;
import com.pcc.dto.PaymentRequest;
import com.pcc.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/pcc")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @PostMapping("")
    public ResponseEntity<PaymentResponse> startPaymentProcess(@RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.ok(paymentService.executePayment(paymentRequest));
    }

    @PostMapping("/paymentResult")
    public ResponseEntity<?> paymentResult(@RequestBody PaymentResponse paymentResponse) {
        paymentService.paymentResult(paymentResponse);
        return ResponseEntity.ok(null);
    }
}
