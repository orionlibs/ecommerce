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

public class DefaultDecimalUIEditor extends AbstractTextBasedUIEditor
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultDecimalUIEditor.class);


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Double value = null;
        if(initialValue == null || initialValue instanceof Double)
        {
            value = (Double)initialValue;
        }
        else if(initialValue instanceof String)
        {
            value = Double.valueOf(Double.parseDouble((String)initialValue));
        }
        else
        {
            LOG.error("Initial value not of type Double.");
        }
        setValue(value);
        String testId = getTestId(parameters);
        if(isEditable())
        {
            Object object = new Object(this);
            object.setValue(value);
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


    public boolean isInline()
    {
        return true;
    }


    public String getEditorType()
    {
        return "DECIMAL";
    }
}
