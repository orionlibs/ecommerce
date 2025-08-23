package de.hybris.platform.cockpit.model.referenceeditor.collection.model;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCollectionEditorModel implements CollectionEditorModel
{
    protected final List<CollectionEditorModelListener> listeners = new ArrayList<>();


    public List<CollectionEditorModelListener> getListeners()
    {
        return this.listeners;
    }


    protected void fireCollectionItemsChanged()
    {
        for(CollectionEditorModelListener listener : this.listeners)
        {
            listener.collectionItemsChanged();
        }
    }


    public void addCollectionEditorModelListener(CollectionEditorModelListener modelListener)
    {
        if(!this.listeners.contains(modelListener))
        {
            this.listeners.add(modelListener);
        }
    }


    public void removeCollectionEditorModelListener(CollectionEditorModelListener modelListener)
    {
        if(this.listeners.contains(modelListener))
        {
            this.listeners.remove(modelListener);
        }
    }


    protected void fireRootTypeChanged()
    {
        for(CollectionEditorModelListener listener : this.listeners)
        {
            listener.rootTypeChanged();
        }
    }


    protected void fireRootSearchTypeChanged()
    {
        for(CollectionEditorModelListener listener : this.listeners)
        {
            listener.rootSearchTypeChanged();
        }
    }
}
