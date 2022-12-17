package com.bank.service.impl;

import com.bank.dto.*;
import com.bank.feign.BankServiceFeignClient;
import com.bank.feign.PccFeignClient;
import com.bank.model.Account;
import com.bank.model.CreditCard;
import com.bank.model.Payment;
import com.bank.repository.AccountRepository;
import com.bank.repository.CreditCardRepository;
import com.bank.repository.PaymentRepository;
import com.bank.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.bank.exception.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Service
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

    @Value("${PCC_URI}")
    private String pccUri = "";

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
            throw new MerchantNotFoundException("Merchant with id " + paymentStartRequest.merchantId + " not found.");
        }
        var payment = new Payment(merchant.getCreditCard().pan, paymentStartRequest.amount, generateId());
        paymentRepository.save(payment);
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
        var buyerCreditCard = creditCardRepository.findByPan(paymentExecutionRequest.pan);
        if (buyerCreditCard == null) {
            return sendPccRequest(paymentExecutionRequest);
        }
        var payment = paymentRepository.findByPaymentId(paymentExecutionRequest.paymentId);
        if (payment == null) {
            throw new PaymentNotFoundException("Payment with id " + paymentExecutionRequest.paymentId + " not found.");
        }
        var merchantCreditCard = creditCardRepository.findByPan(payment.getMerchantPan());
        if (merchantCreditCard == null) {
            throw new CreditCardNotFoundException("Credit card with provided pan not found.");
        }
        if (!validateCreditCard(paymentExecutionRequest, buyerCreditCard)) {
            throw new CreditCardNotValidException("Credit card with provided pan not valid.");
        }
        if (buyerCreditCard.amountOfMoney < payment.getAmount())
            return new PaymentExecutionResponse(payment.getFailedUrl());
        payment.setBuyerPan(buyerCreditCard.pan);
        paymentRepository.save(payment);

        buyerCreditCard.amountOfMoney -= payment.getAmount();
        merchantCreditCard.amountOfMoney += payment.getAmount();
        creditCardRepository.save(buyerCreditCard);
        creditCardRepository.save(merchantCreditCard);
        bankServiceFeignClient.updatePayment(new ProcessedPaymentRequest(payment.getPaymentId(),
                generateId(), new Date()));
        return new PaymentExecutionResponse(payment.getSuccessUrl());
    }

    @Override
    public PaymentInfoResponse getPaymentInfo(Long id) {
        var payment = paymentRepository.findByPaymentId(id);
        if (payment == null) {
            throw new PaymentNotFoundException("Payment with id " + id + " not found.");
        }
        var creditCard = creditCardRepository.findByPan(payment.getBuyerPan());
        if (creditCard == null) {
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
            throw new PaymentNotFoundException("Payment with id" + paymentExecutionRequest.paymentId + " not found.");
        }
        CreditCard merchantCreditCard = creditCardRepository.findByPan(payment.getMerchantPan());
        if(merchantCreditCard == null){
            throw new CreditCardNotFoundException("Credit card is not found.");
        }
        payment.setBuyerPan(paymentExecutionRequest.pan);
        paymentRepository.save(payment);
        PccRequest pccRequest =  new PccRequest(paymentExecutionRequest.pan, paymentExecutionRequest.securityCode,
                paymentExecutionRequest.cardHolderName, paymentExecutionRequest.validThru, payment.getPaymentId(),
                generateId(), new Date(), payment.getAmount());
        System.out.println(pccRequest);
        //Pozvati pcc koji redirektuje na banku kupca, gdje se vrsi skidanje iznosa sa racuna kupca(processPccRequest-metoda)
        //tek nakon sto se to uspjesno zavrsi, vracam se na banku prodavca i dodajem novac na njegov racun
        PccResponse result = pccFeignClient.payment(pccRequest);
        try {
            pccFeignClient.sendPaymentResult(result);
        } catch (Exception e) {
            System.out.println("PCC nije uspio da posalje rezultat");
            e.printStackTrace();
            return new PaymentExecutionResponse(payment.getErrorUrl());
        }
        return new PaymentExecutionResponse(payment.getSuccessUrl());
    }

    @Override
    public void paymentResult(PccResponse pccResponse) {
        Payment payment = paymentRepository.findByPaymentId(pccResponse.paymentId);
        if(payment == null){
            throw new PaymentNotFoundException("Payment with id" + pccResponse.paymentId + " not found.");
        }
        CreditCard merchantCreditCard = creditCardRepository.findByPan(payment.getMerchantPan());
        if(merchantCreditCard == null){
            throw new CreditCardNotFoundException("Credit card is not found.");
        }
        //if (!pccResponse.isAuthenticationSuccessful || !pccResponse.isAuthorizationSuccessful) return;
        merchantCreditCard.amountOfMoney += payment.getAmount();
        creditCardRepository.save(merchantCreditCard);
        bankServiceFeignClient.updatePayment(new ProcessedPaymentRequest(payment.getPaymentId(),
                generateId(), new Date()));
    }

    @Override
    public PccResponse processPccRequest(PccRequest pccRequest) {
        Payment payment = new Payment(pccRequest.paymentId, pccRequest.amount, pccRequest.pan);
        paymentRepository.save(payment);

        PccResponse pccResponse = new PccResponse(payment.getPaymentId(), true, true,
                pccRequest.acquirerOrderId, pccRequest.acquirerTimestamp, generateId(),
                new Date());
        CreditCard buyerCreditCard = creditCardRepository.findByPan(pccRequest.pan);
        if (buyerCreditCard == null || buyerCreditCard.amountOfMoney < payment.getAmount()) {
            pccResponse.isAuthenticationSuccessful = false;
            pccResponse.isAuthorizationSuccessful = false;
            return pccResponse;
        }
        buyerCreditCard.amountOfMoney -= payment.getAmount();
        creditCardRepository.save(buyerCreditCard);
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
        var isSecurityCodeValid = paymentExecutionRequest.securityCode.equals(creditCard.securityCode);
        var isCardExpired = creditCard.validThru.before(new Date());
        return isSecurityCodeValid && !isCardExpired;
    }

}
