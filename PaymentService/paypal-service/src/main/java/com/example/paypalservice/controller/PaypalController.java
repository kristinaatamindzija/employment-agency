package com.example.paypalservice.controller;

import com.example.paypalservice.dto.MerchantResponseDTO;
import com.example.paypalservice.model.StatusTransaction;
import com.example.paypalservice.service.PaypalService;
import com.example.paypalservice.service.TransactionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(value = "/paypal", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaypalController {

    private final PaypalService paypalService;
    private final TransactionService transactionService;

    public PaypalController(PaypalService paypalService, TransactionService transactionService) {
        this.paypalService = paypalService;
        this.transactionService = transactionService;
    }

    @GetMapping("/merchant/{merchantId}")
    public ResponseEntity<MerchantResponseDTO> getMerchant(@PathVariable Long merchantId) {
        return ResponseEntity.ok(paypalService.getMerchant(merchantId));
    }

    @PostMapping("/transaction/{status}/{timestamp}")
    public void createTransaction(@PathVariable StatusTransaction status, @PathVariable Date timestamp) {
        transactionService.createTransaction(status, timestamp);
    }

    @PutMapping("/transaction/{id}/{status}/{timestamp}")
    public void updateTransaction(@PathVariable Long id, @PathVariable StatusTransaction status, @PathVariable Date timestamp) {
        transactionService.updateTransaction(id, status, timestamp);
    }

}
