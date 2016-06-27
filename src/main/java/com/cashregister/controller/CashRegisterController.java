package com.cashregister.controller;

import com.cashregister.exceptions.InvalidInputException;
import com.cashregister.model.Money;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        NavigableMap<Money, Integer> denominations = createDenominations();
        //Fetch possible denomination combinations
        List<NavigableMap<Money, Integer>> possibleDenominations  = possibleStacks(null, denominations, total);
        Optional<NavigableMap<Money, Integer>> result = possibleDenominations.stream().filter(combination ->  {
            return total == combination.entrySet().stream().mapToInt(
                    (entry) -> (entry.getValue() * entry.getKey().getValue())).sum();
        }).findAny();
        if (result.isPresent()) {
            NavigableMap<Money, Integer> changeMap = result.get();
            take(changeMap);
            NavigableMap<Money, Integer> results = this.createDenominations();
            results.putAll(changeMap);
            return show(results, false);
        } else {
            throw new InvalidInputException("Sorry, cannot find a valid change in the cash register");
        }
    }

    public List<NavigableMap<Money, Integer>> possibleStacks(List<NavigableMap<Money,Integer>> preStacks, NavigableMap<Money, Integer> currencies, int total) {

        int preStackSize ;
        List<NavigableMap<Money,Integer>> results = new ArrayList();
        if (currencies == null || currencies.size() == 0 || total == 0) {
            return preStacks;
        }

        //Base Case
        if (preStacks == null || preStacks.size() == 0) {
            Money money = getHighestDenomination(total);
            currencies = currencies.subMap(money, true, currencies.lastKey(), true);

            int counts = currency.get(money);
            IntStream.rangeClosed(0,counts).forEachOrdered(  x -> {
                    NavigableMap<Money, Integer> possibleDenominations = new TreeMap();
                    possibleDenominations.put(money, x);
                    results.add(possibleDenominations);
            });
            currencies.remove(money);
        } else {
            preStackSize = preStacks.size();
            Money money = currencies.firstKey();
            int counts = currency.get(money);
            IntStream.range(0, preStackSize).forEachOrdered( i -> {
                    IntStream.rangeClosed(0, counts).forEachOrdered( j -> {
                        NavigableMap<Money,Integer> possibleDenominations = new TreeMap();
                        possibleDenominations.putAll(preStacks.get(i));
                        possibleDenominations.put(money, j);
                        results.add(possibleDenominations);
                    });
            });
            currencies.remove(money);
        }
        return possibleStacks(results, currencies, total); //possible combinations for next denomination
    }

    public Money getHighestDenomination(int total) {
        Optional<Money> maxMoney = currency.keySet().stream()
                .filter(
                        x -> x.getValue() <=total &&
                                currency.get(x) > 0).max(Comparator.comparing(Money::getValue));
        return maxMoney.isPresent() ?  maxMoney.get() : Money.twenty();
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
