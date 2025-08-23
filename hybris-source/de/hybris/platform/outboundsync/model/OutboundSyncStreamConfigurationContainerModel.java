package de.hybris.platform.outboundsync.model;

import de.hybris.deltadetection.model.StreamConfigurationContainerModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OutboundSyncStreamConfigurationContainerModel extends StreamConfigurationContainerModel
{
    public static final String _TYPECODE = "OutboundSyncStreamConfigurationContainer";


    public OutboundSyncStreamConfigurationContainerModel()
    {
    }


    public OutboundSyncStreamConfigurationContainerModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OutboundSyncStreamConfigurationContainerModel(String _id)
    {
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OutboundSyncStreamConfigurationContainerModel(String _id, ItemModel _owner)
    {
        setId(_id);
        setOwner(_owner);
    }
}
