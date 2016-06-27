package com.cashregister.actions;

import com.cashregister.userinterface.Editor;

/**
 *
 */
public class ActionFactory {
    public static Action createAction(String input, Editor editor) {
        Action action = null;
        if  (input.startsWith("show")) {
            action = new ShowAction(editor);
        } else if (input.startsWith("put")) {
            action = new PutAction(editor);
        } else if (input.startsWith("take")) {
            action = new TakeAction(editor);
        } else if (input.startsWith("change")) {
            action = new ChangeAction(editor);
        }
        return action;
    }
}
