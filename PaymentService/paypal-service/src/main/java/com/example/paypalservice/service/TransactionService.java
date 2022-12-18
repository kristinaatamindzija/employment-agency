package com.example.paypalservice.service;

import com.example.paypalservice.dto.TransactionRequestDTO;
import com.example.paypalservice.model.StatusTransaction;
import com.example.paypalservice.model.Transaction;

import java.util.Date;

public interface TransactionService {
    void createTransaction(TransactionRequestDTO transaction);
    void updateTransaction(TransactionRequestDTO transaction);
}