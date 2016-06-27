package com.cashregister.controller;

import com.cashregister.exceptions.InvalidInputException;
import com.cashregister.model.Money;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class CashRegisterControllerTest {

    CashRegisterController controller;

    @Before
    public void setUp() {
        controller = new CashRegisterController();
    }

    @Test
    public void testPut() throws InvalidInputException {
        TreeMap<Money, Integer> input = new TreeMap<Money, Integer>();
        input.put(Money.twenty(), 1);
        input.put(Money.ten(), 2);
        input.put(Money.five(), 3);
        input.put(Money.two(), 4);
        input.put(Money.one(), 5);
        controller.put(input);
        String output = controller.show();
        assertEquals("68 1 2 3 4 5", output);

        TreeMap<Money, Integer> inputSecond = new TreeMap<Money, Integer>();
        inputSecond.put(Money.twenty(), 1);
        inputSecond.put(Money.ten(), 2);
        inputSecond.put(Money.five(), 3);
        inputSecond.put(Money.two(), 0);
        inputSecond.put(Money.one(), 5);
        controller.put(inputSecond);
        String outputSecond = controller.show();
        assertEquals("128 2 4 6 4 10", outputSecond);

        TreeMap<Money, Integer> inputThird = new TreeMap<Money, Integer>();
        inputThird.put(Money.twenty(), 1);
        inputThird.put(Money.ten(), 4);
        inputThird.put(Money.five(), 3);
        inputThird.put(Money.two(), 0);
        inputThird.put(Money.one(), 10);

        controller.take(inputThird);
        String outputTake = controller.show();
        assertEquals("43 1 0 3 4 0", outputTake);

        String changeResult = controller.change(11);
        assertEquals("0 0 1 3 0", changeResult);

        String outputChange = controller.show();
        assertEquals("32 1 0 2 1 0", outputChange);

    }

    @Test(expected=InvalidInputException.class)
    public void testChangeException() throws InvalidInputException {
        TreeMap<Money, Integer> inputThird = new TreeMap<Money, Integer>();
        inputThird.put(Money.twenty(), 1);
        inputThird.put(Money.ten(), 0);
        inputThird.put(Money.five(), 2);
        inputThird.put(Money.two(), 1);
        inputThird.put(Money.one(), 0);
        try {
            controller.put(inputThird);
        } catch (InvalidInputException e) {
        }
        controller.change(14);
    }

    @Test
    public void testChangeMoneyThirtyOne() throws InvalidInputException {
        TreeMap<Money, Integer> inputThird = new TreeMap<Money, Integer>();
        inputThird.put(Money.twenty(), 1);
        inputThird.put(Money.ten(), 0);
        inputThird.put(Money.five(), 3);
        inputThird.put(Money.two(), 4);
        inputThird.put(Money.one(), 0);

        controller.put(inputThird);
        String output = controller.change(31);
        assertEquals("1 0 1 3 0", output);
    }

    @Test
    public void testChangeZeroOneTwo() throws InvalidInputException {

        TreeMap<Money, Integer> inputThird = new TreeMap<Money, Integer>();
        inputThird.put(Money.twenty(), 1);
        inputThird.put(Money.ten(), 0);
        inputThird.put(Money.five(), 3);
        inputThird.put(Money.two(), 4);
        inputThird.put(Money.one(), 4);
        controller.put(inputThird);
        String output = controller.change(0);
        assertEquals("0 0 0 0 0", output);

        String output2 = controller.change(1);
        assertEquals("0 0 0 0 1", output2);

        String output3 = controller.change(2);
        assertTrue("0 0 0 1 0".equalsIgnoreCase(output3));
    }



}
