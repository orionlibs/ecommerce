package de.hybris.platform.cockpit.model.editor.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Checkbox;

public class CheckboxBooleanUIEditor extends AbstractUIEditor
{
    private static final Logger LOG = LoggerFactory.getLogger(CheckboxBooleanUIEditor.class);
    private static final String TREAT_NULL_VALUES_AS_TRUE = "treatNullValuesAsTrue";
    protected boolean treatNullValuesAsTrue = false;


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Boolean treatNullValuesAs = Boolean.valueOf((String)parameters.get("treatNullValuesAsTrue"));
        if(treatNullValuesAs != null && treatNullValuesAs == Boolean.TRUE)
        {
            this.treatNullValuesAsTrue = true;
        }
        Checkbox editorView = new Checkbox();
        if(!isEditable())
        {
            editorView.setDisabled(true);
        }
        if(initialValue == null || initialValue instanceof Boolean)
        {
            Boolean value = (Boolean)initialValue;
            if(value == null)
            {
                value = Boolean.valueOf(this.treatNullValuesAsTrue);
            }
            editorView.setChecked(value.booleanValue());
        }
        else
        {
            LOG.error("Initial value not of type Boolean.");
        }
        editorView.addEventListener("onBlur", (EventListener)new Object(this, editorView, listener));
        editorView.addEventListener("onCheck", (EventListener)new Object(this, editorView, listener));
        editorView.addEventListener("onOK", (EventListener)new Object(this, editorView, listener));
        UITools.applyTestID((Component)editorView, "boolean_checkbox_ed");
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
