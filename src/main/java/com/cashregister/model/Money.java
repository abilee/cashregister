package com.cashregister.model;

import java.util.Arrays;
import java.util.List;

/**
 * Money denomations - 20, 10, 5, 2, 1
 */
public class Money implements Comparable {

    public static Money twenty() {
        return new Money(20);
    }

    public static Money ten() {
        return new Money(10);
    }
    public static Money five() {
        return new Money(5);
    }
    public static Money two() {
        return new Money(2);
    }
    public static Money one() {
        return new Money(1);
    }
    private Money(int value) {
        this.value = value;
    }
    private int value;

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Object o) {
        return ((Money) o).getValue() - this.getValue();
    }

    public static List<Money> values() {
        return Arrays.asList(twenty(), ten(), five(), two(), one());
    }
}
