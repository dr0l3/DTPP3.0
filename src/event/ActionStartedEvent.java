package event;

import action.VersionThreeAction;

/**
 * Created by runed on 11/12/2016.
 */
public class ActionStartedEvent implements PluginEvent {
    private VersionThreeAction versionThreeAction;

    public ActionStartedEvent(VersionThreeAction versionThreeAction) {
        this.versionThreeAction = versionThreeAction;
    }

    @Override
    public void onUndo() {
        versionThreeAction.exitAction();
    }
}
