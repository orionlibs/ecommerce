package de.hybris.platform.cockpit.model.referenceeditor.collection.model;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.referenceeditor.collection.AbstractCollectionEditor;
import java.util.ArrayList;

public class DefaultCollectionEditorModelListener implements CollectionEditorModelListener
{
    private final DefaultCollectionEditorModel model;
    private final AbstractCollectionEditor collectionEditor;
    private final EditorListener editorListener;


    public DefaultCollectionEditorModelListener(DefaultCollectionEditorModel model, AbstractCollectionEditor collectionEditor, EditorListener editorListener)
    {
        this.model = model;
        this.collectionEditor = collectionEditor;
        this.editorListener = editorListener;
    }


    public void collectionItemsChanged()
    {
        this.collectionEditor.updateCollectionItems();
        if(this.editorListener != null)
        {
            this.editorListener.valueChanged(new ArrayList(this.model.getCollectionItems()));
        }
    }


    public void rootSearchTypeChanged()
    {
        this.collectionEditor.updateRootSearchTypeChanged();
    }


    public void rootTypeChanged()
    {
        this.collectionEditor.updateRootTypeChanged();
    }
}
