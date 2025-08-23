package de.hybris.platform.cockpit.model.referenceeditor;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.zkoss.zul.Div;

public abstract class AbstractReferenceSelector extends Div implements UIReferenceSelector
{
    protected List<ReferenceSelectorListener> listeners = new ArrayList<>();


    public void addReferenceSelectorListener(ReferenceSelectorListener referenceSelectorListener)
    {
        if(!this.listeners.contains(referenceSelectorListener))
        {
            this.listeners.add(referenceSelectorListener);
        }
    }


    public void fireCancel()
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.cancel();
        }
    }


    public void removeReferenceSelectorListener(ReferenceSelectorListener referenceSelectorListener)
    {
        if(this.listeners.contains(referenceSelectorListener))
        {
            this.listeners.remove(referenceSelectorListener);
        }
    }


    protected void fireAbortAndCloseAdvancedMode()
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.abortAndCloseAdvancedMode();
        }
    }


    protected void fireAddItem(Object item)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.addItem(item);
        }
    }


    protected void fireAddItems(List<Object> items)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.addItems(items);
        }
    }


    protected void fireAddTemporaryItem(Object item)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.addTemporaryItem(item);
        }
    }


    protected void fireAddTemporaryItems(Collection temporaryItems)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.addTemporaryItems(temporaryItems);
        }
    }


    protected void fireAddToNotConfirmedItems(Collection items)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.addToNotConfirmedItems(items);
        }
    }


    protected void fireAutoCompleteSearch(String searchTerm)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.triggerAutoCompleteSearch(searchTerm);
        }
    }


    protected void fireChangeMode(SelectorModel.Mode mode)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.changeMode(mode);
        }
    }


    protected void fireClearTemporaryItems()
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.clearTemporaryItems();
        }
    }


    protected void fireConfirmAndCloseAdvancedMode()
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.confirmAndCloseAdvancedMode();
        }
    }


    protected void fireDeselectItem(Object item)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.deselectItem(item);
        }
    }


    protected void fireDeselectTemporaryItem(Object item)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.deselectTemporaryItem(item);
        }
    }


    protected void fireDeselectTemporaryItems()
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.deselectTemporaryItems();
        }
    }


    protected void fireMoveItem(int fromIndex, int toIndex)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.moveItem(fromIndex, toIndex);
        }
    }


    protected void fireMoveTemporaryItem(int fromIndex, int toIndex)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.moveTemporaryItem(fromIndex, toIndex);
        }
    }


    protected void fireRemoveItem(int index)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.removeItem(index);
        }
    }


    protected void fireRemoveItems(Collection indexes)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.removeItems(indexes);
        }
    }


    protected void fireRemoveTemporaryItem(int index)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.removeTemporaryItem(index);
        }
    }


    protected void fireRemoveTemporaryItem(Object item)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.removeTemporaryItem(item);
        }
    }


    protected void fireSaveActualItems()
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.saveActualItems();
        }
    }


    protected void fireEnterPressed()
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.doEnterPressed();
        }
    }


    public void fireOpenReferencedItem(TypedObject item)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.doOpenReferencedItem(item);
        }
    }


    protected void fireSearch(String searchTerm)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.triggerSearch(searchTerm);
        }
    }


    protected void fireSelectItem(Object item)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.selectItem(item);
        }
    }


    protected void fireSelectItems(Collection items)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.selectItems(items);
        }
    }


    protected void fireSelectorAdvancedMode()
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.selectorAdvancedMode();
        }
    }


    protected void fireSelectorNormalMode()
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.selectorNormaMode();
        }
    }


    protected void fireSelectTemporaryItem(Object item)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.selectTemporaryItem(item);
        }
    }


    protected void fireSelectTemporaryItems(Collection items)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.selectTemporaryItems(items);
        }
    }


    protected void fireShowItemEditorPopup(CreateContext context)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.showAddItemPopupEditor(context);
        }
    }


    protected void fireShowItemEditorPopup(ObjectType type, TypedObject sourceItem, CreateContext context)
    {
        for(ReferenceSelectorListener listener : this.listeners)
        {
            listener.showAddItemPopupEditor(type, sourceItem, context);
        }
    }
}
