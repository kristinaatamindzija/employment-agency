package com.example.paypalservice.service.implementations;

import com.example.paypalservice.dto.AuthServiceResponse;
import com.example.paypalservice.dto.TransactionRequestDTO;
import com.example.paypalservice.feign.AuthServiceFeignClient;
import com.example.paypalservice.model.Transaction;
import com.example.paypalservice.repository.TransactionRepository;
import com.example.paypalservice.service.TransactionService;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AuthServiceFeignClient authServiceFeignClient;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AuthServiceFeignClient authServiceFeignClient) {
        this.transactionRepository = transactionRepository;
        this.authServiceFeignClient = authServiceFeignClient;
    }

    @Override
    public void createTransaction(TransactionRequestDTO transactionDto) {
        Transaction transaction = new Transaction(transactionDto.getStatus(), transactionDto.getTimestamp(),
                transactionDto.getMerchantUuid(), transactionDto.getProductUuid(), transactionDto.getPayerId());
        transactionRepository.save(transaction);
    }

    @Override
    public String updateTransaction(TransactionRequestDTO transactionDto) {
        //promeniti ovaj find da bude unikatan. npr neki id
        Transaction transaction = transactionRepository.findByMerchantUuidAndProductUuid(transactionDto.getMerchantUuid(), transactionDto.getProductUuid());
        assert transaction != null;
        transaction.setStatus(transactionDto.getStatus());
        transaction.setTimestamp(transactionDto.getTimestamp());
        transactionRepository.save(transaction);

        AuthServiceResponse authServiceResponse = authServiceFeignClient.getMerchantData(transactionDto.getMerchantUuid());
        return authServiceResponse.getSuccessUrl()  + "/" + transactionDto.getProductUuid();
    }
}
