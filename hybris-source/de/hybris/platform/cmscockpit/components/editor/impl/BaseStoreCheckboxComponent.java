package de.hybris.platform.cmscockpit.components.editor.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.impl.ReferenceCollectionEditor;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.zkoss.zul.ListitemRenderer;

public class BaseStoreCheckboxComponent extends ReferenceCollectionEditor
{
    private static final Logger LOG = Logger.getLogger(BaseStoreCheckboxComponent.class);
    private List<TypedObject> selectedElements = new ArrayList<>();
    private static final String COCKPIT_ID_CREATEWEBSITE_STORES_DEFAULTSTORE_CHECK = "CreateWebsite_Stores_DefaultStore_check";


    public List<TypedObject> getSelectedElements()
    {
        return this.selectedElements;
    }


    public void setSelectedElements(List<TypedObject> selectedElements)
    {
        this.selectedElements = selectedElements;
    }


    public BaseStoreCheckboxComponent(EditorListener editorListener, AdditionalReferenceEditorListener additionalListener)
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
        return ret;
    }
}
