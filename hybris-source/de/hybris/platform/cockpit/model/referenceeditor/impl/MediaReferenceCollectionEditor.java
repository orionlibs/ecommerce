package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.referenceeditor.ReferenceSelectorModel;
import de.hybris.platform.cockpit.model.referenceeditor.UIReferenceSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.ListitemRenderer;

@Deprecated
public class MediaReferenceCollectionEditor extends ReferenceCollectionEditor
{
    private static final Logger LOG = LoggerFactory.getLogger(MediaReferenceCollectionEditor.class);
    protected static final String EDIT_BTN_IMG = "cockpit/images/editMedia.png";


    public MediaReferenceCollectionEditor(EditorListener editorListener, AdditionalReferenceEditorListener additionalReferenceEditorListener)
    {
        super(editorListener, additionalReferenceEditorListener);
    }


    protected ListitemRenderer createCollectionItemListRenderer()
    {
        return (ListitemRenderer)new Object(this);
    }


    protected ReferenceSelector initializeReferenceSelector()
    {
        MediaReferenceSelector4Collection mediaReferenceSelector4Collection = new MediaReferenceSelector4Collection(this);
        mediaReferenceSelector4Collection.setAllowcreate(isAllowCreate());
        mediaReferenceSelector4Collection.setCreateContext(getCreateContext());
        mediaReferenceSelector4Collection.setWidth("100%");
        mediaReferenceSelector4Collection.setModel((ReferenceSelectorModel)getModel().getReferenceSelectorModel());
        mediaReferenceSelector4Collection.setParameters(getParameters());
        this.selectorController = (DefaultReferenceSelectorController)new DefaultMediaReferenceSelectorController(getModel().getReferenceSelectorModel(), (UIReferenceSelector)mediaReferenceSelector4Collection, null, this.additionalListener);
        this.selectorController.initialize();
        return (ReferenceSelector)mediaReferenceSelector4Collection;
    }
}
