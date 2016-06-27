package com.cashregister.actions;

import com.cashregister.exceptions.InvalidInputException;
import com.cashregister.exceptions.ValidatorException;
import com.cashregister.userinterface.Editor;

/**
 */
public interface Action {
    public String perform(String input) throws InvalidInputException, ValidatorException;
}
