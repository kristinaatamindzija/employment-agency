package com.bank.controller;


import com.bank.dto.*;
import com.bank.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("")
     public ResponseEntity<PaymentStartResponse> startPaymentProcess(@Valid @RequestBody PaymentStartRequest paymentStartRequest) {
        try {
            return ResponseEntity.ok(paymentService.startPayment(paymentStartRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentInfoResponse> getPaymentInfo(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(paymentService.getPaymentInfo(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/execute")
    public ResponseEntity<PaymentExecutionResponse> executePayment(@Valid @RequestBody PaymentExecutionRequest paymentExecutionRequest) {
        try {
            return ResponseEntity.ok(paymentService.executePayment(paymentExecutionRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/pcc")
    public ResponseEntity<PccResponse> processPccRequest(@RequestBody PccRequest pccRequest) {
        return ResponseEntity.ok(paymentService.processPccRequest(pccRequest));
    }

    @PostMapping("/pcc/paymentResult")
    public ResponseEntity<?> paymentResult(@RequestBody PccResponse pccResponse) {
        paymentService.paymentResult(pccResponse);
        return ResponseEntity.ok(null);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/bankCredentials/{merchantUuid}")
    public ResponseEntity<BankCredentials> getBankCredentials(@PathVariable String merchantUuid) {
        return ResponseEntity.ok(paymentService.getBankCredentials(merchantUuid));
    }


}
