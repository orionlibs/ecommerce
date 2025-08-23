package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.List;

public class SynchronizationData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<ItemSynchronizationData> items;


    public void setItems(List<ItemSynchronizationData> items)
    {
        this.items = items;
    }


    public List<ItemSynchronizationData> getItems()
    {
        return this.items;
    }
}
