package com.teste.autoflex.thales.exceptions;

public class InsufficentStockQuantityException extends RuntimeException {
    public InsufficentStockQuantityException(String message) {
        super(message);
    }
}
