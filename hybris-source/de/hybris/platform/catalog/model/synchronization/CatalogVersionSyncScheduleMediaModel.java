package de.hybris.platform.catalog.model.synchronization;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CatalogVersionSyncScheduleMediaModel extends MediaModel
{
    public static final String _TYPECODE = "CatalogVersionSyncScheduleMedia";
    public static final String _SYNCJOBSCHEDULEMEDIARELATION = "SyncJobScheduleMediaRelation";
    public static final String SCHEDULEDCOUNT = "scheduledCount";
    public static final String CRONJOBPOS = "cronjobPOS";
    public static final String CRONJOB = "cronjob";


    public CatalogVersionSyncScheduleMediaModel()
    {
    }


    public CatalogVersionSyncScheduleMediaModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CatalogVersionSyncScheduleMediaModel(CatalogVersionModel _catalogVersion, String _code, CatalogVersionSyncCronJobModel _cronjob)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setCronjob(_cronjob);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CatalogVersionSyncScheduleMediaModel(CatalogVersionModel _catalogVersion, String _code, CatalogVersionSyncCronJobModel _cronjob, ItemModel _owner)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setCronjob(_cronjob);
        setOwner(_owner);
    }


    @Accessor(qualifier = "cronjob", type = Accessor.Type.GETTER)
    public CatalogVersionSyncCronJobModel getCronjob()
    {
        return (CatalogVersionSyncCronJobModel)getPersistenceContext().getPropertyValue("cronjob");
    }


    @Accessor(qualifier = "scheduledCount", type = Accessor.Type.GETTER)
    public Integer getScheduledCount()
    {
        return (Integer)getPersistenceContext().getPropertyValue("scheduledCount");
    }


    @Accessor(qualifier = "cronjob", type = Accessor.Type.SETTER)
    public void setCronjob(CatalogVersionSyncCronJobModel value)
    {
        getPersistenceContext().setPropertyValue("cronjob", value);
    }


    @Accessor(qualifier = "scheduledCount", type = Accessor.Type.SETTER)
    public void setScheduledCount(Integer value)
    {
        getPersistenceContext().setPropertyValue("scheduledCount", value);
    }
}
