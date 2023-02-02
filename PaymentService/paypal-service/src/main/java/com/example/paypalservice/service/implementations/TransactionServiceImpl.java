package com.example.paypalservice.service.implementations;

import com.example.paypalservice.dto.AuthServiceResponse;
import com.example.paypalservice.dto.TransactionRequestDTO;
import com.example.paypalservice.feign.AuthServiceFeignClient;
import com.example.paypalservice.model.Transaction;
import com.example.paypalservice.repository.TransactionRepository;
import com.example.paypalservice.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AuthServiceFeignClient authServiceFeignClient;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AuthServiceFeignClient authServiceFeignClient) {
        this.transactionRepository = transactionRepository;
        this.authServiceFeignClient = authServiceFeignClient;
    }

    @Override
    public void createTransaction(TransactionRequestDTO transactionDto) {
        Transaction transaction = transactionRepository.findByMerchantUuidAndProductUuid(transactionDto.getMerchantUuid(), transactionDto.getProductUuid());
        if(transaction != null) {
            log.info("Transaction with merchant uuid: " + transactionDto.getMerchantUuid() + " and product uuid: " + transactionDto.getProductUuid() + " already exists");
            throw new RuntimeException("Transaction already exists");
        }
        Transaction newTransaction = new Transaction(transactionDto.getStatus(), transactionDto.getTimestamp(),
                transactionDto.getMerchantUuid(), transactionDto.getProductUuid(), transactionDto.getPayerId());
        transactionRepository.save(newTransaction);
        log.info("Transaction with merchant uuid: " + transactionDto.getMerchantUuid() + " and product uuid: " + transactionDto.getProductUuid() + " created");
    }

    @Override
    public String updateTransaction(TransactionRequestDTO transactionDto) {
        Transaction transaction = transactionRepository.findByMerchantUuidAndProductUuid(transactionDto.getMerchantUuid(), transactionDto.getProductUuid());
        assert transaction != null;
        transaction.setStatus(transactionDto.getStatus());
        transaction.setTimestamp(transactionDto.getTimestamp());
        transaction.setPayerId(transactionDto.getPayerId());
        transactionRepository.save(transaction);
        log.info("Transaction with merchant uuid: " + transactionDto.getMerchantUuid() +
                " and product uuid: " + transactionDto.getProductUuid() +
                " updated to status: " + transactionDto.getStatus());

        AuthServiceResponse authServiceResponse = authServiceFeignClient.getMerchantData(transactionDto.getMerchantUuid());
        return authServiceResponse.getSuccessUrl()  + "/" + transactionDto.getProductUuid();
    }
}
