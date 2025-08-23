package de.hybris.platform.catalog.model.synchronization;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.SyncItemCronJobModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobHistoryModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

public class CatalogVersionSyncCronJobModel extends SyncItemCronJobModel
{
    public static final String _TYPECODE = "CatalogVersionSyncCronJob";
    public static final String STATUSMESSAGE = "statusMessage";
    public static final String SCHEDULEMEDIAS = "scheduleMedias";


    public CatalogVersionSyncCronJobModel()
    {
    }


    public CatalogVersionSyncCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CatalogVersionSyncCronJobModel(SyncItemJobModel _job)
    {
        setJob((JobModel)_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CatalogVersionSyncCronJobModel(SyncItemJobModel _job, ItemModel _owner)
    {
        setJob((JobModel)_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "activeCronJobHistory", type = Accessor.Type.GETTER)
    public CatalogVersionSyncCronJobHistoryModel getActiveCronJobHistory()
    {
        return (CatalogVersionSyncCronJobHistoryModel)super.getActiveCronJobHistory();
    }


    @Accessor(qualifier = "scheduleMedias", type = Accessor.Type.GETTER)
    public List<CatalogVersionSyncScheduleMediaModel> getScheduleMedias()
    {
        return (List<CatalogVersionSyncScheduleMediaModel>)getPersistenceContext().getPropertyValue("scheduleMedias");
    }


    @Accessor(qualifier = "statusMessage", type = Accessor.Type.GETTER)
    public String getStatusMessage()
    {
        return (String)getPersistenceContext().getPropertyValue("statusMessage");
    }


    @Accessor(qualifier = "activeCronJobHistory", type = Accessor.Type.SETTER)
    public void setActiveCronJobHistory(CronJobHistoryModel value)
    {
        if(value == null || value instanceof CatalogVersionSyncCronJobHistoryModel)
        {
            super.setActiveCronJobHistory(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncCronJobHistoryModel");
        }
    }


    @Accessor(qualifier = "scheduleMedias", type = Accessor.Type.SETTER)
    public void setScheduleMedias(List<CatalogVersionSyncScheduleMediaModel> value)
    {
        getPersistenceContext().setPropertyValue("scheduleMedias", value);
    }


    @Accessor(qualifier = "statusMessage", type = Accessor.Type.SETTER)
    public void setStatusMessage(String value)
    {
        getPersistenceContext().setPropertyValue("statusMessage", value);
    }
}
