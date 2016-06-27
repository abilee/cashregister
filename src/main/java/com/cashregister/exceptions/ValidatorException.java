package com.cashregister.exceptions;

/**
 * Exception thrown for incorrect user input
 */
public class ValidatorException extends Exception {

    public ValidatorException(String message) {
        super(message);
    }
}
