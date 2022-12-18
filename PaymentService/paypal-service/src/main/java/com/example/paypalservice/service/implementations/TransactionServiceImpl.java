package com.example.paypalservice.service.implementations;

import com.example.paypalservice.dto.TransactionRequestDTO;
import com.example.paypalservice.model.StatusTransaction;
import com.example.paypalservice.model.Transaction;
import com.example.paypalservice.repository.TransactionRepository;
import com.example.paypalservice.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void createTransaction(TransactionRequestDTO transactionDto) {
        Transaction transaction = new Transaction(transactionDto.getStatus(), transactionDto.getTimestamp(),
                transactionDto.getMerchantUuid(), transactionDto.getProductUuid(), transactionDto.getPayerId());
        transactionRepository.save(transaction);
    }

    @Override
    public void updateTransaction(TransactionRequestDTO transactionDto) {
        Transaction transaction = transactionRepository.findByMerchantUuidAndProductUuid(transactionDto.getMerchantUuid(), transactionDto.getProductUuid());
        assert transaction != null;
        transaction.setStatus(transactionDto.getStatus());
        transaction.setTimestamp(transactionDto.getTimestamp());
        transactionRepository.save(transaction);
    }
}
