package event;

import action.VersionThreeAction;

/**
 * Created by runed on 11/12/2016.
 */
public class SetSelectingEvent implements PluginEvent {
    private VersionThreeAction action;

    public SetSelectingEvent(VersionThreeAction action) {
        this.action = action;
    }

    @Override
    public void onUndo() {
        action.setSelectingWithoutEvent(!action.isSelecting());
    }
}
