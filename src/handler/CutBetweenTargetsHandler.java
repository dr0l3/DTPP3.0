package handler;

import action.VersionThreeAction;
import api.ActionSpecificHandler;
import event.PerformCutEvent;
import util.EditorUtil;

/**
 * Created by runed on 11/13/2016.
 */
public class CutBetweenTargetsHandler implements ActionSpecificHandler {
    private VersionThreeAction action;

    public CutBetweenTargetsHandler(VersionThreeAction action) {
        this.action = action;
    }

    @Override
    public void handleAction(int offset) {
        //perform the cut
        if(action.getOffsets().size() != 1){
            System.out.println("no offsets! cant cut.");
            return;
        }
        int firstOffset = action.getOffsets().get(0);
        String text = EditorUtil.performCutWithReturn(firstOffset, offset, action.getEditor());
        action.addEvent(new PerformCutEvent(action, firstOffset, text));
        action.removeMarkerPanel();
        action.disposePopup();
    }
}
