package com.cryptoservice.crypto.controller;

import com.cryptoservice.crypto.dto.CryptoPaymentDTO;
import com.cryptoservice.crypto.service.PaymentService;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/crypto", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay")
    public ResponseEntity<?> cryptoPayment(@RequestBody CryptoPaymentDTO payment) {
        return new ResponseEntity<>(paymentService.createPayment(payment), HttpStatus.OK);
    }

    @PostMapping(value = "/update-payment", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void updatePayment(@RequestBody MultiValueMap<String, String> body){
        paymentService.updatePayment(body);
    }
}
