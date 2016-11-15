package util;

import com.intellij.openapi.editor.Editor;

/**
 * Created by runed on 22-10-2016.
 */
public interface TwoOffsetEditorAction {
    void performAction(int startOffset, int endOffset, Editor editor);
}
