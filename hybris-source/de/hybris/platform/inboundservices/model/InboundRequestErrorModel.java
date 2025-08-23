package de.hybris.platform.inboundservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.model.MonitoredRequestErrorModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class InboundRequestErrorModel extends MonitoredRequestErrorModel
{
    public static final String _TYPECODE = "InboundRequestError";
    public static final String _INBOUNDREQUEST2INBOUNDREQUESTERROR = "InboundRequest2InboundRequestError";
    public static final String INBOUNDREQUEST = "inboundRequest";


    public InboundRequestErrorModel()
    {
    }


    public InboundRequestErrorModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public InboundRequestErrorModel(String _code, String _message)
    {
        setCode(_code);
        setMessage(_message);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public InboundRequestErrorModel(String _code, String _message, ItemModel _owner)
    {
        setCode(_code);
        setMessage(_message);
        setOwner(_owner);
    }


    @Accessor(qualifier = "inboundRequest", type = Accessor.Type.GETTER)
    public InboundRequestModel getInboundRequest()
    {
        return (InboundRequestModel)getPersistenceContext().getPropertyValue("inboundRequest");
    }


    @Accessor(qualifier = "inboundRequest", type = Accessor.Type.SETTER)
    public void setInboundRequest(InboundRequestModel value)
    {
        getPersistenceContext().setPropertyValue("inboundRequest", value);
    }
}
