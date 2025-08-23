package de.hybris.platform.outboundsync.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OutboundChannelConfigurationModel extends ItemModel
{
    public static final String _TYPECODE = "OutboundChannelConfiguration";
    public static final String CODE = "code";
    public static final String INTEGRATIONOBJECT = "integrationObject";
    public static final String DESTINATION = "destination";
    public static final String AUTOGENERATE = "autoGenerate";
    public static final String SYNCHRONIZEDELETE = "synchronizeDelete";


    public OutboundChannelConfigurationModel()
    {
    }


    public OutboundChannelConfigurationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OutboundChannelConfigurationModel(String _code, ConsumedDestinationModel _destination, IntegrationObjectModel _integrationObject)
    {
        setCode(_code);
        setDestination(_destination);
        setIntegrationObject(_integrationObject);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OutboundChannelConfigurationModel(String _code, ConsumedDestinationModel _destination, IntegrationObjectModel _integrationObject, ItemModel _owner)
    {
        setCode(_code);
        setDestination(_destination);
        setIntegrationObject(_integrationObject);
        setOwner(_owner);
    }


    @Accessor(qualifier = "autoGenerate", type = Accessor.Type.GETTER)
    public Boolean getAutoGenerate()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("autoGenerate");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "destination", type = Accessor.Type.GETTER)
    public ConsumedDestinationModel getDestination()
    {
        return (ConsumedDestinationModel)getPersistenceContext().getPropertyValue("destination");
    }


    @Accessor(qualifier = "integrationObject", type = Accessor.Type.GETTER)
    public IntegrationObjectModel getIntegrationObject()
    {
        return (IntegrationObjectModel)getPersistenceContext().getPropertyValue("integrationObject");
    }


    @Accessor(qualifier = "synchronizeDelete", type = Accessor.Type.GETTER)
    public Boolean getSynchronizeDelete()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("synchronizeDelete");
    }


    @Accessor(qualifier = "autoGenerate", type = Accessor.Type.SETTER)
    public void setAutoGenerate(Boolean value)
    {
        getPersistenceContext().setPropertyValue("autoGenerate", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "destination", type = Accessor.Type.SETTER)
    public void setDestination(ConsumedDestinationModel value)
    {
        getPersistenceContext().setPropertyValue("destination", value);
    }


    @Accessor(qualifier = "integrationObject", type = Accessor.Type.SETTER)
    public void setIntegrationObject(IntegrationObjectModel value)
    {
        getPersistenceContext().setPropertyValue("integrationObject", value);
    }


    @Accessor(qualifier = "synchronizeDelete", type = Accessor.Type.SETTER)
    public void setSynchronizeDelete(Boolean value)
    {
        getPersistenceContext().setPropertyValue("synchronizeDelete", value);
    }
}
