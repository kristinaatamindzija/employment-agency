package com.example.paypalservice.service.implementations;

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
    public void createTransaction(StatusTransaction statusTransaction, Date timestamp) {
        transactionRepository.save(new Transaction(statusTransaction, timestamp));
    }

    @Override
    public void updateTransaction(Long id, StatusTransaction statusTransaction, Date timestamp) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        assert transaction != null;
        transaction.setStatus(statusTransaction);
        transaction.setTimestamp(timestamp);
        transactionRepository.save(transaction);
    }
}
