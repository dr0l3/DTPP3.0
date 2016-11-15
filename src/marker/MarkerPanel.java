package marker;

import com.intellij.openapi.editor.Editor;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by runed on 11/12/2016.
 */
public class MarkerPanel extends JComponent {

    private Editor editor;
    private java.util.List<Marker> markerList;

    public MarkerPanel(Editor editor) {
        this.editor = editor;
        this.markerList = new ArrayList<>();
        setupLocationAndBoundsOfPanel(editor);
    }

    public void setMarkerList(List<Marker> markerList) {
        this.markerList = markerList;
    }

    private void setupLocationAndBoundsOfPanel(Editor editor) {
        JComponent parent = editor.getContentComponent();
        this.setLocation(0, 0);
        this.invalidate();
        Rectangle visibleArea = editor.getScrollingModel().getVisibleAreaOnScrollingFinished();
        int x = (int) (parent.getLocation().getX() + visibleArea.getX() + editor.getScrollingModel().getHorizontalScrollOffset());
        this.setBounds(x, (int) (visibleArea.getY()), (int) visibleArea.getWidth(), (int) visibleArea.getHeight());
    }

    @Override
    public void paint(Graphics graphics){
        setupLocationAndBoundsOfPanel(editor);
        markerList.forEach(marker -> marker.paint(graphics, editor));
    }
}
