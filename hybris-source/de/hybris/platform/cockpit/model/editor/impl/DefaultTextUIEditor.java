package de.hybris.platform.cockpit.model.editor.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;

public class DefaultTextUIEditor extends AbstractTextBasedUIEditor
{
    private static final Logger log = LoggerFactory.getLogger(DefaultTextUIEditor.class);
    private static final String COCKPIT_ID_CREATEWEBSITE_NAME_INPUT = "_CreateWebsite_Name_input";


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        if(initialValue == null || initialValue instanceof String)
        {
            this.originalValue = (String)initialValue;
        }
        else
        {
            Object object = parameters.get("propInfo");
            log.error("Initial value " + ((object == null) ? "" : ("for " + object + " ")) + "not of type String but " + initialValue
                            .getClass().getName());
            return (HtmlBasedComponent)new AbstractUIEditor.ErrorDiv((AbstractUIEditor)this, Labels.getLabel("editor.error.wrongvaluetype"));
        }
        String attQual = (String)parameters.get("attributeQualifier");
        StringBuilder customTestID = new StringBuilder("text_ed");
        if(attQual != null)
        {
            customTestID.insert(0, attQual + "_");
        }
        if(isEditable())
        {
            Textbox textbox = new Textbox();
            textbox.setSclass("textEditor");
            textbox.setValue(this.originalValue);
            Object rows = parameters.get("rows");
            if(rows instanceof String)
            {
                try
                {
                    textbox.setRows(Integer.parseInt((String)rows));
                }
                catch(Exception e)
                {
                    log.error(e.getMessage(), e);
                }
            }
            if("cmscockpit".equals(UITools.getWebAppName(UITools.getCurrentZKRoot().getDesktop())))
            {
                UITools.applyTestID((Component)textbox, UITools.getWebAppName(UITools.getCurrentZKRoot().getDesktop()) + "_CreateWebsite_Name_input");
            }
            else
            {
                UITools.applyTestID((Component)textbox, customTestID.toString());
            }
            return (HtmlBasedComponent)createViewComponentInternal((InputElement)textbox, listener, parameters);
        }
        Label editorView = new Label();
        editorView.setValue(this.originalValue);
        if("cmscockpit".equals(UITools.getWebAppName(UITools.getCurrentZKRoot().getDesktop())))
        {
            UITools.applyTestID((Component)editorView, UITools.getWebAppName(UITools.getCurrentZKRoot().getDesktop()) + "_CreateWebsite_Name_input");
        }
        else
        {
            UITools.applyTestID((Component)editorView, customTestID.toString());
        }
        return (HtmlBasedComponent)editorView;
    }


    protected void fireValueChanged(EditorListener listener)
    {
        if(this.originalValue != null || !(getValue() instanceof String) || !StringUtils.isBlank((String)getValue()))
        {
            this.originalValue = (String)getValue();
            super.fireValueChanged(listener);
        }
    }


    public boolean isInline()
    {
        return true;
    }


    public String getEditorType()
    {
        return "TEXT";
    }
}
