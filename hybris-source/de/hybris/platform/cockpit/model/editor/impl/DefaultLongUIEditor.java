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

public class DefaultLongUIEditor extends AbstractTextBasedUIEditor
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultLongUIEditor.class);


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Long value = null;
        if(initialValue == null || initialValue instanceof Long)
        {
            value = (Long)initialValue;
        }
        else if(initialValue instanceof String)
        {
            value = Long.valueOf(Long.parseLong((String)initialValue));
        }
        else
        {
            LOG.error("Initial value not of type Long.");
        }
        setValue(value);
        if(isEditable())
        {
            Object object = new Object(this);
            object.setValue(value);
            UITools.applyTestID((Component)object, "long_ed");
            return (HtmlBasedComponent)createViewComponentInternal((InputElement)object, listener, parameters);
        }
        Label editorView = new Label();
        editorView.setValue((value == null) ? "" : value.toString());
        UITools.applyTestID((Component)editorView, "long_ed");
        return (HtmlBasedComponent)editorView;
    }


    public boolean isInline()
    {
        return true;
    }


    public String getEditorType()
    {
        return "LONG";
    }
}
