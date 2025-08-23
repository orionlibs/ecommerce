package de.hybris.platform.apiregistryservices.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ConsumedDestinationModel extends AbstractDestinationModel
{
    public static final String _TYPECODE = "ConsumedDestination";


    public ConsumedDestinationModel()
    {
    }


    public ConsumedDestinationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConsumedDestinationModel(DestinationTargetModel _destinationTarget, EndpointModel _endpoint, String _id, String _url)
    {
        setDestinationTarget(_destinationTarget);
        setEndpoint(_endpoint);
        setId(_id);
        setUrl(_url);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConsumedDestinationModel(DestinationTargetModel _destinationTarget, EndpointModel _endpoint, String _id, ItemModel _owner, String _url)
    {
        setDestinationTarget(_destinationTarget);
        setEndpoint(_endpoint);
        setId(_id);
        setOwner(_owner);
        setUrl(_url);
    }
}
