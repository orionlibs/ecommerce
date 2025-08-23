package de.hybris.platform.cockpit.model.editor.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.impl.InputElement;

public abstract class AbstractTextBasedUIEditor extends AbstractUIEditor
{
    protected String originalValue = null;


    protected AbstractUIEditor.CancelButtonContainer createViewComponentInternal(InputElement editorView, EditorListener listener, Map<String, ? extends Object> parameters)
    {
        parseInitialInputString(parameters);
        if(!isEditable())
        {
            if(Executions.getCurrent().getUserAgent().contains("MSIE"))
            {
                editorView.setReadonly(true);
            }
            else
            {
                editorView.setDisabled(true);
            }
        }
        this.inEditMode = false;
        AbstractUIEditor.CancelButtonContainer cancelButtonContainer = new AbstractUIEditor.CancelButtonContainer(this, listener, (AbstractUIEditor.CancelListener)new Object(this, listener, editorView));
        cancelButtonContainer.setContent((Component)editorView);
        editorView.addEventListener("onFocus", (EventListener)new Object(this, editorView, cancelButtonContainer));
        editorView.addEventListener("onBlur", (EventListener)new Object(this, editorView, listener, cancelButtonContainer));
        editorView.addEventListener("onChange", (EventListener)new Object(this, editorView, listener));
        editorView.addEventListener("onOK", (EventListener)new Object(this, editorView, listener));
        editorView.addEventListener("onCancel", (EventListener)new Object(this, editorView, listener));
        return cancelButtonContainer;
    }


    public void setFocus(HtmlBasedComponent rootEditorComponent, boolean selectAll)
    {
        if(rootEditorComponent instanceof AbstractUIEditor.CancelButtonContainer)
        {
            InputElement element = (InputElement)((AbstractUIEditor.CancelButtonContainer)rootEditorComponent).getContent();
            element.setFocus(true);
            if(selectAll)
            {
                element.select();
            }
            if(this.initialInputString != null)
            {
                element.setText(this.initialInputString);
                int pos = this.initialInputString.length();
                element.setSelectionRange(pos, pos);
            }
        }
    }
}
