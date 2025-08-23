package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class SyncItemCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "SyncItemCronJob";
    public static final String _JOBCRONJOBRELATION = "JobCronJobRelation";
    public static final String FORCEUPDATE = "forceUpdate";
    public static final String PENDINGITEMS = "pendingItems";
    public static final String FINISHEDITEMS = "finishedItems";
    public static final String CREATESAVEDVALUES = "createSavedValues";
    public static final String FULLSYNC = "fullSync";
    public static final String ABORTONCOLLIDINGSYNC = "abortOnCollidingSync";


    public SyncItemCronJobModel()
    {
    }


    public SyncItemCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SyncItemCronJobModel(SyncItemJobModel _job)
    {
        setJob((JobModel)_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SyncItemCronJobModel(SyncItemJobModel _job, ItemModel _owner)
    {
        setJob((JobModel)_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "abortOnCollidingSync", type = Accessor.Type.GETTER)
    public Boolean getAbortOnCollidingSync()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("abortOnCollidingSync");
    }


    @Accessor(qualifier = "createSavedValues", type = Accessor.Type.GETTER)
    public Boolean getCreateSavedValues()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("createSavedValues");
    }


    @Accessor(qualifier = "finishedItems", type = Accessor.Type.GETTER)
    public Collection<ItemModel> getFinishedItems()
    {
        return (Collection<ItemModel>)getPersistenceContext().getPropertyValue("finishedItems");
    }


    @Accessor(qualifier = "forceUpdate", type = Accessor.Type.GETTER)
    public Boolean getForceUpdate()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("forceUpdate");
    }


    @Accessor(qualifier = "fullSync", type = Accessor.Type.GETTER)
    public Boolean getFullSync()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("fullSync");
    }


    @Accessor(qualifier = "job", type = Accessor.Type.GETTER)
    public SyncItemJobModel getJob()
    {
        return (SyncItemJobModel)super.getJob();
    }


    @Accessor(qualifier = "pendingItems", type = Accessor.Type.GETTER)
    public Collection<ItemModel> getPendingItems()
    {
        return (Collection<ItemModel>)getPersistenceContext().getPropertyValue("pendingItems");
    }


    @Accessor(qualifier = "abortOnCollidingSync", type = Accessor.Type.SETTER)
    public void setAbortOnCollidingSync(Boolean value)
    {
        getPersistenceContext().setPropertyValue("abortOnCollidingSync", value);
    }


    @Accessor(qualifier = "createSavedValues", type = Accessor.Type.SETTER)
    public void setCreateSavedValues(Boolean value)
    {
        getPersistenceContext().setPropertyValue("createSavedValues", value);
    }


    @Accessor(qualifier = "forceUpdate", type = Accessor.Type.SETTER)
    public void setForceUpdate(Boolean value)
    {
        getPersistenceContext().setPropertyValue("forceUpdate", value);
    }


    @Accessor(qualifier = "fullSync", type = Accessor.Type.SETTER)
    public void setFullSync(Boolean value)
    {
        getPersistenceContext().setPropertyValue("fullSync", value);
    }


    @Accessor(qualifier = "job", type = Accessor.Type.SETTER)
    public void setJob(JobModel value)
    {
        if(value == null || value instanceof SyncItemJobModel)
        {
            super.setJob(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.catalog.model.SyncItemJobModel");
        }
    }


    @Accessor(qualifier = "pendingItems", type = Accessor.Type.SETTER)
    public void setPendingItems(Collection<ItemModel> value)
    {
        getPersistenceContext().setPropertyValue("pendingItems", value);
    }
}
