package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.referenceeditor.ReferenceCollectionEditorModelListener;
import de.hybris.platform.cockpit.model.referenceeditor.UIReferenceCollectionEditor;
import java.util.ArrayList;

public class DefaultReferenceCollectionEditorModelListener implements ReferenceCollectionEditorModelListener
{
    private final DefaultReferenceCollectionEditorModel model;
    private final UIReferenceCollectionEditor collectionEditor;
    private final EditorListener editorListener;


    public DefaultReferenceCollectionEditorModelListener(DefaultReferenceCollectionEditorModel model, UIReferenceCollectionEditor collectionEditor, EditorListener editorListener)
    {
        this.model = model;
        this.collectionEditor = collectionEditor;
        this.editorListener = editorListener;
    }


    public void changed()
    {
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
