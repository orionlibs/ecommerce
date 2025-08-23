package de.hybris.platform.searchservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class AbstractSnIndexerCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "AbstractSnIndexerCronJob";
    public static final String _SNINDEXTYPE2INDEXERCRONJOB = "SnIndexType2IndexerCronJob";
    public static final String LASTSUCCESSFULSTARTTIME = "lastSuccessfulStartTime";
    public static final String INDEXTYPEPOS = "indexTypePOS";
    public static final String INDEXTYPE = "indexType";


    public AbstractSnIndexerCronJobModel()
    {
    }


    public AbstractSnIndexerCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractSnIndexerCronJobModel(SnIndexTypeModel _indexType, JobModel _job)
    {
        setIndexType(_indexType);
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractSnIndexerCronJobModel(SnIndexTypeModel _indexType, JobModel _job, ItemModel _owner)
    {
        setIndexType(_indexType);
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "indexType", type = Accessor.Type.GETTER)
    public SnIndexTypeModel getIndexType()
    {
        return (SnIndexTypeModel)getPersistenceContext().getPropertyValue("indexType");
    }


    @Accessor(qualifier = "lastSuccessfulStartTime", type = Accessor.Type.GETTER)
    public Date getLastSuccessfulStartTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("lastSuccessfulStartTime");
    }


    @Accessor(qualifier = "indexType", type = Accessor.Type.SETTER)
    public void setIndexType(SnIndexTypeModel value)
    {
        getPersistenceContext().setPropertyValue("indexType", value);
    }


    @Accessor(qualifier = "lastSuccessfulStartTime", type = Accessor.Type.SETTER)
    public void setLastSuccessfulStartTime(Date value)
    {
        getPersistenceContext().setPropertyValue("lastSuccessfulStartTime", value);
    }
}
