package event;

import action.VersionThreeAction;

/**
 * Created by runed on 11/12/2016.
 */
public class ActionStartedPlugin implements PluginEvent {
    private VersionThreeAction versionThreeAction;

    public ActionStartedPlugin(VersionThreeAction versionThreeAction) {
        this.versionThreeAction = versionThreeAction;
    }

    @Override
    public void onUndo() {
        versionThreeAction.exitAction();
    }
}
