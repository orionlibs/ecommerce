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

public class DefaultIntegerUIEditor extends AbstractTextBasedUIEditor
{
    private static final Logger log = LoggerFactory.getLogger(DefaultIntegerUIEditor.class);


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Integer value = null;
        if(initialValue == null || initialValue instanceof Integer)
        {
            value = (Integer)initialValue;
        }
        else if(initialValue instanceof String)
        {
            value = Integer.valueOf(Integer.parseInt((String)initialValue));
        }
        else
        {
            log.error("Initial value not of type Integer.");
        }
        setValue(value);
        if(isEditable())
        {
            Object object = new Object(this);
            object.setValue(value);
            UITools.applyTestID((Component)object, "integer_ed");
            return (HtmlBasedComponent)createViewComponentInternal((InputElement)object, listener, parameters);
        }
        Label editorView = new Label();
        editorView.setValue((value == null) ? "" : value.toString());
        UITools.applyTestID((Component)editorView, "integer_ed");
        return (HtmlBasedComponent)editorView;
    }


    public boolean isInline()
    {
        return true;
    }


    public String getEditorType()
    {
        return "INTEGER";
    }
}
