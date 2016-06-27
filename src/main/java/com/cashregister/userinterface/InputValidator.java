package com.cashregister.userinterface;

import com.cashregister.exceptions.ValidatorException;

/**
 * Validate input
 */
public interface InputValidator {
    public void validate(String input) throws ValidatorException;
}
