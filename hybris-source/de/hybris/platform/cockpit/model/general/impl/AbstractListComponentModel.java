package de.hybris.platform.cockpit.model.general.impl;

import de.hybris.platform.cockpit.model.general.ListComponentModelListener;
import de.hybris.platform.cockpit.model.general.MutableListModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractListComponentModel implements MutableListModel
{
    protected List<ListComponentModelListener> listeners = new ArrayList<>();
    protected Map<TypedObject, Set<TypedObject>> updateNotificationMap = new HashMap<>();


    public void addListComponentModelListener(ListComponentModelListener listener)
    {
        if(!this.listeners.contains(listener))
        {
            this.listeners.add(listener);
        }
    }


    public void removeListComponentModelListener(ListComponentModelListener listener)
    {
        this.listeners.remove(listener);
    }


    protected void fireItemsActivated(Collection<TypedObject> items)
    {
        List<ListComponentModelListener> lmListeners = new LinkedList<>(this.listeners);
        for(ListComponentModelListener listener : lmListeners)
        {
            listener.activationChanged(items);
        }
    }


    protected void fireSelectionChanged(List<Integer> indexes)
    {
        List<ListComponentModelListener> lmListeners = new LinkedList<>(this.listeners);
        for(ListComponentModelListener listener : lmListeners)
        {
            listener.selectionChanged(indexes);
        }
    }


    protected void fireMoved(int fromIndex, int toIndex)
    {
        List<ListComponentModelListener> lmListeners = new LinkedList<>(this.listeners);
        for(ListComponentModelListener listener : lmListeners)
        {
            listener.itemMoved(fromIndex, toIndex);
        }
    }


    protected void fireRemoved(Collection<Integer> indexes)
    {
        List<ListComponentModelListener> lmListeners = new LinkedList<>(this.listeners);
        for(ListComponentModelListener listener : lmListeners)
        {
            listener.itemsRemoved(indexes);
        }
    }


    protected void fireItemsChanged()
    {
        List<ListComponentModelListener> lmListeners = new LinkedList<>(this.listeners);
        for(ListComponentModelListener listener : lmListeners)
        {
            listener.itemsChanged();
        }
    }


    protected void fireChanged()
    {
        List<ListComponentModelListener> lmListeners = new LinkedList<>(this.listeners);
        for(ListComponentModelListener listener : lmListeners)
        {
            listener.changed();
        }
    }


    public boolean isForceRenderOnSelectionChanged()
    {
        return false;
    }


    public void fireEvent(String eventName, Object value)
    {
        List<ListComponentModelListener> lmListeners = new LinkedList<>(this.listeners);
        for(ListComponentModelListener listener : lmListeners)
        {
            listener.onEvent(eventName, value);
        }
    }


    public void addToAdditionalItemChangeUpdateNotificationMap(TypedObject itemToUpdate, Collection<TypedObject> changedItems)
    {
        for(TypedObject typedObject : changedItems)
        {
            Set<TypedObject> set = this.updateNotificationMap.get(typedObject);
            if(set == null)
            {
                set = new HashSet<>();
                this.updateNotificationMap.put(typedObject, set);
            }
            set.add(itemToUpdate);
        }
    }


    public Collection<TypedObject> getAdditionalItemsToUpdate(TypedObject changedItem)
    {
        Set<TypedObject> set = this.updateNotificationMap.get(changedItem);
        return (set == null) ? Collections.EMPTY_SET : set;
    }
}
