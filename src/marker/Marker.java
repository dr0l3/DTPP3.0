package marker;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.openapi.editor.markup.*;
import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by runed on 11/12/2016.
 */
public class Marker {
    private int startOffset;
    private int endOffset;
    private String markerText;
    private String replacementText;
    private boolean primaryMarker;
    private boolean selectionMaker;
    private boolean ignored;

    public Marker() {
    }

    public void paint(Graphics graphics, Editor editor) {
        if(ignored){
            return;
        }
        graphics.setFont(editor.getColorsScheme().getFont(EditorFontType.BOLD));
        drawBackgroundOfMarker(graphics, editor);
        drawMarkerChar(graphics, editor);
    }
    private void drawBackgroundOfMarker(Graphics graphics, Editor editor) {
        JComponent canvas = editor.getContentComponent();
        Font font = editor.getColorsScheme().getFont(EditorFontType.BOLD);
        Rectangle2D fontRect = canvas.getFontMetrics(font).getStringBounds(String.valueOf(this.markerText), graphics);

        graphics.setColor(JBColor.WHITE);

        double x = canvas.getX() + editor.logicalPositionToXY(editor.offsetToLogicalPosition(this.startOffset)).getX();
        double y = canvas.getY() + editor.logicalPositionToXY(editor.offsetToLogicalPosition(this.endOffset)).getY();

        graphics.fillRect((int)x, (int)y, (int) fontRect.getWidth(), (int) fontRect.getHeight());
        if(this.markerText.length()> 1) {
            graphics.setColor(JBColor.GRAY);
            x = canvas.getX() + editor.logicalPositionToXY(editor.offsetToLogicalPosition(this.startOffset+1)).getX();
            y = canvas.getY() + editor.logicalPositionToXY(editor.offsetToLogicalPosition(this.endOffset)).getY();
            float bottomYOfMarkerChar = (float) (y + font.getSize());
            graphics.drawString(this.markerText.substring(1, this.markerText.length()), (int) x, (int) bottomYOfMarkerChar);
        }
    }

    private void drawMarkerChar(Graphics g, Editor editor) {
        JComponent canvas = editor.getContentComponent();
        Font font = editor.getColorsScheme().getFont(EditorFontType.BOLD);
        double x = canvas.getX() + editor.logicalPositionToXY(editor.offsetToLogicalPosition(this.startOffset)).getX();
        double y = canvas.getY() + editor.logicalPositionToXY(editor.offsetToLogicalPosition(this.startOffset)).getY();
        float bottomYOfMarkerChar = (float) (y + font.getSize());

        if(this.primaryMarker) {
            g.setColor(JBColor.RED);
        } else {
            g.setColor(JBColor.YELLOW);
        }

        ((Graphics2D)g).drawString(this.replacementText, (float)x, bottomYOfMarkerChar);
        MarkupModel markupModel = editor.getMarkupModel();
        TextAttributes attributes = new TextAttributes();
        attributes.setEffectType(EffectType.SEARCH_MATCH);
        attributes.setBackgroundColor(JBColor.WHITE);
        markupModel.addRangeHighlighter(this.startOffset, this.endOffset, HighlighterLayer.SELECTION, attributes, HighlighterTargetArea.EXACT_RANGE);
    }

    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }

    public String getMarkerText() {
        return markerText;
    }

    public void setMarkerText(String markerText) {
        this.markerText = markerText;
    }

    public String getReplacementText() {
        return replacementText;
    }

    public void setReplacementText(String replacementText) {
        this.replacementText = replacementText;
    }

    public boolean isPrimaryMarker() {
        return primaryMarker;
    }

    public void setPrimaryMarker(boolean primaryMarker) {
        this.primaryMarker = primaryMarker;
    }

    public boolean isSelectionMaker() {
        return selectionMaker;
    }

    public void setSelectionMaker(boolean selectionMaker) {
        this.selectionMaker = selectionMaker;
    }

    public boolean isIgnored() {
        return ignored;
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }
}
