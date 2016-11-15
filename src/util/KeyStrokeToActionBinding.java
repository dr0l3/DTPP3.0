package util;

import javax.swing.*;

/**
 * Created by runed on 23-10-2016.
 */
public class KeyStrokeToActionBinding {
    private KeyStroke keyStroke;
    private String stringIntermediary;
    private Action action;

    public KeyStrokeToActionBinding(KeyStroke keyStroke, String stringIntermediary, Action action) {
        this.keyStroke = keyStroke;
        this.stringIntermediary = stringIntermediary;
        this.action = action;
    }


    public KeyStroke getKeyStroke() {
        return keyStroke;
    }

    public void setKeyStroke(KeyStroke keyStroke) {
        this.keyStroke = keyStroke;
    }

    public String getStringIntermediary() {
        return stringIntermediary;
    }

    public void setStringIntermediary(String stringIntermediary) {
        this.stringIntermediary = stringIntermediary;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
