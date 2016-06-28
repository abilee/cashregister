package com.cashregister.controller;

import com.cashregister.exceptions.InvalidInputException;
import com.cashregister.model.Money;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Controller to put, take, change, and show denominations and counts in cash register
 */
public class CashRegisterController {
    private NavigableMap<Money, Integer> currency = new TreeMap();

    public CashRegisterController() {
        currency = createDenominations();

    }

    NavigableMap<Money, Integer> createDenominations() {
        NavigableMap<Money, Integer> denominations = Money.values().stream().collect(
                                Collectors.toMap(k-> k, v-> 0,(m1,m2) ->0 , TreeMap::new));
        return denominations;
    }

    public void put(NavigableMap<Money, Integer> input) throws InvalidInputException {
        validate(input, false);
        input.forEach((k, v) -> {
            BiFunction<Integer, Integer, Integer> netcount = (v1, v2) -> v1 + v2;
            currency.merge(k, v, netcount);
        });
    }

    public void take(NavigableMap<Money, Integer> input) throws InvalidInputException {
        validate(input, true);
        input.forEach((k, v) -> {
            BiFunction<Integer, Integer, Integer> takecount = (v1, v2) -> v1 - v2;
            currency.merge(k, v, takecount);
        });
    }

    public String change(Integer total) throws InvalidInputException {
        NavigableMap<Money, Integer> results = this.createDenominations();
        boolean changeFound = change(total, total, results);
        if(changeFound) {
            this.take(results);
            return this.show(results, false);
        }
        throw new InvalidInputException("Combination of currencies not found in register");
    }

    /**
     * Change - Start with highest denomination for a input total. If no match, try lower denomination.
     * @param total - total for which change is requested
     * @param balance - balance for which change needs to be calculated at a given stage
     * @param results - Accumulated denominations for total at a given stage
     * @return boolean - whether possible change combination found in register
     * @throws InvalidInputException
     */
    public boolean change(Integer total, Integer balance, NavigableMap<Money, Integer> results)
            throws InvalidInputException {
        boolean changeFound = false;

        if (balance == 0) { //successfully found change for a given total if balance is zero
           changeFound = true;
        }
        if (!changeFound) { //change start with highest possible denomination
            Money highestDenomination = getHighestDenomination(balance);
            changeFound = tryChangeWithDenomination(total, balance, highestDenomination, results);
            // if change not found, try the next lower denomination
            if (!changeFound && highestDenomination != null) {
                Money lowerDenomination = currency.higherKey(highestDenomination);
                changeFound = tryChangeWithDenomination(total, balance, lowerDenomination, results);

                //if change not found, try the next lower denomination
                if (!changeFound && lowerDenomination != null) {
                    Money nextLowerDenomination = currency.higherKey(lowerDenomination);
                    changeFound = tryChangeWithDenomination(total, balance, nextLowerDenomination, results);
                }
            }
        }
        return changeFound;
    }

    /**
     * Try different splits in a currency denomination
     */
    private boolean tryChangeWithDenomination(int total, int balance, Money denomination, NavigableMap<Money, Integer> results) throws InvalidInputException {
        if (denomination == null) {
            return false;
        }
        int denominationCount = currency.get(denomination) - results.get(denomination);
        if (denominationCount == 0) {
            return false;
        }
        int denominationValue = denomination.getValue();
        int neededSplit = balance/denominationValue;
        //lower of neededSplit and denomination count available in the register
        int splitCounter = neededSplit > denominationCount? denominationCount : neededSplit;

        for (int count = splitCounter; count > 0; count--) {
            try {
                results.put(denomination, count);
                int currencyValue = denomination.getValue();
                balance = balance - (count * currencyValue);
                change(total, balance, results); //find change for the remaining balance
                if (getTotal(results) == total) {
                    //change found if accumulated denominations matches total
                    return true;
                }
            } catch (InvalidInputException e) {
                //Handle exception and continue with the next combination
                e.printStackTrace();
            }
        }
        return false;
    }

    public Money getHighestDenomination(int total) {
        Optional<Money> maxMoney = currency.keySet().stream()
                .filter(
                        x -> x.getValue() <=total &&
                                currency.get(x) > 0).max(Comparator.comparing(Money::getValue));
        return maxMoney.isPresent() ?  maxMoney.get() : null;
    }

    public int getTotal() {
        return getTotal(currency);
    }

    private int getTotal(NavigableMap<Money, Integer> results) {
        return results.entrySet().stream().mapToInt(x -> x.getKey().getValue()*x.getValue()).sum();
    }

    public String show() {
        return show(currency, true);
    }

    private String show(NavigableMap<Money, Integer> inputMap, boolean includeTotal) {
        StringBuilder toStringBuilder = new StringBuilder();
        if (includeTotal) {
            toStringBuilder.append(getTotal());
        }

        Function<Money, Integer> zeroMapper = (k) -> 0;
        Money.values().forEach(k -> inputMap.computeIfAbsent(k,zeroMapper ));
        inputMap.navigableKeySet().stream().forEachOrdered( (x) -> toStringBuilder.append(" ").append(inputMap.get(x)));
        return toStringBuilder.toString().trim();

    }
    private void validate(NavigableMap<Money, Integer> inputEntries, boolean maxLimitCheck) throws InvalidInputException {
        Optional<Map.Entry<Money, Integer>> invalidEntries = inputEntries.entrySet().stream().filter( (entry) ->
        {   Integer countInRegister = currency.get(entry.getKey());
            return countInRegister == null || countInRegister < 0 || (maxLimitCheck && countInRegister < entry.getValue()); }).findAny();
        if (invalidEntries.isPresent()) {
            Map.Entry<Money, Integer> entry = invalidEntries.get();
            throw new InvalidInputException("Invalid Input data : money=" + entry.getKey() + " count:" + entry.getValue());
        }
    }

}
