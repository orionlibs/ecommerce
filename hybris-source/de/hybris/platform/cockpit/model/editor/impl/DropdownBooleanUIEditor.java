package de.hybris.platform.cockpit.model.editor.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;

public class DropdownBooleanUIEditor extends AbstractUIEditor
{
    private static final Logger LOG = LoggerFactory.getLogger(DropdownBooleanUIEditor.class);
    private BooleanCombobox editorView = null;


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        this.editorView = new BooleanCombobox(this);
        this.editorView.setReadonly(Boolean.TRUE.booleanValue());
        if(!isEditable())
        {
            this.editorView.setDisabled(true);
        }
        if(initialValue == null || initialValue instanceof Boolean)
        {
            this.editorView.setValue((Boolean)initialValue);
        }
        else
        {
            LOG.error("Initial value not of type Boolean.");
        }
        AbstractUIEditor.CancelButtonContainer cancelButtonCnt = new AbstractUIEditor.CancelButtonContainer(this, listener, (AbstractUIEditor.CancelListener)new Object(this, listener));
        cancelButtonCnt.setContent((Component)this.editorView);
        cancelButtonCnt.showButton(Boolean.TRUE.booleanValue());
        this.editorView.addEventListener("onFocus", (EventListener)new Object(this, cancelButtonCnt));
        this.editorView.addEventListener("onBlur", (EventListener)new Object(this, listener, cancelButtonCnt));
        this.editorView.addEventListener("onChange", (EventListener)new Object(this, listener));
        this.editorView.addEventListener("onOK", (EventListener)new Object(this, listener));
        this.editorView.addEventListener("onCancel", (EventListener)new Object(this, cancelButtonCnt, listener));
        UITools.applyTestID((Component)this.editorView, "boolean_dd_ed");
        return (HtmlBasedComponent)cancelButtonCnt;
    }


    public boolean isInline()
    {
        return true;
    }


    public String getEditorType()
    {
        return "BOOLEAN";
    }
}
