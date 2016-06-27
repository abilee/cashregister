package com.cashregister.userinterface;

import com.cashregister.controller.CashRegisterController;
import com.cashregister.exceptions.InvalidInputException;
import com.cashregister.exceptions.ValidatorException;
import com.cashregister.model.Money;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * POSEditor loads initializes the cash register
 */
public class POSEditor implements Editor {
    CashRegisterController controller;

    public POSEditor() {
        controller = new CashRegisterController();
        try {
            init();
        } catch (InvalidInputException e) {
            //Log Severe error and terminate the process as initialization failed
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void init() throws InvalidInputException {
        TreeMap<Money, Integer> input = new TreeMap<Money, Integer>();
        input.put(Money.twenty(), 1);
        input.put(Money.ten(), 2);
        input.put(Money.five(), 3);
        input.put(Money.two(), 4);
        input.put(Money.one(), 5);
        controller.put(input);
    }

    @Override
    public String put(NavigableMap<Money, Integer> input) throws InvalidInputException, ValidatorException {
        controller.put(input);
        return controller.show();
    }

    @Override
    public String take(NavigableMap<Money, Integer> input) throws InvalidInputException, ValidatorException {
        controller.take(input);
        return controller.show();
    }

    @Override
    public String show() {
        return controller.show();
    }

    @Override
    public String change(int input) throws InvalidInputException, ValidatorException {
        return controller.change(input);
    }

}
