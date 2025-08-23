package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.List;

public class SyncItemStatusData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String itemId;
    private String itemType;
    private String name;
    private String status;
    private Long lastSyncStatus;
    private Long lastModifiedDate;
    private String catalogVersionUuid;
    private List<ItemTypeData> dependentItemTypesOutOfSync;
    private List<SyncItemStatusData> selectedDependencies;
    private List<SyncItemStatusData> sharedDependencies;
    private List<SyncItemStatusData> unavailableDependencies;


    public void setItemId(String itemId)
    {
        this.itemId = itemId;
    }


    public String getItemId()
    {
        return this.itemId;
    }


    public void setItemType(String itemType)
    {
        this.itemType = itemType;
    }


    public String getItemType()
    {
        return this.itemType;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setStatus(String status)
    {
        this.status = status;
    }


    public String getStatus()
    {
        return this.status;
    }


    public void setLastSyncStatus(Long lastSyncStatus)
    {
        this.lastSyncStatus = lastSyncStatus;
    }


    public Long getLastSyncStatus()
    {
        return this.lastSyncStatus;
    }


    public void setLastModifiedDate(Long lastModifiedDate)
    {
        this.lastModifiedDate = lastModifiedDate;
    }


    public Long getLastModifiedDate()
    {
        return this.lastModifiedDate;
    }


    public void setCatalogVersionUuid(String catalogVersionUuid)
    {
        this.catalogVersionUuid = catalogVersionUuid;
    }


    public String getCatalogVersionUuid()
    {
        return this.catalogVersionUuid;
    }


    public void setDependentItemTypesOutOfSync(List<ItemTypeData> dependentItemTypesOutOfSync)
    {
        this.dependentItemTypesOutOfSync = dependentItemTypesOutOfSync;
    }


    public List<ItemTypeData> getDependentItemTypesOutOfSync()
    {
        return this.dependentItemTypesOutOfSync;
    }


    public void setSelectedDependencies(List<SyncItemStatusData> selectedDependencies)
    {
        this.selectedDependencies = selectedDependencies;
    }


    public List<SyncItemStatusData> getSelectedDependencies()
    {
        return this.selectedDependencies;
    }


    public void setSharedDependencies(List<SyncItemStatusData> sharedDependencies)
    {
        this.sharedDependencies = sharedDependencies;
    }


    public List<SyncItemStatusData> getSharedDependencies()
    {
        return this.sharedDependencies;
    }


    public void setUnavailableDependencies(List<SyncItemStatusData> unavailableDependencies)
    {
        this.unavailableDependencies = unavailableDependencies;
    }


    public List<SyncItemStatusData> getUnavailableDependencies()
    {
        return this.unavailableDependencies;
    }
}
