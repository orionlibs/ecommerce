package de.hybris.platform.commercewebservicescommons.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class OldCartRemovalCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "OldCartRemovalCronJob";
    public static final String SITES = "sites";
    public static final String CARTREMOVALAGE = "cartRemovalAge";
    public static final String ANONYMOUSCARTREMOVALAGE = "anonymousCartRemovalAge";


    public OldCartRemovalCronJobModel()
    {
    }


    public OldCartRemovalCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OldCartRemovalCronJobModel(JobModel _job)
    {
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OldCartRemovalCronJobModel(JobModel _job, ItemModel _owner)
    {
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "anonymousCartRemovalAge", type = Accessor.Type.GETTER)
    public Integer getAnonymousCartRemovalAge()
    {
        return (Integer)getPersistenceContext().getPropertyValue("anonymousCartRemovalAge");
    }


    @Accessor(qualifier = "cartRemovalAge", type = Accessor.Type.GETTER)
    public Integer getCartRemovalAge()
    {
        return (Integer)getPersistenceContext().getPropertyValue("cartRemovalAge");
    }


    @Accessor(qualifier = "sites", type = Accessor.Type.GETTER)
    public Collection<BaseSiteModel> getSites()
    {
        return (Collection<BaseSiteModel>)getPersistenceContext().getPropertyValue("sites");
    }


    @Accessor(qualifier = "anonymousCartRemovalAge", type = Accessor.Type.SETTER)
    public void setAnonymousCartRemovalAge(Integer value)
    {
        getPersistenceContext().setPropertyValue("anonymousCartRemovalAge", value);
    }


    @Accessor(qualifier = "cartRemovalAge", type = Accessor.Type.SETTER)
    public void setCartRemovalAge(Integer value)
    {
        getPersistenceContext().setPropertyValue("cartRemovalAge", value);
    }


    @Accessor(qualifier = "sites", type = Accessor.Type.SETTER)
    public void setSites(Collection<BaseSiteModel> value)
    {
        getPersistenceContext().setPropertyValue("sites", value);
    }
}
