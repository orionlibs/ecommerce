package de.hybris.platform.admincockpit.components.editorarea;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.impl.AbstractUIEditor;
import de.hybris.platform.cockpit.model.editor.impl.DefaultTextUIEditor;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;

public class ClassUIEditor extends DefaultTextUIEditor
{
    private static final Logger LOG = LoggerFactory.getLogger(ClassUIEditor.class);


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Textbox editorView = new Textbox();
        editorView.setSclass("textEditor");
        String label = "";
        if(initialValue instanceof Class)
        {
            editorView.setValue(((Class)initialValue).getName());
        }
        else if(initialValue != null)
        {
            label = initialValue.toString();
            editorView.setValue(label);
        }
        UITools.applyTestID((Component)editorView, "dummy_ed");
        return (HtmlBasedComponent)createViewComponentInternal((InputElement)editorView, listener, parameters);
    }


    protected AbstractUIEditor.CancelButtonContainer createViewComponentInternal(InputElement editorView, EditorListener listener, Map<String, ? extends Object> parameters)
    {
        parseInitialInputString(parameters);
        if(!isEditable())
        {
            editorView.setDisabled(true);
        }
        this.inEditMode = false;
        AbstractUIEditor.CancelButtonContainer cancelButtonContainer = new AbstractUIEditor.CancelButtonContainer((AbstractUIEditor)this, listener, (AbstractUIEditor.CancelListener)new Object(this, listener, editorView));
        cancelButtonContainer.setContent((Component)editorView);
        editorView.addEventListener("onFocus", (EventListener)new Object(this, editorView, cancelButtonContainer));
        editorView.addEventListener("onBlur", (EventListener)new Object(this, editorView, listener, cancelButtonContainer));
        editorView.addEventListener("onChange", (EventListener)new Object(this, editorView, listener));
        editorView.addEventListener("onOK", (EventListener)new Object(this, listener));
        editorView.addEventListener("onCancel", (EventListener)new Object(this, editorView, listener));
        return cancelButtonContainer;
    }


    public String getEditorType()
    {
        return "Dummy";
    }


    private void setClass(String className)
    {
        try
        {
            if(className != null && !className.isEmpty())
            {
                setValue(Class.forName(className));
            }
        }
        catch(ClassNotFoundException e)
        {
            LOG.error(e.getMessage(), e);
        }
    }


    protected void fireValueChanged(EditorListener listener)
    {
        if(this.originalValue != null || !(getValue() instanceof String) || !StringUtils.isBlank((String)getValue()))
        {
            if(this.inEditMode)
            {
                listener.valueChanged(getValue());
            }
        }
    }
}
