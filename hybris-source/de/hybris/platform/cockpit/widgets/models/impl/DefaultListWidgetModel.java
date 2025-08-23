package de.hybris.platform.cockpit.widgets.models.impl;

import de.hybris.platform.cockpit.widgets.models.ListWidgetModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultListWidgetModel<E> extends AbstractWidgetModel implements ListWidgetModel<E>
{
    private final List<E> items = new ArrayList<>();


    public List<E> getItems()
    {
        return Collections.unmodifiableList(this.items);
    }


    public boolean setItems(List<E> items)
    {
        boolean changed = false;
        if(!this.items.equals(items))
        {
            this.items.clear();
            this.items.addAll(items);
            changed = true;
        }
        return changed;
    }
}
