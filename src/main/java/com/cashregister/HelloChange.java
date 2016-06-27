package com.cashregister;

import com.cashregister.actions.*;
import com.cashregister.userinterface.Editor;
import com.cashregister.userinterface.POSEditor;
import com.cashregister.exceptions.InvalidInputException;
import com.cashregister.exceptions.ValidatorException;

import java.io.Console;

/**
 * Main program to interacts with a cash register.
 *
 */
public class HelloChange {
    Editor posEditor;

    HelloChange() {
        posEditor = new POSEditor();
    }

    public Editor getPosEditor() {
        return posEditor;
    }

    public static void main(String[] args) {

        Console console = System.console();

        if(console == null) {
            System.exit(-1);
        }
        HelloChange change = new HelloChange();
        String help = "Hello change. \n" +
                "show = show denominations in register \n" +
                "put num20 num10 num5 num2 num1  = add denominations to register \n" +
                "take num20 num10 num5 num2 num1 = take denominations from register \n" +
                "change value = remove money value \n" +
                "quit = exit \n" +
                " Ready \n";

        console.printf(help);
        Editor posEditor = change.getPosEditor();
        while (true) {
            String input = console.readLine().toLowerCase();
            try {

                Action action = ActionFactory.createAction(input, posEditor);
                if (action != null) {
                    String resultValues = action.perform(input);
                    console.printf(resultValues +"\n");
                } else if ("quit".equalsIgnoreCase(input)) {
                    console.printf("Bye");
                    System.exit(0);
                } else {
                    console.printf(help);
                }

            } catch (ValidatorException e) {
                console.printf( e.getMessage() + " \n");
            } catch (InvalidInputException e) {
                console.printf("sorry \n");
            }

        }

    }
}
