package handler;

import action.VersionThreeAction;
import api.ActionSpecificHandler;
import event.PerformJumpEvent;
import util.EditorUtil;

/**
 * Created by runed on 11/13/2016.
 */
public class JumpHandler implements ActionSpecificHandler {
    private VersionThreeAction action;

    public JumpHandler(VersionThreeAction action) {
        this.action = action;
    }

    @Override
    public void handleAction(int offset) {
        EditorUtil.performMove(offset, action.getEditor());
        action.addEvent(new PerformJumpEvent(action));
    }
}
