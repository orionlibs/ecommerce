package de.hybris.platform.outboundsync.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.deltadetection.model.StreamConfigurationContainerModel;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OutboundSyncStreamConfigurationModel extends StreamConfigurationModel
{
    public static final String _TYPECODE = "OutboundSyncStreamConfiguration";
    public static final String OUTBOUNDCHANNELCONFIGURATION = "outboundChannelConfiguration";


    public OutboundSyncStreamConfigurationModel()
    {
    }


    public OutboundSyncStreamConfigurationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OutboundSyncStreamConfigurationModel(StreamConfigurationContainerModel _container, ComposedTypeModel _itemTypeForStream, OutboundChannelConfigurationModel _outboundChannelConfiguration, String _streamId)
    {
        setContainer(_container);
        setItemTypeForStream(_itemTypeForStream);
        setOutboundChannelConfiguration(_outboundChannelConfiguration);
        setStreamId(_streamId);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OutboundSyncStreamConfigurationModel(StreamConfigurationContainerModel _container, ComposedTypeModel _itemTypeForStream, OutboundChannelConfigurationModel _outboundChannelConfiguration, ItemModel _owner, String _streamId)
    {
        setContainer(_container);
        setItemTypeForStream(_itemTypeForStream);
        setOutboundChannelConfiguration(_outboundChannelConfiguration);
        setOwner(_owner);
        setStreamId(_streamId);
    }


    @Accessor(qualifier = "outboundChannelConfiguration", type = Accessor.Type.GETTER)
    public OutboundChannelConfigurationModel getOutboundChannelConfiguration()
    {
        return (OutboundChannelConfigurationModel)getPersistenceContext().getPropertyValue("outboundChannelConfiguration");
    }


    @Accessor(qualifier = "outboundChannelConfiguration", type = Accessor.Type.SETTER)
    public void setOutboundChannelConfiguration(OutboundChannelConfigurationModel value)
    {
        getPersistenceContext().setPropertyValue("outboundChannelConfiguration", value);
    }
}
