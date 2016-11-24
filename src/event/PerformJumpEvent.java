package event;

import action.VersionThreeAction;
import handler.JumpHandler;
import util.EditorUtil;

/**
 * Created by runed on 11/13/2016.
 */
public class PerformJumpEvent implements PluginEvent {
    private VersionThreeAction action;
    private String popupText;

    public PerformJumpEvent(VersionThreeAction action, String popupText) {
        this.action = action;
        this.popupText = popupText;
    }

    @Override
    public void onUndo() {
        EditorUtil.performMove(action.getOriginalOffset(), action.getEditor());
        action.requeueHandler(new JumpHandler(action));
        action.recreateMarkerPanel();
        action.recreateLastPopup(popupText);
    }
}
