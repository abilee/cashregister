package com.cashregister.userinterface;

import com.cashregister.exceptions.InvalidInputException;
import com.cashregister.exceptions.ValidatorException;
import com.cashregister.model.Money;

import java.util.NavigableMap;

/**
 */
public interface Editor {
    public String put(NavigableMap<Money, Integer> input) throws InvalidInputException, ValidatorException;
    public String take(NavigableMap<Money, Integer> input) throws InvalidInputException, ValidatorException;
    public String show() throws ValidatorException;
    public String change(int input) throws InvalidInputException, ValidatorException;
}
