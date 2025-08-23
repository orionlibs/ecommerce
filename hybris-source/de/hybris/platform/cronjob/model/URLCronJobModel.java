package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class URLCronJobModel extends MediaProcessCronJobModel
{
    public static final String _TYPECODE = "URLCronJob";
    public static final String URL = "URL";


    public URLCronJobModel()
    {
    }


    public URLCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public URLCronJobModel(JobModel _job)
    {
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public URLCronJobModel(JobModel _job, ItemModel _owner)
    {
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "URL", type = Accessor.Type.GETTER)
    public String getURL()
    {
        return (String)getPersistenceContext().getPropertyValue("URL");
    }


    @Accessor(qualifier = "URL", type = Accessor.Type.SETTER)
    public void setURL(String value)
    {
        getPersistenceContext().setPropertyValue("URL", value);
    }
}
