package de.hybris.platform.servicelayer.internal.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ServicelayerJobModel extends JobModel
{
    public static final String _TYPECODE = "ServicelayerJob";
    public static final String SPRINGID = "springId";
    public static final String SPRINGIDCRONJOBFACTORY = "springIdCronJobFactory";


    public ServicelayerJobModel()
    {
    }


    public ServicelayerJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ServicelayerJobModel(String _code, String _springId)
    {
        setCode(_code);
        setSpringId(_springId);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ServicelayerJobModel(String _code, Integer _nodeID, ItemModel _owner, String _springId)
    {
        setCode(_code);
        setNodeID(_nodeID);
        setOwner(_owner);
        setSpringId(_springId);
    }


    @Accessor(qualifier = "springId", type = Accessor.Type.GETTER)
    public String getSpringId()
    {
        return (String)getPersistenceContext().getPropertyValue("springId");
    }


    @Accessor(qualifier = "springIdCronJobFactory", type = Accessor.Type.GETTER)
    public String getSpringIdCronJobFactory()
    {
        return (String)getPersistenceContext().getPropertyValue("springIdCronJobFactory");
    }


    @Accessor(qualifier = "springId", type = Accessor.Type.SETTER)
    public void setSpringId(String value)
    {
        getPersistenceContext().setPropertyValue("springId", value);
    }


    @Accessor(qualifier = "springIdCronJobFactory", type = Accessor.Type.SETTER)
    public void setSpringIdCronJobFactory(String value)
    {
        getPersistenceContext().setPropertyValue("springIdCronJobFactory", value);
    }
}
