package com.cashregister.actions;

import com.cashregister.userinterface.Editor;
import com.cashregister.userinterface.InputValidator;
import com.cashregister.exceptions.InvalidInputException;
import com.cashregister.exceptions.ValidatorException;

/**
 * Change action: change moneyValue
 */
public class ChangeAction implements Action, InputValidator {
    private final Editor editor;

    public ChangeAction(Editor editor) {
        this.editor = editor;
    }

    @Override
    public String perform(String input) throws InvalidInputException, ValidatorException {
        validate(input);
        try {
            String moneyValue = input.toLowerCase().trim().split(" ")[1];
            Integer intValue = Integer.parseInt(moneyValue);
            return editor.change(intValue);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new ValidatorException("Incorrect format: " + input + "; Correct format: change value ");
        }
    }

    @Override
    public void validate(String input) throws ValidatorException {
        boolean validated = input.toLowerCase().trim().matches("change \\d+");
        if (!validated) {
            throw new ValidatorException("Incorrect format: " + input + "; Correct format: change value ");
        }
    }
}
