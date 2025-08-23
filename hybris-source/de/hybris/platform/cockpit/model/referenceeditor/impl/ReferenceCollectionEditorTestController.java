package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.referenceeditor.ReferenceCollectionEditorListener;
import de.hybris.platform.cockpit.model.referenceeditor.ReferenceCollectionEditorModelListener;
import de.hybris.platform.cockpit.model.referenceeditor.UIReferenceCollectionEditor;

public class ReferenceCollectionEditorTestController
{
    private final UIReferenceCollectionEditor collectionEditor;
    private final DefaultReferenceCollectionEditorModel model;


    public ReferenceCollectionEditorTestController(UIReferenceCollectionEditor collectionEditor, DefaultReferenceCollectionEditorModel model)
    {
        this.collectionEditor = collectionEditor;
        this.model = model;
    }


    public void initialize()
    {
        this.collectionEditor.addReferenceCollectionEditorListener(createReferenceCollectionEditorListener(this.model));
        this.model.addCollectionEditorModelListener(createCollectionEditorModelListener(this.collectionEditor));
    }


    protected ReferenceCollectionEditorListener createReferenceCollectionEditorListener(DefaultReferenceCollectionEditorModel model)
    {
        return (ReferenceCollectionEditorListener)new DefaultReferenceCollectionEditorListener(model, null);
    }


    protected ReferenceCollectionEditorModelListener createCollectionEditorModelListener(UIReferenceCollectionEditor collectionEditor)
    {
        return (ReferenceCollectionEditorModelListener)new DefaultReferenceCollectionEditorModelListener(null, collectionEditor, null);
    }
}
