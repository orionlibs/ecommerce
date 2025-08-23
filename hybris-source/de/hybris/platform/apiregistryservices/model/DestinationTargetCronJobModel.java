package de.hybris.platform.apiregistryservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class DestinationTargetCronJobModel extends ServicelayerJobModel
{
    public static final String _TYPECODE = "DestinationTargetCronJob";
    public static final String DESTINATIONTARGETID = "destinationTargetId";


    public DestinationTargetCronJobModel()
    {
    }


    public DestinationTargetCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DestinationTargetCronJobModel(String _code, String _destinationTargetId, String _springId)
    {
        setCode(_code);
        setDestinationTargetId(_destinationTargetId);
        setSpringId(_springId);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DestinationTargetCronJobModel(String _code, String _destinationTargetId, Integer _nodeID, ItemModel _owner, String _springId)
    {
        setCode(_code);
        setDestinationTargetId(_destinationTargetId);
        setNodeID(_nodeID);
        setOwner(_owner);
        setSpringId(_springId);
    }


    @Accessor(qualifier = "destinationTargetId", type = Accessor.Type.GETTER)
    public String getDestinationTargetId()
    {
        return (String)getPersistenceContext().getPropertyValue("destinationTargetId");
    }


    @Accessor(qualifier = "destinationTargetId", type = Accessor.Type.SETTER)
    public void setDestinationTargetId(String value)
    {
        getPersistenceContext().setPropertyValue("destinationTargetId", value);
    }
}
