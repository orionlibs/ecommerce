package de.hybris.platform.core.model.util;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class ModifiedCatalogItemsViewModel extends ItemModel
{
    public static final String _TYPECODE = "ModifiedCatalogItemsView";
    public static final String JOB = "job";
    public static final String SOURCEITEM = "sourceItem";
    public static final String TARGETITEM = "targetItem";
    public static final String SOURCEMODIFIEDTIME = "sourceModifiedTime";
    public static final String LASTSYNCTIME = "lastSyncTime";
    public static final String TYPECODE = "typeCode";


    public ModifiedCatalogItemsViewModel()
    {
    }


    public ModifiedCatalogItemsViewModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ModifiedCatalogItemsViewModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "job", type = Accessor.Type.GETTER)
    public SyncItemJobModel getJob()
    {
        return (SyncItemJobModel)getPersistenceContext().getPropertyValue("job");
    }


    @Accessor(qualifier = "lastSyncTime", type = Accessor.Type.GETTER)
    public Date getLastSyncTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("lastSyncTime");
    }


    @Accessor(qualifier = "sourceItem", type = Accessor.Type.GETTER)
    public ItemModel getSourceItem()
    {
        return (ItemModel)getPersistenceContext().getPropertyValue("sourceItem");
    }


    @Accessor(qualifier = "sourceModifiedTime", type = Accessor.Type.GETTER)
    public Date getSourceModifiedTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("sourceModifiedTime");
    }


    @Accessor(qualifier = "targetItem", type = Accessor.Type.GETTER)
    public ItemModel getTargetItem()
    {
        return (ItemModel)getPersistenceContext().getPropertyValue("targetItem");
    }


    @Accessor(qualifier = "typeCode", type = Accessor.Type.GETTER)
    public String getTypeCode()
    {
        return (String)getPersistenceContext().getPropertyValue("typeCode");
    }
}
