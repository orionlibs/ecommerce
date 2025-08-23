package de.hybris.platform.cockpit.model.editor.impl;

import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.ListUIEditor;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;

public class DefaultFeatureUIEditor extends AbstractFeatureUIEditor
{
    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        HtmlBasedComponent editorView = super.createViewComponent(initialValue, parameters, getEditorListener(listener));
        editorView.setSclass("feature");
        this.editor = getEditor(getAssignment());
        if(this.editor != null)
        {
            if(this.editor instanceof ListUIEditor)
            {
                ((ListUIEditor)this.editor).setAvailableValues(AbstractUIEditor.filterValues((PropertyDescriptor)getPropertyDescriptor(), getTypeService()
                                .getAvailableValues((PropertyDescriptor)getPropertyDescriptor())));
            }
            this.editor.setEditable(isEditable());
            this.editorViewComponent = this.editor.createViewComponent(getEditorInitialValue(initialValue), parameters,
                            getEditorListener(listener));
            this.editor.setValue(getEditorInitialValue(initialValue));
            editorView.appendChild((Component)this.editorViewComponent);
        }
        if(supportUnits())
        {
            HtmlBasedComponent unitsDropdown;
            editorView.setSclass("featureWithUnits");
            this.unitEditor = createUnitUIEditor();
            this.unitEditor.setEditable((isSearchMode(parameters) || isEditable()));
            ClassificationAttributeUnitModel selectedUnit = (this.featureValue == null) ? getAssignment().getUnit() : this.featureValue.getUnit();
            if(selectedUnit != null)
            {
                TypedObject typedObject = getTypeService().wrapItem(selectedUnit);
                unitsDropdown = this.unitEditor.createViewComponent(typedObject, parameters, getEditorListener(listener));
                this.unitEditor.setValue(typedObject);
            }
            else
            {
                unitsDropdown = this.unitEditor.createViewComponent("null", parameters, getEditorListener(listener));
            }
            editorView.appendChild((Component)unitsDropdown);
            this.editorViewComponent.setSclass("featureValue");
        }
        return editorView;
    }


    public Object getValue()
    {
        return this.featureValue;
    }


    public boolean isInline()
    {
        return true;
    }
}
