package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import handler.CutBetweenTargetsHandler;
import handler.SaveOffsetAndCreateBlankOverlayHandler;

/**
 * Created by runed on 11/13/2016.
 */
public class CutBetweenTargetsAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        VersionThreeAction action = new VersionThreeAction();
        action.addActionSpecificHandler(new SaveOffsetAndCreateBlankOverlayHandler(action));
        action.addActionSpecificHandler(new CutBetweenTargetsHandler(action));
        action.actionPerformed(anActionEvent);
    }
}
