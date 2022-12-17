package com.example.paypalservice.service;

import com.example.paypalservice.model.StatusTransaction;

import java.util.Date;

public interface TransactionService {
    void createTransaction(StatusTransaction statusTransaction, Date timestamp);
    void updateTransaction(Long id, StatusTransaction statusTransaction, Date timestamp);
}
