package com.cashregister.actions;

import com.cashregister.userinterface.Editor;

/**
 * Factory to create actions
 */
public class ActionFactory {
    public static Action createAction(String input, Editor editor) {
        Action action = null;
        if  (input.startsWith(Action.SHOW)) {
            action = new ShowAction(editor);
        } else if (input.startsWith(Action.PUT)) {
            action = new PutAction(editor);
        } else if (input.startsWith(Action.TAKE)) {
            action = new TakeAction(editor);
        } else if (input.startsWith(Action.CHANGE)) {
            action = new ChangeAction(editor);
        }
        return action;
    }
}
