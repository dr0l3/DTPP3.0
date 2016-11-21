package handler;

import action.VersionThreeAction;
import api.ActionSpecificHandler;
import event.SaveOffsetAndCreateBlankOverlayEvent;

/**
 * Created by runed on 11/13/2016.
 */
public class SaveOffsetAndCreateBlankOverlayHandler implements ActionSpecificHandler {
    private VersionThreeAction action;

    public SaveOffsetAndCreateBlankOverlayHandler(VersionThreeAction action) {
        this.action = action;
    }

    @Override
    public void handleAction(int offset) {
        action.addEvent(new SaveOffsetAndCreateBlankOverlayEvent(action));
        action.saveOffset(offset);
        System.out.println("Saving and doing stuff");
        action.disposePopup();
        action.createFreshPopup();
        action.showPopup();
        action.focusPopup();
    }
}
