package de.hybris.platform.cmsfacades.data;

import de.hybris.platform.core.model.ItemModel;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SynchronizationItemDetailsData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private ItemModel item;
    private String catalogId;
    private String sourceVersionId;
    private String targetVersionId;
    private String syncStatus;
    private Date lastSyncStatusDate;
    private List<SyncItemInfoJobStatusData> relatedItemStatuses;


    public void setItem(ItemModel item)
    {
        this.item = item;
    }


    public ItemModel getItem()
    {
        return this.item;
    }


    public void setCatalogId(String catalogId)
    {
        this.catalogId = catalogId;
    }


    public String getCatalogId()
    {
        return this.catalogId;
    }


    public void setSourceVersionId(String sourceVersionId)
    {
        this.sourceVersionId = sourceVersionId;
    }


    public String getSourceVersionId()
    {
        return this.sourceVersionId;
    }


    public void setTargetVersionId(String targetVersionId)
    {
        this.targetVersionId = targetVersionId;
    }


    public String getTargetVersionId()
    {
        return this.targetVersionId;
    }


    public void setSyncStatus(String syncStatus)
    {
        this.syncStatus = syncStatus;
    }


    public String getSyncStatus()
    {
        return this.syncStatus;
    }


    public void setLastSyncStatusDate(Date lastSyncStatusDate)
    {
        this.lastSyncStatusDate = lastSyncStatusDate;
    }


    public Date getLastSyncStatusDate()
    {
        return this.lastSyncStatusDate;
    }


    public void setRelatedItemStatuses(List<SyncItemInfoJobStatusData> relatedItemStatuses)
    {
        this.relatedItemStatuses = relatedItemStatuses;
    }


    public List<SyncItemInfoJobStatusData> getRelatedItemStatuses()
    {
        return this.relatedItemStatuses;
    }
}
