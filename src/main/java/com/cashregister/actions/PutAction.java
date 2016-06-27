package com.cashregister.actions;

import com.cashregister.userinterface.BaseParser;
import com.cashregister.userinterface.Editor;
import com.cashregister.model.Money;
import com.cashregister.exceptions.InvalidInputException;
import com.cashregister.exceptions.ValidatorException;

import java.util.NavigableMap;

/**
 * PutAction: adds Money to the cash register
 */
public class PutAction extends BaseParser implements Action {
    private final Editor editor;

    public PutAction(Editor editor) {
        this.editor = editor;
    }

    @Override
    public String perform(String input) throws InvalidInputException, ValidatorException {
        validate(input);
        NavigableMap<Money, Integer> denomations = this.parseInput(input);
        return editor.put(denomations);
    }

    @Override
    public void validate(String input) throws ValidatorException {
        //put 1 3 4 5 6
        boolean validated = input.toLowerCase().trim().matches("put \\d+ \\d+ \\d+ \\d+ \\d+");
        if (! validated) {
            throw new ValidatorException("Incorrect format: " + input + " ; Correct format : put num20 num10 num5 num2 num1");
        }
    }
}
