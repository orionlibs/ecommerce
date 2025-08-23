package de.hybris.platform.integrationservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.enums.AuthenticationType;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

public class InboundChannelConfigurationModel extends ItemModel
{
    public static final String _TYPECODE = "InboundChannelConfiguration";
    public static final String INTEGRATIONOBJECT = "integrationObject";
    public static final String AUTHENTICATIONTYPE = "authenticationType";
    public static final String EXPOSEDDESTINATIONS = "exposedDestinations";


    public InboundChannelConfigurationModel()
    {
    }


    public InboundChannelConfigurationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public InboundChannelConfigurationModel(IntegrationObjectModel _integrationObject)
    {
        setIntegrationObject(_integrationObject);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public InboundChannelConfigurationModel(IntegrationObjectModel _integrationObject, ItemModel _owner)
    {
        setIntegrationObject(_integrationObject);
        setOwner(_owner);
    }


    @Accessor(qualifier = "authenticationType", type = Accessor.Type.GETTER)
    public AuthenticationType getAuthenticationType()
    {
        return (AuthenticationType)getPersistenceContext().getPropertyValue("authenticationType");
    }


    @Accessor(qualifier = "exposedDestinations", type = Accessor.Type.GETTER)
    public List<ExposedDestinationModel> getExposedDestinations()
    {
        return (List<ExposedDestinationModel>)getPersistenceContext().getPropertyValue("exposedDestinations");
    }


    @Accessor(qualifier = "integrationObject", type = Accessor.Type.GETTER)
    public IntegrationObjectModel getIntegrationObject()
    {
        return (IntegrationObjectModel)getPersistenceContext().getPropertyValue("integrationObject");
    }


    @Accessor(qualifier = "authenticationType", type = Accessor.Type.SETTER)
    public void setAuthenticationType(AuthenticationType value)
    {
        getPersistenceContext().setPropertyValue("authenticationType", value);
    }


    @Accessor(qualifier = "exposedDestinations", type = Accessor.Type.SETTER)
    public void setExposedDestinations(List<ExposedDestinationModel> value)
    {
        getPersistenceContext().setPropertyValue("exposedDestinations", value);
    }


    @Accessor(qualifier = "integrationObject", type = Accessor.Type.SETTER)
    public void setIntegrationObject(IntegrationObjectModel value)
    {
        getPersistenceContext().setPropertyValue("integrationObject", value);
    }
}
