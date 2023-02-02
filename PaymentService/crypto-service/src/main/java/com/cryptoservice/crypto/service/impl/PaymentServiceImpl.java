package com.cryptoservice.crypto.service.impl;

import com.cryptoservice.crypto.dto.CryptoPaymentDTO;
import com.cryptoservice.crypto.model.Payment;
import com.cryptoservice.crypto.model.PaymentStatus;
import com.cryptoservice.crypto.repository.PaymentRepository;
import com.cryptoservice.crypto.security.Encryptor;
import com.cryptoservice.crypto.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }

    @Override
    public String createPayment(CryptoPaymentDTO payment) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(payment.bitcoinApiToken);
        log.info("Creating payment with ID {} for merchant with API token {}",payment.orderId,
                Encryptor.encrypt(payment.bitcoinApiToken));
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("title", payment.title);
        map.add("price_amount", payment.priceAmount);
        map.add("price_currency", payment.priceCurrency);
        map.add("receive_currency", payment.receiveCurrency);
        map.add("callback_url", payment.callbackUrl);
        map.add("success_url", payment.successUrl);
        map.add("cancel_url", payment.cancelUrl);
        map.add("order_id", payment.orderId);
        map.add("description", payment.description);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api-sandbox.coingate.com/api/v2/orders", request, String.class);
        ObjectMapper mapper = new ObjectMapper();
        Payment newPayment = new Payment();
        try {
            JsonNode root = mapper.readTree(response.getBody());
            newPayment.setId(root.get("id").asText());
            newPayment.setOrderId(root.get("order_id").asText());
            newPayment.setStatus(PaymentStatus.valueOf(root.get("status").asText().toUpperCase()));
            newPayment.setPriceAmount(root.get("price_amount").asDouble());
            newPayment.setPriceCurrency(root.get("price_currency").asText());
            newPayment.setReceiveCurrency(root.get("receive_currency").asText());
            newPayment.setReceiveAmount(root.get("receive_amount").asDouble());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
            newPayment.setCreatedAt(LocalDateTime.parse(root.get("created_at").asText(), formatter));
            newPayment.setMerchantUuid(payment.merchantUuid);
        } catch (JsonProcessingException e) {
            log.error("Error while parsing JSON response from CoinGate API");
            throw new RuntimeException(e);
        }
        paymentRepository.save(newPayment);
        log.info("Payment with ID {} created",newPayment.getId());
        return response.getBody();
    }

    @Override
    public void updatePayment(MultiValueMap<String, String> body) {
        Map<String, String> paymentMap = body.toSingleValueMap();
        Payment payment = paymentRepository.findById(paymentMap.get("id")).orElse(null);
        if(payment == null){
            log.error("Payment with ID {} not found",paymentMap.get("id"));
            throw new RuntimeException("Payment not found");
        }
        payment.setStatus(PaymentStatus.valueOf(paymentMap.get("status").toUpperCase()));
        payment.setPayCurrency(paymentMap.get("pay_currency"));
        payment.setPayAmount(Double.valueOf(paymentMap.get("pay_amount")));
        payment.setReceiveCurrency(paymentMap.get("receive_currency"));
        payment.setReceiveAmount(Double.valueOf(paymentMap.get("receive_amount")));

        paymentRepository.save(payment);
        log.info("Payment with ID {} updated with status {}",payment.getId(), payment.getStatus());
    }
}
