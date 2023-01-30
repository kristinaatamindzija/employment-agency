package com.example.paypalservice.controller;

import com.example.paypalservice.dto.MerchantResponseDTO;
import com.example.paypalservice.dto.TransactionRequestDTO;
import com.example.paypalservice.model.StatusTransaction;
import com.example.paypalservice.model.Transaction;
import com.example.paypalservice.service.PaypalService;
import com.example.paypalservice.service.SubscriptionPlanService;
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
    private final SubscriptionPlanService subscriptionPlanService;

    public PaypalController(PaypalService paypalService, TransactionService transactionService, SubscriptionPlanService subscriptionPlanService) {
        this.paypalService = paypalService;
        this.transactionService = transactionService;
        this.subscriptionPlanService = subscriptionPlanService;
    }

    @GetMapping("/merchant/{merchantUuid}")
    public ResponseEntity<MerchantResponseDTO> getMerchant(@PathVariable String merchantUuid) {
        return ResponseEntity.ok(paypalService.getMerchant(merchantUuid));
    }

    @PostMapping("/transaction")
    public void createTransaction(@RequestBody TransactionRequestDTO transaction) {
        transactionService.createTransaction(transaction);
    }

    @PutMapping("/transaction")
    public String updateTransaction(@RequestBody TransactionRequestDTO transaction) {
        return transactionService.updateTransaction(transaction);
    }

    @PostMapping("/plan/{merchantUuid}/{productId}/{planPaypalId}")
    public void createSubscriptionPlan(@PathVariable String merchantUuid, @PathVariable Long productId, @PathVariable String planPaypalId) {
        subscriptionPlanService.createSubscriptionPlan(merchantUuid, productId, planPaypalId);
    }

    @GetMapping("/plan/{merchantUuid}/{productId}")
    public String getSubscriptionPlanId(@PathVariable String merchantUuid, @PathVariable Long productId) {
        return subscriptionPlanService.getSubscriptionPlanId(merchantUuid, productId);
    }

}
