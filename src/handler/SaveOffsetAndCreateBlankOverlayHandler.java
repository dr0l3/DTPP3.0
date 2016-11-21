package handler;

import action.VersionThreeAction;
import api.ActionSpecificHandler;

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
        action.saveOffset(offset);
        action.disposePopup();
        action.createFreshPopup();
        action.showPopup();
    }
}
