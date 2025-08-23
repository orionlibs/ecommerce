package de.hybris.platform.cmsfacades.data;

import de.hybris.platform.core.model.ItemModel;
import java.io.Serializable;

public class SyncItemInfoJobStatusData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private ItemModel item;
    private String syncStatus;


    public void setItem(ItemModel item)
    {
        this.item = item;
    }


    public ItemModel getItem()
    {
        return this.item;
    }


    public void setSyncStatus(String syncStatus)
    {
        this.syncStatus = syncStatus;
    }


    public String getSyncStatus()
    {
        return this.syncStatus;
    }
}
