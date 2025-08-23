package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.misc.ComponentController;
import de.hybris.platform.cockpit.model.referenceeditor.ReferenceCollectionEditorListener;
import de.hybris.platform.cockpit.model.referenceeditor.ReferenceCollectionEditorModelListener;
import de.hybris.platform.cockpit.model.referenceeditor.UIReferenceCollectionEditor;

@Deprecated
public class ReferenceCollectionEditorController implements ComponentController
{
    private final UIReferenceCollectionEditor collectionEditor;
    private final DefaultReferenceCollectionEditorModel model;
    private final EditorListener editorListener;
    private final ObjectType rootType;
    private ReferenceCollectionEditorListener referenceCollectionEditorListener;
    private ReferenceCollectionEditorModelListener referenceCollectionEditorModelListener;


    public ReferenceCollectionEditorController(UIReferenceCollectionEditor collectionEditor, DefaultReferenceCollectionEditorModel model, EditorListener editorListener, ObjectType rootType)
    {
        this.collectionEditor = collectionEditor;
        this.model = model;
        this.editorListener = editorListener;
        this.rootType = rootType;
    }


    public void initialize()
    {
        this.referenceCollectionEditorListener = createReferenceCollectionEditorListener();
        this.referenceCollectionEditorModelListener = createCollectionEditorModelListener();
        this.collectionEditor.addReferenceCollectionEditorListener(this.referenceCollectionEditorListener);
        this.model.addCollectionEditorModelListener(this.referenceCollectionEditorModelListener);
    }


    protected ReferenceCollectionEditorListener createReferenceCollectionEditorListener()
    {
        return (ReferenceCollectionEditorListener)new DefaultReferenceCollectionEditorListener(this.model, this.rootType);
    }


    protected ReferenceCollectionEditorModelListener createCollectionEditorModelListener()
    {
        return (ReferenceCollectionEditorModelListener)new DefaultReferenceCollectionEditorModelListener(this.model, this.collectionEditor, this.editorListener);
    }


    public void unregisterListeners()
    {
        if(this.collectionEditor != null)
        {
            this.collectionEditor.removeReferenceCollectionEditorListener(this.referenceCollectionEditorListener);
        }
        if(this.model != null)
        {
            this.model.removeCollectionEditorModelListener(this.referenceCollectionEditorModelListener);
        }
    }
}
