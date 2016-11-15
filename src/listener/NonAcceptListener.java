package listener;

import action.VersionThreeAction;
import util.EditorUtil;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

/**
 * Created by runed on 11/13/2016.
 */
public class NonAcceptListener implements KeyListener{

    private VersionThreeAction action;

    public NonAcceptListener(VersionThreeAction action) {
        this.action = action;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == '\u001B') {
            System.out.println("Action not accepted");
            action.handleEscape();
        }
        action.getEditor().getContentComponent().removeKeyListener(this);
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
