/*
 * Copyright 2000-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package util;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.ui.JBColor;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Rune on 09-10-2015.
 */
public class EditorUtil {

    public static void performScrollByLinenumber(Editor editor, int linesToMove){
        VisualPosition pos = editor.getCaretModel().getCurrentCaret().getLogicalPosition().toVisualPosition();
        int linenumber = ((pos.getLine() + linesToMove) > 0) ? (pos.getLine() + linesToMove) : 0;
        int currentcollumn = pos.getColumn();
        LogicalPosition newLogPos = new LogicalPosition(linenumber, currentcollumn);
        editor.getCaretModel().getCurrentCaret().moveToLogicalPosition(newLogPos);
        editor.getScrollingModel().scrollToCaret(ScrollType.CENTER);
    }

    public static void performScrollToPosition(Editor editor, int offset){
        LogicalPosition pos = editor.offsetToLogicalPosition(offset);
        editor.getScrollingModel().scrollTo(pos, ScrollType.CENTER);
    }

    public static void performDelete(int startOffset, int endOffset, Editor editor){
        Document document = editor.getDocument();
        WriteCommandAction.runWriteCommandAction(editor.getProject(), () -> {
            document.replaceString(
                    startOffset,endOffset, "");
        });
        editor.getSelectionModel().removeSelection();

    }

    public static void performMark(int startOffset, int endOffset, Editor editor){
        editor.getSelectionModel().setSelection(startOffset, endOffset);
    }


    public static void performPaste(List<Integer> offsets, Editor editor, String toBePasted){
        Runnable pasteRunnable = () -> {
            try {
                for (Integer offset : offsets) {
                    editor.getDocument().replaceString(offset, offset, toBePasted);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        };
        WriteCommandAction.runWriteCommandAction(editor.getProject(), pasteRunnable);
    }

    public static void performPaste(int offset, Editor editor, String toBePasted){
        Runnable pasteRunnable = () -> {
            editor.getDocument().replaceString(offset, offset, toBePasted);
        };
        WriteCommandAction.runWriteCommandAction(editor.getProject(), pasteRunnable);
    }

    public static void performPasteFromClipboard(int offset, Editor editor){
        String stringToPasted = extractCopiedStringFromClipboard();
        performPaste(offset, editor, stringToPasted);
    }

    public static String extractCopiedStringFromClipboard(){
        CopyPasteManager cpmanager = CopyPasteManager.getInstance();
        if(cpmanager.getContents()== null ) {
            return "";
        }
        DataFlavor[] dataFlavor = cpmanager.getContents().getTransferDataFlavors();
        Object toBePasted = (cpmanager.getContents(dataFlavor[0]));
        String stringToPasted;
        if (toBePasted instanceof String) {
            stringToPasted = (String)toBePasted;
        } else {
            stringToPasted = String.valueOf(toBePasted);

        }
        return stringToPasted;
    }

    public static void performPasteFromClipboard(List<Integer> offsets, Editor editor){
        String stringToPasted = extractCopiedStringFromClipboard();
        performPaste(offsets, editor, stringToPasted);
    }

    public static void performCut(int startOffset, int endOffset, Editor editor){
        performCopy(startOffset,endOffset,editor);
        performDelete(startOffset,endOffset,editor);
    }

    public static String performCutWithReturn(int startOffset, int endOffset, Editor editor){
        performCopy(startOffset, endOffset, editor);
        String text = extractCopiedStringFromClipboard();
        performDelete(startOffset,endOffset,editor);
        return text;
    }

    public static void performCopy(int startOffset, int endOffset, Editor editor){
        performMark(startOffset,endOffset,editor);
        editor.getSelectionModel().copySelectionToClipboard();
        editor.getSelectionModel().removeSelection();
    }

    public static void performMove(int offset, Editor editor){
        editor.getCaretModel().moveToOffset(offset);
    }

    public static void performInsertCaret(int offset, Editor editor){
        editor.getCaretModel().addCaret(editor.offsetToVisualPosition(offset));
    }

    public static void performMarkRange(int startOffset, int endOffset, Editor editor){
        MarkupModel markupModel = editor.getMarkupModel();
        TextAttributes attributes = new TextAttributes();
        attributes.setEffectType(EffectType.SEARCH_MATCH);
        attributes.setBackgroundColor(JBColor.CYAN);
        markupModel.addRangeHighlighter(startOffset, endOffset, HighlighterLayer.SELECTION, attributes, HighlighterTargetArea.EXACT_RANGE);
    }

    public static void performMarkSingleCharacter(int offset, Editor editor){
        MarkupModel markupModel = editor.getMarkupModel();
        TextAttributes attributes = new TextAttributes();
        attributes.setEffectType(EffectType.SEARCH_MATCH);
        attributes.setBackgroundColor(JBColor.CYAN);
        markupModel.addRangeHighlighter(offset, offset+3, HighlighterLayer.SELECTION, attributes, HighlighterTargetArea.EXACT_RANGE);
    }

    public static void performInsertCarets(List<Integer> offsets, Editor editor){
        for (Integer offset :
                offsets) {
            performInsertCaret(offset, editor);
        }
    }

    public static boolean isPrintableChar(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return (!Character.isISOControl(c)) &&
                c != KeyEvent.CHAR_UNDEFINED &&
                block != null &&
                block != Character.UnicodeBlock.SPECIALS;
    }

    public static char getCounterCase(char c) {
        return Character.isUpperCase(c) ? Character.toLowerCase(c) : Character.toUpperCase(c);
    }

    public static TextRange getVisibleTextRange(Editor editor) {
        Rectangle visibleArea = editor.getScrollingModel().getVisibleArea();
        LogicalPosition startLogicalPosition = editor.xyToLogicalPosition(visibleArea.getLocation());
        Double endVisualX = visibleArea.getX() + visibleArea.getWidth();
        Double endVisualY = visibleArea.getY() + visibleArea.getHeight();
        LogicalPosition endLogicalPosition = editor.xyToLogicalPosition(new Point(endVisualX.intValue(), endVisualY.intValue()));

        return new TextRange(editor.logicalPositionToOffset(startLogicalPosition), editor.logicalPositionToOffset(endLogicalPosition));
    }

    public static ArrayList<Integer> getMatchesForStringInTextRange(String searchString, Editor editor, TextRange textRange) {
        Document document = editor.getDocument();
        ArrayList<Integer> offsets = new ArrayList<>();
        int startOffset = textRange.getStartOffset();
        List<Integer> currentCaretOffsets = editor.getCaretModel().getAllCarets()
                .stream()
                .map(Caret::getOffset)
                .collect(Collectors.toList());
        String text = editor.getDocument().getText(textRange).toLowerCase();
        text = text.replace("\n", " ");
        text = text.replace("\r", " ");
        text = text.replace("\t", " ");
        int index = -1;
        while(true){
            index = text.indexOf(searchString, index + 1);
            if(index == -1){
                return offsets;
            }
            int offset = startOffset + index;
            //exclude current caret position
            if(currentCaretOffsets.contains(offset)){
                continue;
            }
            //exclude multiple spaces in a row
            if(index > 1 && index < text.length()-2
                    && stringOnlyContainsSpaces(searchString)
                    && text.charAt(index-1) == ' '
                    && document.getLineEndOffset(document.getLineNumber(offset)) != offset){
                continue;
            }
            offsets.add(offset);
        }
    }

    public static boolean stringOnlyContainsSpaces(String toBeChecked){
        for (char character : toBeChecked.toCharArray()){
            if(!(character == ' ')){
                return false;
            }
        }
        return true;
    }

}
