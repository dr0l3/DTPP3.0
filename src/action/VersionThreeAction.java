package action;

import api.ActionSpecificHandler;
import api.SearchDirection;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import event.ActionStartedEvent;
import event.PluginEvent;
import event.SetSelectingEvent;
import listener.NonAcceptListener;
import marker.Marker;
import marker.MarkerComparator;
import marker.MarkerPanel;
import popup.TextPopup;
import util.EditorUtil;

import java.util.*;

import static util.EditorUtil.getMatchesForStringInTextRange;
import static util.EditorUtil.getVisibleTextRange;
import static util.MarkerUtil.findMarkerByReplacementText;
import static util.MarkerUtil.offsetsToMarkers;
import static util.MarkerUtil.sortMarkersAndAssignReplacementText;

/**
 * Created by runed on 11/12/2016.
 */
public class VersionThreeAction {
    private Stack<PluginEvent> eventStack;
    private List<Marker> markerList;
    private MarkerPanel markerPanel;
    private TextPopup textPopup;
    private Editor editor;
    private int contextPoint;
    private List<Integer> offsets;
    private int originalOffset;
    private Deque<ActionSpecificHandler> actionSpecificHandlerQueue = new LinkedList<>();


    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getData(CommonDataKeys.PROJECT);
        editor = anActionEvent.getData(CommonDataKeys.EDITOR);
        if (editor == null || project == null) {
            return;
        }

        this.eventStack = new Stack<>();
        this.markerList = new ArrayList<>();
        this.markerPanel = new MarkerPanel(editor);
        this.offsets = new ArrayList<>();
        editor.getContentComponent().add(this.markerPanel);
        createFreshPopup();
        showPopup();
        disposePopup();
        createFreshPopup();
        showPopup();
        this.contextPoint = editor.getCaretModel().getOffset();
        this.originalOffset = editor.getCaretModel().getOffset();
        eventStack.push(new ActionStartedEvent(this));
        System.out.println("Action started");
        textPopup.focus();
    }

    public void createFreshPopup(){
        System.out.println(this.toString());
        this.textPopup = new TextPopup(this);
        System.out.println("Showing new popup");
    }

    public void showPopup(){
        this.textPopup.show();
        System.out.println(this.textPopup.getInternalPopup().getLocationOnScreen().toString());
    }

    public void focusPopup(){
        this.textPopup.focus();
    }

    public void disposePopup(){
        System.out.println("Killing popup");
        this.textPopup.destroy();
    }

    public boolean isSelecting() {
        System.out.println("Getting selection status");
        return !textPopup.getTextField().isEditable();
    }

    public void updateMarkers(String text) {
        System.out.println("Updating markers to: " + text);
        if(text.equals("")){
            this.markerList = new ArrayList<>();
            return;
        }

        List<Integer> offsets = getMatchesForStringInTextRange(text, editor, getVisibleTextRange(editor));
        this.markerList = offsetsToMarkers(offsets, text);
        System.out.println("Markers in markerlist: " + this.markerList.size());

        if(this.markerList.isEmpty()){
            offsets = getMatchesForStringInTextRange(text, editor, new TextRange(0,editor.getDocument().getTextLength()));
            this.markerList= offsetsToMarkers(offsets, text);
            scrollToMarkerCollection();
        }

        this.markerList = sortMarkersAndAssignReplacementText(this.markerList, this.contextPoint);
        System.out.println("Markers in markerlist: " + this.markerList.size());
        this.markerPanel.setMarkerList(this.markerList);
        this.markerPanel.invalidate();
        this.markerPanel.repaint();
    }

    public void setSelecting(boolean b) {
        System.out.println("Setting selection status");
        textPopup.getTextField().setEditable(!b);
        eventStack.push(new SetSelectingEvent(this));
    }

    public void setSelectingWithoutEvent(boolean b){
        textPopup.getTextField().setEditable(!b);
        textPopup.getTextField().requestFocus();
    }

    public void searchFurther(SearchDirection direction) {
        System.out.println("Searching in direction + " + direction);
    }

    public void handleSelect(String choice) {
        //get marker
        System.out.println("Handling select for choice: " + choice);
        Optional<Marker> marker = findMarkerByReplacementText(markerList, choice);
        if(!marker.isPresent()){
            System.out.println("Marker not present");
            return;
        }
        //find location
        int offset = marker.get().getStartOffset();
        //if not escape then continue
        editor.getContentComponent().addKeyListener(new NonAcceptListener(this));
        //optimistically perform handler action
        actionSpecificHandlerQueue.poll().handleAction(offset);
    }

    public void handleSelectFirstOccurence(SearchDirection direction) {
        System.out.println("Selecting first occurence in direction + " + direction);
    }

    public void handleSelectAll() {
        System.out.println("Selecting all");
    }

    public void setContextPoint(int offset){
        this.contextPoint = offset;
    }

    public void exitAction(){
        editor.getScrollingModel().scrollToCaret(ScrollType.MAKE_VISIBLE);
        editor.getMarkupModel().removeAllHighlighters();
        editor.getContentComponent().remove(markerPanel);
        textPopup.getInternalPopup().dispose();
    }

    public Editor getEditor() {
        return editor;
    }

    public void handleEscape() {
        if(eventStack.size() > 0) {
            PluginEvent evt = eventStack.pop();
            evt.onUndo();
        }
        System.out.println("Current Event stack");
        eventStack.forEach(System.out::println);
    }

    private void scrollToMarkerCollection(){
        //do we go up or down?
        long markersDownwards = markerList.stream()
                .filter(marker2 -> marker2.getStartOffset() > contextPoint)
                .count();
        long markersUpwards = markerList.size()-markersDownwards;
        int offset;
        //center on first occurence
        if(markersDownwards> markersUpwards && markersDownwards > 0){
            System.out.println("Scrolling to markerlocation downwards");
            offset = markerList.stream()
                    .filter(marker2 -> marker2.getStartOffset() > contextPoint)
                    .sorted(new MarkerComparator(contextPoint))
                    .findFirst().get().getStartOffset();
            EditorUtil.performScrollToPosition(editor, offset);
            setContextPoint(offset);
        } else if(markersDownwards < markersUpwards && markersUpwards > 0) {
            System.out.println("Scrolling to markerlocation upwards");
            offset = markerList.stream()
                    .filter(marker2 -> marker2.getStartOffset() < contextPoint)
                    .sorted(new MarkerComparator(contextPoint))
                    .findFirst().get().getStartOffset();
            EditorUtil.performScrollToPosition(editor, offset);
            setContextPoint(offset);
        }
    }

    public List<Integer> getOffsets() {
        return offsets;
    }

    public void saveOffset(int offset){
        this.offsets.add(offset);
        System.out.println(this);
        this.offsets.forEach(System.out::println);
    }

    public void removeLastOffset(){
        this.offsets.remove(this.offsets.size()-1);
    }

    public int getContextPoint() {
        return contextPoint;
    }

    public int getOriginalOffset() {
        return originalOffset;
    }

    public void recreateLastPopup(String text) {
        textPopup = new TextPopup(textPopup, this);
        textPopup.setText(text);
        textPopup.show();
        textPopup.focus();
    }

    public void recreateMarkerPanel(){
        String searchText = textPopup.getTextField().getText();
        updateMarkers(searchText);
        editor.getContentComponent().add(markerPanel);
        markerPanel.repaint();
    }

    public Queue<ActionSpecificHandler> getActionSpecificHandlerQueue() {
        return actionSpecificHandlerQueue;
    }

    public void addActionSpecificHandler(ActionSpecificHandler actionSpecificHandler) {
        this.actionSpecificHandlerQueue.add(actionSpecificHandler);
    }

    public void addEvent(PluginEvent event){
        this.eventStack.push(event);
    }

    public void requeueHandler(ActionSpecificHandler handler) {
        this.actionSpecificHandlerQueue.addFirst(handler);
    }

    public void resetPopup() {
        this.textPopup.reset();
    }

    public void removeMarkerPanel() {
        this.editor.getContentComponent().remove(markerPanel);
    }

    public void clearMarkerList(){
        markerList = new ArrayList<>();
        markerPanel.setMarkerList(markerList);
        markerPanel.repaint();
    }

    public String getPopupText() {
        return textPopup.getTextField().getText();
    }
}
