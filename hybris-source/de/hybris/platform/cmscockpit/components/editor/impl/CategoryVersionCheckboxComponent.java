package de.hybris.platform.cmscockpit.components.editor.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.impl.ReferenceCollectionEditor;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListitemRenderer;

public class CategoryVersionCheckboxComponent extends ReferenceCollectionEditor
{
    private List<TypedObject> selectedElements = new ArrayList<>();


    public List<TypedObject> getSelectedElements()
    {
        return this.selectedElements;
    }


    public void setSelectedElements(List<TypedObject> selectedElements)
    {
        this.selectedElements = selectedElements;
    }


    public CategoryVersionCheckboxComponent(EditorListener editorListener, AdditionalReferenceEditorListener additionalListener)
    {
        super(editorListener, additionalListener);
    }


    public void saveItems(List<TypedObject> items)
    {
    }


    protected ListitemRenderer createCollectionItemListRenderer()
    {
        return (ListitemRenderer)new Object(this);
    }


    public boolean initialize()
    {
        boolean ret = super.initialize();
        this.referenceSelector.setVisible(false);
        setSclass("categoryCheckboxChooser");
        Button saveBtn = new Button(Labels.getLabel("general.save"));
        saveBtn.addEventListener("onClick", (EventListener)new Object(this));
        return ret;
    }
}
