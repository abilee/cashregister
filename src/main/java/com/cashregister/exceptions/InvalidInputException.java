package com.cashregister.exceptions;

/**
 * Exception thrown when cash register doesnt have required denominations
 */
public class InvalidInputException extends Exception {
    public InvalidInputException(String s) {
        super(s);
    }
}
