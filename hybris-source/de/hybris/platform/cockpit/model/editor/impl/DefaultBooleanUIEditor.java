package de.hybris.platform.cockpit.model.editor.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;

public class DefaultBooleanUIEditor extends AbstractUIEditor
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBooleanUIEditor.class);


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        BooleanRadioGroup editorView = new BooleanRadioGroup(this);
        if(!isEditable())
        {
            editorView.setDisabled(true);
        }
        if(initialValue == null || initialValue instanceof Boolean)
        {
            editorView.setValue((Boolean)initialValue);
        }
        else
        {
            LOG.error("Initial value not of type Boolean.");
        }
        editorView.addEventListener("onBlur", (EventListener)new Object(this, editorView, listener));
        editorView.addEventListener("onCheck", (EventListener)new Object(this, editorView, listener));
        editorView.addEventListener("onOK", (EventListener)new Object(this, editorView, listener));
        UITools.applyTestID((Component)editorView, "boolean_ed");
        return (HtmlBasedComponent)editorView;
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
