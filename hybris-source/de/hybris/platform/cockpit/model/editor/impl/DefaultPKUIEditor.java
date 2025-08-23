package de.hybris.platform.cockpit.model.editor.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.PK;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.impl.InputElement;

public class DefaultPKUIEditor extends AbstractTextBasedUIEditor
{
    private static final Logger log = LoggerFactory.getLogger(DefaultPKUIEditor.class);


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Long value = null;
        if(initialValue == null || initialValue instanceof PK)
        {
            value = (initialValue == null) ? null : Long.valueOf(((PK)initialValue).getLongValue());
        }
        else if(initialValue instanceof String)
        {
            value = Long.valueOf(PK.parse((String)initialValue).getLongValue());
        }
        else if(initialValue instanceof Long)
        {
            value = (Long)initialValue;
        }
        else
        {
            log.error("Initial value not of type PK.");
        }
        setValue(value);
        if(isEditable())
        {
            Longbox longbox = new Longbox();
            longbox.setValue(value);
            UITools.applyTestID((Component)longbox, "pk_ed");
            return (HtmlBasedComponent)createViewComponentInternal((InputElement)longbox, listener, parameters);
        }
        Label editorView = new Label();
        editorView.setValue((value == null) ? "" : value.toString());
        UITools.applyTestID((Component)editorView, "pk_ed");
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
