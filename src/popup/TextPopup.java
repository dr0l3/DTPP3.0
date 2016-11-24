package popup;

import action.VersionThreeAction;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;
import listener.SearchKeyListener;
import util.ListenerUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by runed on 11/12/2016.
 */
public class TextPopup {
    private JBPopup internalPopup;
    private JTextField textField;
    private VersionThreeAction action;
    private JPanel panel;

    public TextPopup(VersionThreeAction action) {
        panel = new JPanel();
        textField = new JTextField();
        textField.setColumns(10);
        this.action = action;

        panel.add(textField, BorderLayout.WEST);

        //listens for actions when the textfield is not locked
        ListenerUtil.getDocumentListeners(action)
                .forEach(listener -> textField.getDocument().addDocumentListener(listener));

        //listens for special actions like enter, scroll etc.
        textField.getInputMap().remove(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        ListenerUtil.getKeybindings(action)
                .forEach(binding -> ListenerUtil.applyBinding(textField, binding));

        //listens for the choice when selecting
        textField.addKeyListener(new SearchKeyListener(action));

        this.internalPopup = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(panel, panel)
                .setCancelKeyEnabled(false)
                .setFocusable(true)
                .setMovable(false)
                .setShowBorder(true)
                .createPopup();
    }

    public TextPopup(TextPopup original, VersionThreeAction action){
        JPanel panel = new JPanel();
        textField = new JTextField();
        textField.setText(original.getTextField().getText());
        textField.setColumns(original.getTextField().getColumns());
        textField.setEditable(original.getTextField().isEditable());
        this.action = action;

        panel.add(textField, BorderLayout.WEST);

        //listens for actions when the textfield is not locked
        ListenerUtil.getDocumentListeners(action)
                .forEach(listener -> textField.getDocument().addDocumentListener(listener));

        //listens for special actions like enter, scroll etc.
        textField.getInputMap().remove(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        ListenerUtil.getKeybindings(action)
                .forEach(binding -> ListenerUtil.applyBinding(textField, binding));

        //listens for the choice when selecting
        textField.addKeyListener(new SearchKeyListener(action));

        this.internalPopup = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(panel, panel)
                .setCancelKeyEnabled(false)
                .setFocusable(true)
                .setMovable(false)
                .setShowBorder(true)
                .createPopup();
    }

    public void show(){
        RelativePoint popupLocation = JBPopupFactory.getInstance().guessBestPopupLocation(action.getEditor().getComponent());
        internalPopup.show(popupLocation);
    }

    public void focus(){
        this.textField.grabFocus();
    }

    public void destroy(){
        internalPopup.cancel();
        internalPopup.dispose();
    }

    public void dispose(){
        internalPopup.dispose();
        internalPopup.cancel();
    }

    public void cancel(){
        internalPopup.cancel();

    }

    public JBPopup getInternalPopup() {
        return internalPopup;
    }

    public JTextField getTextField() {
        return textField;
    }

    public void reset() {
        this.textField.setEditable(true);
        this.textField.setText("");
    }

    public void setText(String text){
        textField.setText(text);
    }
}
