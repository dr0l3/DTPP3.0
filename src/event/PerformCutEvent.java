package event;

import action.VersionThreeAction;
import handler.CutBetweenTargetsHandler;
import util.EditorUtil;

/**
 * Created by runed on 11/13/2016.
 */
public class PerformCutEvent implements PluginEvent {
    private VersionThreeAction action;
    private int firstOffset;
    private String removedText;

    public PerformCutEvent(VersionThreeAction action, int firstOffset, String removedText) {
        this.action = action;
        this.firstOffset = firstOffset;
        this.removedText = removedText;
    }

    @Override
    public void onUndo() {
        EditorUtil.performPaste(firstOffset, action.getEditor(), removedText);
        action.requeueHandler(new CutBetweenTargetsHandler(action));
        action.recreateMarkerPanel();
        action.recreateLastPopup();
    }
}
