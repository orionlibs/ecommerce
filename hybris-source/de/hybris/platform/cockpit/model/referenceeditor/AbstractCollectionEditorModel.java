package de.hybris.platform.cockpit.model.referenceeditor;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCollectionEditorModel implements CollectionEditorModel
{
    protected final List<ReferenceCollectionEditorModelListener> listeners = new ArrayList<>();


    public List<ReferenceCollectionEditorModelListener> getListeners()
    {
        return this.listeners;
    }


    protected void fireCollectionItemsChanged()
    {
        for(ReferenceCollectionEditorModelListener listener : this.listeners)
        {
            listener.collectionItemsChanged();
        }
    }


    protected void fireChanged()
    {
        for(ReferenceCollectionEditorModelListener listener : this.listeners)
        {
            listener.changed();
        }
    }


    public void addCollectionEditorModelListener(ReferenceCollectionEditorModelListener modelListener)
    {
        if(!this.listeners.contains(modelListener))
        {
            this.listeners.add(modelListener);
        }
    }


    public void removeCollectionEditorModelListener(ReferenceCollectionEditorModelListener modelListener)
    {
        if(this.listeners.contains(modelListener))
        {
            this.listeners.remove(modelListener);
        }
    }


    protected void fireRootTypeChanged()
    {
        for(ReferenceCollectionEditorModelListener listener : this.listeners)
        {
            listener.rootTypeChanged();
        }
    }


    protected void fireRootSearchTypeChanged()
    {
        for(ReferenceCollectionEditorModelListener listener : this.listeners)
        {
            listener.rootSearchTypeChanged();
        }
    }
}
