package util;

import com.intellij.openapi.editor.Editor;

import java.util.List;

/**
 * Created by runed on 11/6/2016.
 */
public interface ManyOffsetEditorAction {
    void performAction(List<Integer> offset, Editor editor);
}
