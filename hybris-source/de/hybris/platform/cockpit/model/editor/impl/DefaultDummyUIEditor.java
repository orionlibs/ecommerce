package de.hybris.platform.cockpit.model.editor.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Label;

public class DefaultDummyUIEditor extends AbstractUIEditor
{
    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        String value = "";
        if(initialValue instanceof TypedObject)
        {
            value = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject((TypedObject)initialValue);
        }
        else if(initialValue != null)
        {
            value = initialValue.toString();
        }
        Label editorView = new Label();
        editorView.setValue(value);
        UITools.applyTestID((Component)editorView, "dummy_ed");
        return (HtmlBasedComponent)editorView;
    }


    public boolean isInline()
    {
        return true;
    }


    public String getEditorType()
    {
        return "Dummy";
    }
}
