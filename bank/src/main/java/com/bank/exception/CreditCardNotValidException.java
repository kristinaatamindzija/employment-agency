package com.bank.exception;

public class CreditCardNotValidException extends RuntimeException {
    public CreditCardNotValidException(String message) {
        super(message);
    }
}
