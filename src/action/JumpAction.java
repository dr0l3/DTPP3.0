package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import handler.JumpHandler;

/**
 * Created by runed on 11/13/2016.
 */
public class JumpAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        VersionThreeAction action = new VersionThreeAction();
        action.addActionSpecificHandler(new JumpHandler(action));
        action.actionPerformed(anActionEvent);
    }
}
