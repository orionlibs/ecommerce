package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

public class CompositeCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "CompositeCronJob";
    public static final String COMPOSITEENTRIES = "compositeEntries";


    public CompositeCronJobModel()
    {
    }


    public CompositeCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CompositeCronJobModel(JobModel _job)
    {
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CompositeCronJobModel(JobModel _job, ItemModel _owner)
    {
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "compositeEntries", type = Accessor.Type.GETTER)
    public List<CompositeEntryModel> getCompositeEntries()
    {
        return (List<CompositeEntryModel>)getPersistenceContext().getPropertyValue("compositeEntries");
    }


    @Accessor(qualifier = "compositeEntries", type = Accessor.Type.SETTER)
    public void setCompositeEntries(List<CompositeEntryModel> value)
    {
        getPersistenceContext().setPropertyValue("compositeEntries", value);
    }
}
