package com.cashregister.actions;

import com.cashregister.exceptions.InvalidInputException;
import com.cashregister.exceptions.ValidatorException;
import com.cashregister.userinterface.Editor;

/**
 */
public interface Action {
    public String perform(String input) throws InvalidInputException, ValidatorException;
    public static final String PUT = "put";
    public static final String TAKE = "take";
    public static final String CHANGE = "change";
    public static final String SHOW = "show";

}
