package de.hybris.platform.searchservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class FullSnIndexerCronJobModel extends AbstractSnIndexerCronJobModel
{
    public static final String _TYPECODE = "FullSnIndexerCronJob";
    public static final String INDEXERITEMSOURCE = "indexerItemSource";


    public FullSnIndexerCronJobModel()
    {
    }


    public FullSnIndexerCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FullSnIndexerCronJobModel(SnIndexTypeModel _indexType, JobModel _job)
    {
        setIndexType(_indexType);
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FullSnIndexerCronJobModel(SnIndexTypeModel _indexType, JobModel _job, ItemModel _owner)
    {
        setIndexType(_indexType);
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "indexerItemSource", type = Accessor.Type.GETTER)
    public AbstractSnIndexerItemSourceModel getIndexerItemSource()
    {
        return (AbstractSnIndexerItemSourceModel)getPersistenceContext().getPropertyValue("indexerItemSource");
    }


    @Accessor(qualifier = "indexerItemSource", type = Accessor.Type.SETTER)
    public void setIndexerItemSource(AbstractSnIndexerItemSourceModel value)
    {
        getPersistenceContext().setPropertyValue("indexerItemSource", value);
    }
}
