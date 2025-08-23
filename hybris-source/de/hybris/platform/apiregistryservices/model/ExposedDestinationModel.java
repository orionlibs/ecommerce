package de.hybris.platform.apiregistryservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.model.InboundChannelConfigurationModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ExposedDestinationModel extends AbstractDestinationModel
{
    public static final String _TYPECODE = "ExposedDestination";
    public static final String _INBOUNDCHANNELCONFIGURATION2EXPOSEDDESTINATION = "InboundChannelConfiguration2ExposedDestination";
    public static final String TARGETID = "targetId";
    public static final String INBOUNDCHANNELCONFIGURATIONPOS = "inboundChannelConfigurationPOS";
    public static final String INBOUNDCHANNELCONFIGURATION = "inboundChannelConfiguration";


    public ExposedDestinationModel()
    {
    }


    public ExposedDestinationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ExposedDestinationModel(DestinationTargetModel _destinationTarget, EndpointModel _endpoint, String _id, String _url)
    {
        setDestinationTarget(_destinationTarget);
        setEndpoint(_endpoint);
        setId(_id);
        setUrl(_url);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ExposedDestinationModel(DestinationTargetModel _destinationTarget, EndpointModel _endpoint, String _id, ItemModel _owner, String _url)
    {
        setDestinationTarget(_destinationTarget);
        setEndpoint(_endpoint);
        setId(_id);
        setOwner(_owner);
        setUrl(_url);
    }


    @Accessor(qualifier = "inboundChannelConfiguration", type = Accessor.Type.GETTER)
    public InboundChannelConfigurationModel getInboundChannelConfiguration()
    {
        return (InboundChannelConfigurationModel)getPersistenceContext().getPropertyValue("inboundChannelConfiguration");
    }


    @Accessor(qualifier = "targetId", type = Accessor.Type.GETTER)
    public String getTargetId()
    {
        return (String)getPersistenceContext().getPropertyValue("targetId");
    }


    @Accessor(qualifier = "inboundChannelConfiguration", type = Accessor.Type.SETTER)
    public void setInboundChannelConfiguration(InboundChannelConfigurationModel value)
    {
        getPersistenceContext().setPropertyValue("inboundChannelConfiguration", value);
    }


    @Accessor(qualifier = "targetId", type = Accessor.Type.SETTER)
    public void setTargetId(String value)
    {
        getPersistenceContext().setPropertyValue("targetId", value);
    }
}
