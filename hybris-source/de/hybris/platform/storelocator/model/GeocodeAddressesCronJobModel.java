package de.hybris.platform.storelocator.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class GeocodeAddressesCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "GeocodeAddressesCronJob";
    public static final String BATCHSIZE = "batchSize";
    public static final String INTERNALDELAY = "internalDelay";


    public GeocodeAddressesCronJobModel()
    {
    }


    public GeocodeAddressesCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public GeocodeAddressesCronJobModel(JobModel _job)
    {
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public GeocodeAddressesCronJobModel(JobModel _job, ItemModel _owner)
    {
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "batchSize", type = Accessor.Type.GETTER)
    public int getBatchSize()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("batchSize"));
    }


    @Accessor(qualifier = "internalDelay", type = Accessor.Type.GETTER)
    public int getInternalDelay()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("internalDelay"));
    }


    @Accessor(qualifier = "batchSize", type = Accessor.Type.SETTER)
    public void setBatchSize(int value)
    {
        getPersistenceContext().setPropertyValue("batchSize", toObject(value));
    }


    @Accessor(qualifier = "internalDelay", type = Accessor.Type.SETTER)
    public void setInternalDelay(int value)
    {
        getPersistenceContext().setPropertyValue("internalDelay", toObject(value));
    }
}
