package event;

import action.VersionThreeAction;

/**
 * Created by G50848 on 23/11/2016.
 */
public class SaveOffsetAndCreateBlankOverlayEvent implements PluginEvent {
    private VersionThreeAction action;
    private String popupTextBeforeSelect;

    public SaveOffsetAndCreateBlankOverlayEvent(VersionThreeAction action, String popupTextBeforeSelect) {
        this.action = action;
        this.popupTextBeforeSelect = popupTextBeforeSelect;
    }

    @Override
    public void onUndo() {
        action.removeLastOffset();
        action.recreateMarkerPanel();
        action.disposePopup();
        action.recreateLastPopup(popupTextBeforeSelect);
    }
}
