package de.hybris.platform.cockpit.widgets.models.impl;

import de.hybris.platform.cockpit.widgets.models.ItemWidgetModel;

public class DefaultItemWidgetModel<T> extends AbstractWidgetModel implements ItemWidgetModel<T>
{
    private T item;


    public T getItem()
    {
        return this.item;
    }


    public boolean setItem(T item)
    {
        boolean changed = false;
        if((this.item != null && !this.item.equals(item)) || (this.item == null && item != null))
        {
            this.item = item;
            changed = true;
        }
        return changed;
    }
}
