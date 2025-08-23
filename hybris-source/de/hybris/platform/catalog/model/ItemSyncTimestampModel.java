package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;

public class ItemSyncTimestampModel extends ItemModel
{
    public static final String _TYPECODE = "ItemSyncTimestamp";
    public static final String SYNCJOB = "syncJob";
    public static final String SOURCEITEM = "sourceItem";
    public static final String TARGETITEM = "targetItem";
    public static final String SOURCEVERSION = "sourceVersion";
    public static final String TARGETVERSION = "targetVersion";
    public static final String LASTSYNCSOURCEMODIFIEDTIME = "lastSyncSourceModifiedTime";
    public static final String LASTSYNCTIME = "lastSyncTime";
    public static final String PENDINGATTRIBUTESOWNERJOB = "pendingAttributesOwnerJob";
    public static final String PENDINGATTRIBUTESSCHEDULEDTURN = "pendingAttributesScheduledTurn";
    public static final String PENDINGATTRIBUTEQUALIFIERS = "pendingAttributeQualifiers";
    public static final String OUTDATED = "outdated";
    public static final String PENDINGATTRIBUTES = "pendingAttributes";


    public ItemSyncTimestampModel()
    {
    }


    public ItemSyncTimestampModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ItemSyncTimestampModel(ItemModel _sourceItem, CatalogVersionModel _sourceVersion, ItemModel _targetItem, CatalogVersionModel _targetVersion)
    {
        setSourceItem(_sourceItem);
        setSourceVersion(_sourceVersion);
        setTargetItem(_targetItem);
        setTargetVersion(_targetVersion);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ItemSyncTimestampModel(ItemModel _owner, ItemModel _sourceItem, CatalogVersionModel _sourceVersion, SyncItemJobModel _syncJob, ItemModel _targetItem, CatalogVersionModel _targetVersion)
    {
        setOwner(_owner);
        setSourceItem(_sourceItem);
        setSourceVersion(_sourceVersion);
        setSyncJob(_syncJob);
        setTargetItem(_targetItem);
        setTargetVersion(_targetVersion);
    }


    @Accessor(qualifier = "lastSyncSourceModifiedTime", type = Accessor.Type.GETTER)
    public Date getLastSyncSourceModifiedTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("lastSyncSourceModifiedTime");
    }


    @Accessor(qualifier = "lastSyncTime", type = Accessor.Type.GETTER)
    public Date getLastSyncTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("lastSyncTime");
    }


    @Accessor(qualifier = "outdated", type = Accessor.Type.GETTER)
    public Boolean getOutdated()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("outdated");
    }


    @Accessor(qualifier = "pendingAttributeQualifiers", type = Accessor.Type.GETTER)
    public String getPendingAttributeQualifiers()
    {
        return (String)getPersistenceContext().getPropertyValue("pendingAttributeQualifiers");
    }


    @Accessor(qualifier = "pendingAttributes", type = Accessor.Type.GETTER)
    public Collection<AttributeDescriptorModel> getPendingAttributes()
    {
        return (Collection<AttributeDescriptorModel>)getPersistenceContext().getPropertyValue("pendingAttributes");
    }


    @Accessor(qualifier = "pendingAttributesOwnerJob", type = Accessor.Type.GETTER)
    public SyncItemCronJobModel getPendingAttributesOwnerJob()
    {
        return (SyncItemCronJobModel)getPersistenceContext().getPropertyValue("pendingAttributesOwnerJob");
    }


    @Accessor(qualifier = "pendingAttributesScheduledTurn", type = Accessor.Type.GETTER)
    public Integer getPendingAttributesScheduledTurn()
    {
        return (Integer)getPersistenceContext().getPropertyValue("pendingAttributesScheduledTurn");
    }


    @Accessor(qualifier = "sourceItem", type = Accessor.Type.GETTER)
    public ItemModel getSourceItem()
    {
        return (ItemModel)getPersistenceContext().getPropertyValue("sourceItem");
    }


    @Accessor(qualifier = "sourceVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getSourceVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("sourceVersion");
    }


    @Accessor(qualifier = "syncJob", type = Accessor.Type.GETTER)
    public SyncItemJobModel getSyncJob()
    {
        return (SyncItemJobModel)getPersistenceContext().getPropertyValue("syncJob");
    }


    @Accessor(qualifier = "targetItem", type = Accessor.Type.GETTER)
    public ItemModel getTargetItem()
    {
        return (ItemModel)getPersistenceContext().getPropertyValue("targetItem");
    }


    @Accessor(qualifier = "targetVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getTargetVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("targetVersion");
    }


    @Accessor(qualifier = "lastSyncSourceModifiedTime", type = Accessor.Type.SETTER)
    public void setLastSyncSourceModifiedTime(Date value)
    {
        getPersistenceContext().setPropertyValue("lastSyncSourceModifiedTime", value);
    }


    @Accessor(qualifier = "lastSyncTime", type = Accessor.Type.SETTER)
    public void setLastSyncTime(Date value)
    {
        getPersistenceContext().setPropertyValue("lastSyncTime", value);
    }


    @Accessor(qualifier = "pendingAttributeQualifiers", type = Accessor.Type.SETTER)
    public void setPendingAttributeQualifiers(String value)
    {
        getPersistenceContext().setPropertyValue("pendingAttributeQualifiers", value);
    }


    @Accessor(qualifier = "pendingAttributesOwnerJob", type = Accessor.Type.SETTER)
    public void setPendingAttributesOwnerJob(SyncItemCronJobModel value)
    {
        getPersistenceContext().setPropertyValue("pendingAttributesOwnerJob", value);
    }


    @Accessor(qualifier = "pendingAttributesScheduledTurn", type = Accessor.Type.SETTER)
    public void setPendingAttributesScheduledTurn(Integer value)
    {
        getPersistenceContext().setPropertyValue("pendingAttributesScheduledTurn", value);
    }


    @Accessor(qualifier = "sourceItem", type = Accessor.Type.SETTER)
    public void setSourceItem(ItemModel value)
    {
        getPersistenceContext().setPropertyValue("sourceItem", value);
    }


    @Accessor(qualifier = "sourceVersion", type = Accessor.Type.SETTER)
    public void setSourceVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("sourceVersion", value);
    }


    @Accessor(qualifier = "syncJob", type = Accessor.Type.SETTER)
    public void setSyncJob(SyncItemJobModel value)
    {
        getPersistenceContext().setPropertyValue("syncJob", value);
    }


    @Accessor(qualifier = "targetItem", type = Accessor.Type.SETTER)
    public void setTargetItem(ItemModel value)
    {
        getPersistenceContext().setPropertyValue("targetItem", value);
    }


    @Accessor(qualifier = "targetVersion", type = Accessor.Type.SETTER)
    public void setTargetVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("targetVersion", value);
    }
}
