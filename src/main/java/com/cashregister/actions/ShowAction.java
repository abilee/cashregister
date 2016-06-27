package com.cashregister.actions;

import com.cashregister.userinterface.Editor;
import com.cashregister.userinterface.InputValidator;
import com.cashregister.exceptions.InvalidInputException;
import com.cashregister.exceptions.ValidatorException;

/**
 * Show Action: show denominations in cash register
 */
public class ShowAction implements Action, InputValidator {
    private final Editor editor;

    public ShowAction(Editor editor) {
        this.editor = editor;
    }

    @Override
    public String perform(String input) throws InvalidInputException, ValidatorException {
        validate(input);
        return editor.show();
    }

    @Override
    public void validate(String input) throws ValidatorException {
        boolean validated = input.toLowerCase().trim().equals(Action.SHOW);
        if (!validated) {
            throw new ValidatorException("Incorrect format: " + input + " ; Correct input: show");
        }
    }
}
