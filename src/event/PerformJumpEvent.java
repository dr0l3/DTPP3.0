package event;

import action.VersionThreeAction;
import handler.JumpHandler;
import util.EditorUtil;

/**
 * Created by runed on 11/13/2016.
 */
public class PerformJumpEvent implements PluginEvent {
    private VersionThreeAction action;

    public PerformJumpEvent(VersionThreeAction action) {
        this.action = action;
    }

    @Override
    public void onUndo() {
        EditorUtil.performMove(action.getOriginalOffset(), action.getEditor());
        action.requeueHandler(new JumpHandler(action));
        action.recreateMarkerPanel();
        action.recreateLastPopup();
    }
}
