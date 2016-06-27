package com.cashregister.actions;

import com.cashregister.userinterface.BaseParser;
import com.cashregister.userinterface.Editor;
import com.cashregister.model.Money;
import com.cashregister.exceptions.InvalidInputException;
import com.cashregister.exceptions.ValidatorException;

import java.util.NavigableMap;

/**
 * Take action: take money from register
 */
public class TakeAction extends BaseParser implements Action {

    private final Editor editor;

    public TakeAction(Editor editor) {
        this.editor = editor;
    }

    @Override
    public String perform(String input) throws InvalidInputException, ValidatorException {
        validate(input);
        NavigableMap<Money, Integer> denominations = this.parseInput(input);
        return editor.take(denominations);
    }

    public void validate(String input) throws ValidatorException {
        //take 1 3 4 5 6
        boolean validated = input.toLowerCase().trim().matches(Action.TAKE+" \\d+ \\d+ \\d+ \\d+ \\d+");
        if (! validated) {
            throw new ValidatorException("Incorrect format: " + input + " ; Correct format : take num20 num10 num5 num2 num1");
        }
    }
}
