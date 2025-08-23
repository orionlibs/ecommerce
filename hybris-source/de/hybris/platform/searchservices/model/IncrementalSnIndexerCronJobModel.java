package de.hybris.platform.searchservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

public class IncrementalSnIndexerCronJobModel extends AbstractSnIndexerCronJobModel
{
    public static final String _TYPECODE = "IncrementalSnIndexerCronJob";
    public static final String INDEXERITEMSOURCEOPERATIONS = "indexerItemSourceOperations";


    public IncrementalSnIndexerCronJobModel()
    {
    }


    public IncrementalSnIndexerCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public IncrementalSnIndexerCronJobModel(SnIndexTypeModel _indexType, JobModel _job)
    {
        setIndexType(_indexType);
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public IncrementalSnIndexerCronJobModel(SnIndexTypeModel _indexType, JobModel _job, ItemModel _owner)
    {
        setIndexType(_indexType);
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "indexerItemSourceOperations", type = Accessor.Type.GETTER)
    public List<SnIndexerItemSourceOperationModel> getIndexerItemSourceOperations()
    {
        return (List<SnIndexerItemSourceOperationModel>)getPersistenceContext().getPropertyValue("indexerItemSourceOperations");
    }


    @Accessor(qualifier = "indexerItemSourceOperations", type = Accessor.Type.SETTER)
    public void setIndexerItemSourceOperations(List<SnIndexerItemSourceOperationModel> value)
    {
        getPersistenceContext().setPropertyValue("indexerItemSourceOperations", value);
    }
}
