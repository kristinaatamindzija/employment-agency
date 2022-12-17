package com.bankservice.controller;

import com.bankservice.dto.PaymentRequest;
import com.bankservice.dto.ProcessedPaymentRequest;
import com.bankservice.dto.StartPaymentRequest;
import com.bankservice.dto.StartPaymentResponse;
import com.bankservice.model.Payment;
import com.bankservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
//        val result = paymentService.processPayment(map(request), request.qr)
//        ResponseEntity(map(result), HttpStatus.OK)
//    } catch (e: MerchantAccountDoesNotExistException) {
//        pspLogger.log(e.message, level = LogLevel.ERROR)
//        ResponseEntity(HttpStatus.BAD_REQUEST)
//    } catch (e: PaymentInvalidException) {
//        pspLogger.log("From ${request.merchantUuid} " + e.message, level = LogLevel.ERROR)
//        ResponseEntity(HttpStatus.BAD_REQUEST)
//    } catch (e: Exception) {
//        pspLogger.logException(e)
//        ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
//    }
    }

    @PostMapping("/process-payment")
    public void processPayment(@RequestBody ProcessedPaymentRequest processedPaymentRequest) {
        paymentService.updatePaymentData(processedPaymentRequest);
    }

}
