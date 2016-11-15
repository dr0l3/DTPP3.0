package listener;

import action.VersionThreeAction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by runed on 15-10-2016.
 */
public class SearchKeyListener implements KeyListener {
    private VersionThreeAction action;

    public SearchKeyListener(VersionThreeAction action) {
        this.action = action;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(!action.isSelecting()){
            return;
        }
        action.handleSelect(String.valueOf(e.getKeyChar()));
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
