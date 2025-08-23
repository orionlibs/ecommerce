package de.hybris.platform.catalog.model.synchronization;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobHistoryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class CatalogVersionSyncCronJobHistoryModel extends CronJobHistoryModel
{
    public static final String _TYPECODE = "CatalogVersionSyncCronJobHistory";
    public static final String PROCESSEDITEMSCOUNT = "processedItemsCount";
    public static final String SCHEDULEDITEMSCOUNT = "scheduledItemsCount";
    public static final String DUMPEDITEMSCOUNT = "dumpedItemsCount";
    public static final String FULLSYNC = "fullSync";


    public CatalogVersionSyncCronJobHistoryModel()
    {
    }


    public CatalogVersionSyncCronJobHistoryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CatalogVersionSyncCronJobHistoryModel(String _cronJobCode, String _jobCode, int _nodeID, Date _startTime)
    {
        setCronJobCode(_cronJobCode);
        setJobCode(_jobCode);
        setNodeID(_nodeID);
        setStartTime(_startTime);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CatalogVersionSyncCronJobHistoryModel(String _cronJobCode, String _jobCode, int _nodeID, ItemModel _owner, Date _startTime)
    {
        setCronJobCode(_cronJobCode);
        setJobCode(_jobCode);
        setNodeID(_nodeID);
        setOwner(_owner);
        setStartTime(_startTime);
    }


    @Accessor(qualifier = "dumpedItemsCount", type = Accessor.Type.GETTER)
    public Integer getDumpedItemsCount()
    {
        return (Integer)getPersistenceContext().getPropertyValue("dumpedItemsCount");
    }


    @Accessor(qualifier = "fullSync", type = Accessor.Type.GETTER)
    public Boolean getFullSync()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("fullSync");
    }


    @Accessor(qualifier = "processedItemsCount", type = Accessor.Type.GETTER)
    public Integer getProcessedItemsCount()
    {
        return (Integer)getPersistenceContext().getPropertyValue("processedItemsCount");
    }


    @Accessor(qualifier = "scheduledItemsCount", type = Accessor.Type.GETTER)
    public Integer getScheduledItemsCount()
    {
        return (Integer)getPersistenceContext().getPropertyValue("scheduledItemsCount");
    }


    @Accessor(qualifier = "dumpedItemsCount", type = Accessor.Type.SETTER)
    public void setDumpedItemsCount(Integer value)
    {
        getPersistenceContext().setPropertyValue("dumpedItemsCount", value);
    }


    @Accessor(qualifier = "fullSync", type = Accessor.Type.SETTER)
    public void setFullSync(Boolean value)
    {
        getPersistenceContext().setPropertyValue("fullSync", value);
    }


    @Accessor(qualifier = "processedItemsCount", type = Accessor.Type.SETTER)
    public void setProcessedItemsCount(Integer value)
    {
        getPersistenceContext().setPropertyValue("processedItemsCount", value);
    }


    @Accessor(qualifier = "scheduledItemsCount", type = Accessor.Type.SETTER)
    public void setScheduledItemsCount(Integer value)
    {
        getPersistenceContext().setPropertyValue("scheduledItemsCount", value);
    }
}
