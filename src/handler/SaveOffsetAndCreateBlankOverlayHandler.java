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
        // TODO: 24/11/2016 Text from popup should be saved here
        action.addEvent(new SaveOffsetAndCreateBlankOverlayEvent(action));
        // TODO: 24/11/2016 Special offset markers for selected stuff should be inserted here 
        action.saveOffset(offset);
        action.clearMarkerList();
        System.out.println("Saving and doing stuff");
        action.disposePopup();
        action.createFreshPopup();
        action.showPopup();
        action.focusPopup();
    }
}
