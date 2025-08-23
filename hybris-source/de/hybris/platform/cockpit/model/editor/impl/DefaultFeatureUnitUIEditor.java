package de.hybris.platform.cockpit.model.editor.impl;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;

public class DefaultFeatureUnitUIEditor extends DefaultSelectUIEditor
{
    protected List<Object> availableUnitValues;
    protected ClassAttributeAssignmentModel assignment;


    protected void addObjectToCombo(Object value, Combobox box)
    {
        Comboitem comboitem = new Comboitem();
        String label = null;
        String image = null;
        if(value instanceof TypedObject)
        {
            TypedObject typedObject = (TypedObject)value;
            LabelService labelService = UISessionUtils.getCurrentSession().getLabelService();
            label = labelService.getObjectTextLabel(typedObject);
            image = labelService.getObjectIconPath(typedObject);
        }
        else
        {
            label = value.toString();
        }
        comboitem.setLabel(label);
        comboitem.setValue(value);
        if(image != null)
        {
            comboitem.setImage(image);
        }
        if("".equals(label))
        {
            comboitem.setHeight("1em");
        }
        box.appendChild((Component)comboitem);
    }


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Label label;
        HtmlBasedComponent editorView = super.createViewComponent(initialValue, parameters, listener);
        editorView.setSclass("featureCombo");
        if(isEditable())
        {
            return editorView;
        }
        if(initialValue instanceof HybrisEnumValue)
        {
            label = new Label(((HybrisEnumValue)initialValue).getCode());
        }
        else if(initialValue instanceof TypedObject)
        {
            Object value = ((TypedObject)initialValue).getObject();
            if(value instanceof ClassificationAttributeUnitModel)
            {
                label = new Label(((ClassificationAttributeUnitModel)value).getCode());
            }
            else
            {
                label = new Label("-");
            }
        }
        else
        {
            label = new Label("-");
        }
        return (HtmlBasedComponent)label;
    }
}
