package de.hybris.platform.cockpit.model.editor.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Label;
import org.zkoss.zul.impl.InputElement;

public class DefaultFloatUIEditor extends AbstractTextBasedUIEditor
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultFloatUIEditor.class);


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Float value = null;
        if(initialValue == null || initialValue instanceof Float)
        {
            value = (Float)initialValue;
        }
        else if(initialValue instanceof Double)
        {
            value = Float.valueOf(((Double)initialValue).floatValue());
        }
        else if(initialValue instanceof String)
        {
            value = Float.valueOf(Float.parseFloat((String)initialValue));
        }
        else
        {
            LOG.error("Initial value not of type Float.");
        }
        setValue(value);
        String testId = getTestId(parameters);
        if(isEditable())
        {
            Object object = new Object(this);
            Double doubleValue = (value == null) ? null : Double.valueOf(value.toString());
            object.setValue(doubleValue);
            UITools.applyTestID((Component)object, testId);
            return (HtmlBasedComponent)createViewComponentInternal((InputElement)object, listener, parameters);
        }
        Label editorView = new Label();
        editorView.setValue((value == null) ? "" : value.toString());
        UITools.applyTestID((Component)editorView, testId);
        return (HtmlBasedComponent)editorView;
    }


    protected String getTestId(Map<String, ? extends Object> parameters)
    {
        try
        {
            return (String)parameters.get("attributeQualifier");
        }
        catch(Exception e)
        {
            LOG.warn("Could not resolve property descriptor qualifier for editor : " + getClass());
            return "dummy_ed";
        }
    }


    public void setValue(Object value)
    {
        Float floatValue = null;
        if(value == null || value instanceof Float)
        {
            floatValue = (Float)value;
            super.setValue(floatValue);
        }
        else if(value instanceof Double)
        {
            floatValue = Float.valueOf(((Double)value).floatValue());
            super.setValue(floatValue);
        }
        else
        {
            super.setValue(value);
        }
    }


    public boolean isInline()
    {
        return true;
    }


    public String getEditorType()
    {
        return "FLOAT";
    }
}
