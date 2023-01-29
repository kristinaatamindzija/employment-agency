package com.bank.service.impl;

import com.bank.dto.*;
import com.bank.feign.BankServiceFeignClient;
import com.bank.feign.PccFeignClient;
import com.bank.model.Account;
import com.bank.model.CreditCard;
import com.bank.model.Payment;
import com.bank.model.PaymentStatus;
import com.bank.repository.AccountRepository;
import com.bank.repository.CreditCardRepository;
import com.bank.repository.PaymentRepository;
import com.bank.security.Encryptor;
import com.bank.security.Hasher;
import com.bank.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.bank.exception.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final CreditCardRepository creditCardRepository;
    private final AccountRepository accountRepository;

    private final PccFeignClient pccFeignClient;
    private final BankServiceFeignClient bankServiceFeignClient;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, CreditCardRepository creditCardRepository,
                              AccountRepository accountRepository, PccFeignClient pccFeignClient, BankServiceFeignClient bankServiceFeignClient) {
        this.paymentRepository = paymentRepository;
        this.creditCardRepository = creditCardRepository;
        this.accountRepository = accountRepository;
        this.pccFeignClient = pccFeignClient;
        this.bankServiceFeignClient = bankServiceFeignClient;
    }

    @Value("${CREDIT_CARD_INPUT_URI}")
    private String creditCardInputUri = "";

    @Override
    public PaymentStartResponse startPayment(PaymentStartRequest paymentStartRequest) {
        Account merchant = accountRepository.findByMerchantId(paymentStartRequest.merchantId);
        if(merchant == null) {
            throw new MerchantNotFoundException("Merchant with id " + paymentStartRequest.merchantId + " not found.");
        }
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        if(!bCryptPasswordEncoder.matches(paymentStartRequest.merchantPassword, merchant.getPassword())) {
//            throw new MerchantNotFoundException("Merchant with id " + paymentStartRequest.merchantId + " not found.");
//        }
        if(!paymentStartRequest.merchantPassword.equals(merchant.getPassword())) {
            log.error("Merchant with id " + paymentStartRequest.merchantId + " not found.");
            throw new MerchantNotFoundException("Merchant with id " + paymentStartRequest.merchantId + " not found.");
        }
        var payment = new Payment(merchant.getCreditCard().pan, paymentStartRequest.amount, generateId());
        payment.setSuccessUrl(paymentStartRequest.successUrl);
        payment.setFailedUrl(paymentStartRequest.failUrl);
        payment.setErrorUrl(paymentStartRequest.errorUrl);
        paymentRepository.save(payment);
        log.info("Payment with id " + payment.getId() + " started.");
        var paymentStartResponse = new PaymentStartResponse();
        paymentStartResponse.paymentId = payment.getPaymentId();
        var method = "/card/";
        if (paymentStartRequest.qr) {
            method = "/qr/";
        }
        paymentStartResponse.paymentUrl = creditCardInputUri + method + payment.getPaymentId();
        return paymentStartResponse;
    }

    @Override
    public PaymentExecutionResponse executePayment(PaymentExecutionRequest paymentExecutionRequest) {
        var payment = paymentRepository.findByPaymentId(paymentExecutionRequest.paymentId);
        if (payment == null) {
            log.error("Payment with id " + paymentExecutionRequest.paymentId + " not found.");
            throw new PaymentNotFoundException("Payment with id " + paymentExecutionRequest.paymentId + " not found.");
        }
        if (payment.getStatus().equals(PaymentStatus.SUCCESSFUL)) {
            log.error("Payment with id " + paymentExecutionRequest.paymentId + " already completed.");
            throw new PaymentAlreadyCompleted("Payment with id " + paymentExecutionRequest.paymentId + " already completed.");
        }
        String encryptedIssuerPan = Encryptor.encrypt(paymentExecutionRequest.pan);
        String encryptedAcquirerPan = Encryptor.encrypt(payment.getMerchantPan());
        var buyerCreditCard = creditCardRepository.findByPan(Hasher.hashPAN(paymentExecutionRequest.pan));
        if (buyerCreditCard == null) {
            log.info("Buyer credit card with pan " + encryptedIssuerPan + " not found.");
            return sendPccRequest(paymentExecutionRequest);
        }
        var merchantCreditCard = creditCardRepository.findByPan(payment.getMerchantPan());
        if (merchantCreditCard == null) {
            log.error("Merchant credit card with pan " + encryptedAcquirerPan + " not found.");
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new CreditCardNotFoundException("Credit card with provided pan not found.");
        }
        if (!validateCreditCard(paymentExecutionRequest, buyerCreditCard)) {
            log.error("Credit card with pan " + encryptedIssuerPan + " is not valid.");
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new CreditCardNotValidException("Credit card with provided pan not valid.");
        }
        if (buyerCreditCard.amountOfMoney < payment.getAmount()) {
            log.error("Not enough money on credit card with pan " + encryptedIssuerPan + ".");
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            return new PaymentExecutionResponse(payment.getFailedUrl());
        }
        payment.setBuyerPan(buyerCreditCard.pan);
        paymentRepository.save(payment);

        buyerCreditCard.amountOfMoney -= payment.getAmount();
        merchantCreditCard.amountOfMoney += payment.getAmount();
        creditCardRepository.save(buyerCreditCard);
        log.info("Money withdrawn from credit card with pan " + encryptedIssuerPan + ".");
        creditCardRepository.save(merchantCreditCard);
        log.info("Money deposited to credit card with pan " + encryptedAcquirerPan + ".");
        payment.setStatus(PaymentStatus.SUCCESSFUL);
        paymentRepository.save(payment);
        bankServiceFeignClient.updatePayment(new ProcessedPaymentRequest(payment.getPaymentId(),
                generateId(), new Date(), payment.getStatus()));
        log.info("Payment with id " + payment.getId() + " executed.");
        return new PaymentExecutionResponse(payment.getSuccessUrl());
    }

    @Override
    public PaymentInfoResponse getPaymentInfo(Long id) {
        var payment = paymentRepository.findByPaymentId(id);
        if (payment == null) {
            log.error("Payment with id " + id + " not found.");
            throw new PaymentNotFoundException("Payment with id " + id + " not found.");
        }
        var creditCard = creditCardRepository.findByPan(payment.getBuyerPan());
        if (creditCard == null) {
            log.error("Credit card with pan " + Encryptor.encrypt(payment.getBuyerPan()) + " not found.");
            throw new CreditCardNotFoundException("Credit card with provided pan not found.");
        }
        var account = creditCard.getAccount();
        return new PaymentInfoResponse(account.getName(), account.getSurname(),
                                       creditCard.getPan(), payment.getAmount());
    }


    @Override
    public PaymentExecutionResponse sendPccRequest(PaymentExecutionRequest paymentExecutionRequest) {
        Payment payment = paymentRepository.findByPaymentId(paymentExecutionRequest.paymentId);
        if(payment == null){
            log.error("Payment with id " + paymentExecutionRequest.paymentId + " not found.");
            throw new PaymentNotFoundException("Payment with id" + paymentExecutionRequest.paymentId + " not found.");
        }
        CreditCard merchantCreditCard = creditCardRepository.findByPan(payment.getMerchantPan());
        if(merchantCreditCard == null){
            log.error("Merchant with provided pan not found.");
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new CreditCardNotFoundException("Credit card is not found.");
        }
        payment.setBuyerPan(Hasher.hashPAN(paymentExecutionRequest.pan));
        paymentRepository.save(payment);

        PccRequest pccRequest =  new PccRequest(paymentExecutionRequest.pan, paymentExecutionRequest.securityCode,
                paymentExecutionRequest.cardHolderName, paymentExecutionRequest.validThru, payment.getPaymentId(),
                generateId(), new Date(), payment.getAmount());
        //Pozvati pcc koji redirektuje na banku kupca, gdje se vrsi skidanje iznosa sa racuna kupca(processPccRequest-metoda)
        //tek nakon sto se to uspjesno zavrsi, vracam se na banku prodavca i dodajem novac na njegov racun
        PccResponse result = pccFeignClient.payment(pccRequest);
        try {
            pccFeignClient.sendPaymentResult(result);
            log.info("Result of payment with id " + payment.getPaymentId() + " sent to pcc.");
        } catch (Exception e) {
            log.error("Error while sending payment result to pcc.");
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            return new PaymentExecutionResponse(payment.getErrorUrl());
        }
        return new PaymentExecutionResponse(payment.getSuccessUrl());
    }

    @Override
    public void paymentResult(PccResponse pccResponse) {
        Payment payment = paymentRepository.findByPaymentId(pccResponse.paymentId);
        if(payment == null){
            log.error("Payment with id " + pccResponse.paymentId + " not found.");
            throw new PaymentNotFoundException("Payment with id" + pccResponse.paymentId + " not found.");
        }
        CreditCard merchantCreditCard = creditCardRepository.findByPan(payment.getMerchantPan());
        if(merchantCreditCard == null){
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            log.error("Merchant with provided pan not found.");
            throw new CreditCardNotFoundException("Credit card is not found.");
        }
        //if (!pccResponse.isAuthenticationSuccessful || !pccResponse.isAuthorizationSuccessful) return;
        merchantCreditCard.amountOfMoney += payment.getAmount();
        creditCardRepository.save(merchantCreditCard);
        log.info("Money deposited to credit card with pan " + Encryptor.encrypt(merchantCreditCard.pan) + ".");
        payment.setStatus(PaymentStatus.SUCCESSFUL);
        paymentRepository.save(payment);
        bankServiceFeignClient.updatePayment(new ProcessedPaymentRequest(payment.getPaymentId(),
                generateId(), new Date(), payment.getStatus()));
        log.info("Update request for payment with id " + payment.getPaymentId() + " sent to bank service.");
    }

    @Override
    public PccResponse processPccRequest(PccRequest pccRequest) {
        Payment payment = new Payment(pccRequest.paymentId, pccRequest.amount, pccRequest.pan);
        paymentRepository.save(payment);
        log.info("Payment with id " + payment.getId() + " created.");
        PccResponse pccResponse = new PccResponse(payment.getPaymentId(), true, true,
                pccRequest.acquirerOrderId, pccRequest.acquirerTimestamp, generateId(),
                new Date());
        CreditCard buyerCreditCard = creditCardRepository.findByPan(Hasher.hashPAN(pccRequest.pan));
        if (buyerCreditCard == null || buyerCreditCard.amountOfMoney < payment.getAmount()) {
            log.error("Credit card with provided pan " + Encryptor.encrypt(pccRequest.pan) + " not found or not enough money to withdraw.");
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            pccResponse.isAuthenticationSuccessful = false;
            pccResponse.isAuthorizationSuccessful = false;
            return pccResponse;
        }
        buyerCreditCard.amountOfMoney -= payment.getAmount();
        creditCardRepository.save(buyerCreditCard);
        log.info("Money withdrawn from credit card with pan " + Encryptor.encrypt(buyerCreditCard.pan) + ".");
        return pccResponse;
    }

    private Long generateId() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder paymentId = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            paymentId.append(random.nextInt(10));
        }
        return Long.parseLong(paymentId.toString());
    }

    private Boolean validateCreditCard(PaymentExecutionRequest paymentExecutionRequest, CreditCard creditCard) {
        var isSecurityCodeValid = Hasher.hashPAN(paymentExecutionRequest.securityCode).equals(creditCard.securityCode);
        var isCardExpired = creditCard.validThru.before(new Date());
        return isSecurityCodeValid && !isCardExpired;
    }

}
