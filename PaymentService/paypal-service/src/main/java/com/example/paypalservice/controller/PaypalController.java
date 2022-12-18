package com.example.paypalservice.controller;

import com.example.paypalservice.dto.MerchantResponseDTO;
import com.example.paypalservice.model.StatusTransaction;
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

    @PostMapping("/plan/{merchantId}/{productId}/{planPaypalId}")
    public void createSubscriptionPlan(@PathVariable Long merchantId, @PathVariable Long productId, @PathVariable String planPaypalId) {
        subscriptionPlanService.createSubscriptionPlan(merchantId, productId, planPaypalId);
    }

    @GetMapping("/plan/{merchantId}/{productId}")
    public String getSubscriptionPlanId(@PathVariable Long merchantId, @PathVariable Long productId) {
        return subscriptionPlanService.getSubscriptionPlanId(merchantId, productId);
    }

}
