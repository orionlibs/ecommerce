package de.hybris.platform.acceleratorservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class CartRemovalCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "CartRemovalCronJob";
    public static final String _BASESITE2CARTREMOVALCRONJOB = "BaseSite2CartRemovalCronJob";
    public static final String SITES = "sites";


    public CartRemovalCronJobModel()
    {
    }


    public CartRemovalCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CartRemovalCronJobModel(JobModel _job, Collection<BaseSiteModel> _sites)
    {
        setJob(_job);
        setSites(_sites);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CartRemovalCronJobModel(JobModel _job, ItemModel _owner, Collection<BaseSiteModel> _sites)
    {
        setJob(_job);
        setOwner(_owner);
        setSites(_sites);
    }


    @Accessor(qualifier = "sites", type = Accessor.Type.GETTER)
    public Collection<BaseSiteModel> getSites()
    {
        return (Collection<BaseSiteModel>)getPersistenceContext().getPropertyValue("sites");
    }


    @Accessor(qualifier = "sites", type = Accessor.Type.SETTER)
    public void setSites(Collection<BaseSiteModel> value)
    {
        getPersistenceContext().setPropertyValue("sites", value);
    }
}
