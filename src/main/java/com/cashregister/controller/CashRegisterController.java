package com.cashregister.controller;

import com.cashregister.exceptions.InvalidInputException;
import com.cashregister.model.Money;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BiFunction;
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
        boolean result = change(total, results);
        if(result) {
            NavigableMap<Money, Integer> denominations = this.createDenominations();
            denominations.putAll(results);
            this.take(denominations);
            return this.show(denominations, false);
        }
        throw new InvalidInputException("Combination of currencies not found in register");
    }

    /**
     * Change - Match with splits in highest denomination. If not, follow up the next lower denomination.
     * @param total
     * @param results
     * @return
     * @throws InvalidInputException
     */
    public boolean change(Integer total, NavigableMap<Money, Integer> results) throws InvalidInputException {

        if (total == 0) {
            return true;
        }
        Money highestDenomination = getHighestDenomination(total);
        if (highestDenomination == null) {
            return false;
        }
        int countInRegister = currency.get(highestDenomination) - results.get(highestDenomination);
        int splitNeeded = total/highestDenomination.getValue();
        int currencyValue = highestDenomination.getValue();
        //Base case when a denomination can equal total value
        if (total == splitNeeded * currencyValue && splitNeeded <= countInRegister) {
            results.put(highestDenomination, splitNeeded);
            return true;
        }
        boolean result = false;
        if (splitNeeded <=countInRegister) { //Try highest currency denomination that fits the total
            result = tryChangeWithDenomination(total, highestDenomination, results);
        }
        if (!result) { // If not, try next lower denomination
            Money nextDenomination = currency.higherKey(highestDenomination);
            return tryChangeWithDenomination(total, nextDenomination, results);
        }
        return result;

    }

    /**
     * Try different splits starting within a currency denomination
     */
    private boolean tryChangeWithDenomination(int total, Money denomination, NavigableMap<Money, Integer> results) throws InvalidInputException {
        if (denomination == null) {
            return false;
        }
        int denominationCount = currency.get(denomination) - results.get(denomination);
        int denominationValue = denomination.getValue();
        int neededSplit = total/denominationValue;

        int splitCounter = neededSplit > denominationCount? denominationCount : neededSplit;
        boolean result = false;
        for (int count = splitCounter; count > 0; count--) {
            try {
                results.put(denomination, count);
                int currencyValue = denomination.getValue();
                result = change(total - (count) * currencyValue, results);
                if (result) {
                    break;
                }
            } catch (InvalidInputException e) {
                //Handle exception and continue with the next combination
                e.printStackTrace();
            }
        }
        return result;
    }

    public Money getHighestDenomination(int total) {
        Optional<Money> maxMoney = currency.keySet().stream()
                .filter(
                        x -> x.getValue() <=total &&
                                currency.get(x) > 0).max(Comparator.comparing(Money::getValue));
        return maxMoney.isPresent() ?  maxMoney.get() : null;
    }

    public int getTotal() {
        return currency.entrySet().stream().mapToInt(x -> x.getKey().getValue()*x.getValue()).sum();
    }

    public String show() {
        return show(currency, true);
    }

    private String show(NavigableMap<Money, Integer> inputMap, boolean includeTotal) {
        StringBuilder toStringBuilder = new StringBuilder();
        if (includeTotal) {
            toStringBuilder.append(getTotal());
        }
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
