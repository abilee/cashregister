package com.cashregister.userinterface;


import com.cashregister.exceptions.ValidatorException;
import com.cashregister.model.Money;

import java.util.NavigableMap;
import java.util.TreeMap;

public abstract class BaseParser implements InputValidator {

    public NavigableMap<Money, Integer> parseInput(String input) throws ValidatorException {
        validate(input);
        String[] inputVals = input.trim().split(" ");
        NavigableMap<Money, Integer> denominations = new TreeMap<>();
        try {
            denominations.put(Money.twenty(), Integer.parseInt(inputVals[1]));
            denominations.put(Money.ten(), Integer.parseInt(inputVals[2]));
            denominations.put(Money.five(), Integer.parseInt(inputVals[3]));
            denominations.put(Money.two(), Integer.parseInt(inputVals[4]));
            denominations.put(Money.one(), Integer.parseInt(inputVals[5]));
        } catch (NumberFormatException e) {
            throw new ValidatorException("Incorrect format: " + input + " ; Correct format : take|put num20 num10 num5 num2 num1");
        }
        return denominations;
    }

}
