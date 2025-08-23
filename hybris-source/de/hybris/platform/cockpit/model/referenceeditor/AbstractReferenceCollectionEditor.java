package de.hybris.platform.cockpit.model.referenceeditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.zkoss.zul.Div;

@Deprecated
public abstract class AbstractReferenceCollectionEditor extends Div implements UIReferenceCollectionEditor
{
    protected List<ReferenceCollectionEditorListener> listeners = new ArrayList<>();


    public void removeReferenceCollectionEditorListener(ReferenceCollectionEditorListener collectionListener)
    {
        if(this.listeners.contains(collectionListener))
        {
            this.listeners.remove(collectionListener);
        }
    }


    public void addReferenceCollectionEditorListener(ReferenceCollectionEditorListener collectionEditor)
    {
        if(!this.listeners.contains(collectionEditor))
        {
            this.listeners.add(collectionEditor);
        }
    }


    protected void fireAddCollectionItem(Object item)
    {
        for(ReferenceCollectionEditorListener listener : this.listeners)
        {
            listener.addCollectionItem(item);
        }
    }


    protected void fireAddCollectionItems(List<Object> items)
    {
        for(ReferenceCollectionEditorListener listener : this.listeners)
        {
            listener.addCollectionItems(items);
        }
    }


    protected void fireRemoveCollectionItem(int index)
    {
        for(ReferenceCollectionEditorListener listener : this.listeners)
        {
            listener.removeCollectionItem(index);
        }
    }


    protected void fireRemoveCollectionItem(Object item)
    {
        for(ReferenceCollectionEditorListener listener : this.listeners)
        {
            listener.removeCollectionItem(item);
        }
    }


    protected void fireRemoveCollectionItems(Collection indexes)
    {
        for(ReferenceCollectionEditorListener listener : this.listeners)
        {
            listener.removeCollectionItems(indexes);
        }
    }


    protected void fireClearCollectionItems()
    {
        for(ReferenceCollectionEditorListener listener : this.listeners)
        {
            listener.clearCollectionItems();
        }
    }


    protected void fireMoveCollectionItem(int fromIndex, int toIndex)
    {
        for(ReferenceCollectionEditorListener listener : this.listeners)
        {
            listener.moveCollectionItem(fromIndex, toIndex);
        }
    }
}
