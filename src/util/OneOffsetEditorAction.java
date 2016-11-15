package util;

import com.intellij.openapi.editor.Editor;

/**
 * Created by runed on 22-10-2016.
 */
public interface OneOffsetEditorAction {
    void performAction(int offset, Editor editor);
}
