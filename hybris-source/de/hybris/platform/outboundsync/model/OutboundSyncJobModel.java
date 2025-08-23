package de.hybris.platform.outboundsync.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OutboundSyncJobModel extends ServicelayerJobModel
{
    public static final String _TYPECODE = "OutboundSyncJob";
    public static final String STREAMCONFIGURATIONCONTAINER = "streamConfigurationContainer";


    public OutboundSyncJobModel()
    {
    }


    public OutboundSyncJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OutboundSyncJobModel(String _code, String _springId, OutboundSyncStreamConfigurationContainerModel _streamConfigurationContainer)
    {
        setCode(_code);
        setSpringId(_springId);
        setStreamConfigurationContainer(_streamConfigurationContainer);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OutboundSyncJobModel(String _code, Integer _nodeID, ItemModel _owner, String _springId, OutboundSyncStreamConfigurationContainerModel _streamConfigurationContainer)
    {
        setCode(_code);
        setNodeID(_nodeID);
        setOwner(_owner);
        setSpringId(_springId);
        setStreamConfigurationContainer(_streamConfigurationContainer);
    }


    @Accessor(qualifier = "streamConfigurationContainer", type = Accessor.Type.GETTER)
    public OutboundSyncStreamConfigurationContainerModel getStreamConfigurationContainer()
    {
        return (OutboundSyncStreamConfigurationContainerModel)getPersistenceContext().getPropertyValue("streamConfigurationContainer");
    }


    @Accessor(qualifier = "streamConfigurationContainer", type = Accessor.Type.SETTER)
    public void setStreamConfigurationContainer(OutboundSyncStreamConfigurationContainerModel value)
    {
        getPersistenceContext().setPropertyValue("streamConfigurationContainer", value);
    }
}
