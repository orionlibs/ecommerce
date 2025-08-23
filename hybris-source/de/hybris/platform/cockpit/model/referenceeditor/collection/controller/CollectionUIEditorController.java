package de.hybris.platform.cockpit.model.referenceeditor.collection.controller;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.misc.ComponentController;
import de.hybris.platform.cockpit.model.referenceeditor.collection.AbstractCollectionEditor;
import de.hybris.platform.cockpit.model.referenceeditor.collection.CollectionEditorController;
import de.hybris.platform.cockpit.model.referenceeditor.collection.DefaultCollectionEditorListener;
import de.hybris.platform.cockpit.model.referenceeditor.collection.model.CollectionEditorModelListener;
import de.hybris.platform.cockpit.model.referenceeditor.collection.model.DefaultCollectionEditorModel;
import de.hybris.platform.cockpit.model.referenceeditor.collection.model.DefaultCollectionEditorModelListener;

public class CollectionUIEditorController implements ComponentController
{
    private final transient AbstractCollectionEditor collectionEditor;
    private final DefaultCollectionEditorModel model;
    private final ObjectType rootType;
    private final EditorListener editorListener;
    private CollectionEditorModelListener modelListener;


    public CollectionUIEditorController(AbstractCollectionEditor collectionEditor, DefaultCollectionEditorModel model, EditorListener editorListener, ObjectType rootType)
    {
        this.collectionEditor = collectionEditor;
        this.model = model;
        this.editorListener = editorListener;
        this.rootType = rootType;
    }


    public void initialize()
    {
        this.modelListener = createCollectionEditorModelListener();
        this.collectionEditor.setCollectionControler(createReferenceCollectionEditorListener());
        this.model.addCollectionEditorModelListener(this.modelListener);
    }


    protected CollectionEditorController createReferenceCollectionEditorListener()
    {
        return (CollectionEditorController)new DefaultCollectionEditorListener(this.model, this.rootType);
    }


    protected CollectionEditorModelListener createCollectionEditorModelListener()
    {
        return (CollectionEditorModelListener)new DefaultCollectionEditorModelListener(this.model, this.collectionEditor, this.editorListener);
    }


    public void unregisterListeners()
    {
        if(this.collectionEditor != null)
        {
            this.collectionEditor.setCollectionControler(null);
        }
        if(this.model != null)
        {
            this.model.removeCollectionEditorModelListener(this.modelListener);
        }
    }
}
