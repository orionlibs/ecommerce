package de.hybris.platform.outboundservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.integrationservices.enums.HttpMethod;
import de.hybris.platform.integrationservices.enums.IntegrationRequestStatus;
import de.hybris.platform.integrationservices.model.MonitoredRequestModel;
import de.hybris.platform.outboundservices.enums.OutboundSource;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OutboundRequestModel extends MonitoredRequestModel
{
    public static final String _TYPECODE = "OutboundRequest";
    public static final String DESTINATION = "destination";
    public static final String SOURCE = "source";
    public static final String ERROR = "error";


    public OutboundRequestModel()
    {
    }


    public OutboundRequestModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OutboundRequestModel(String _destination, IntegrationRequestStatus _status, String _type)
    {
        setDestination(_destination);
        setStatus(_status);
        setType(_type);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OutboundRequestModel(String _destination, HttpMethod _httpMethod, ItemModel _owner, String _sapPassport, IntegrationRequestStatus _status, String _type, UserModel _user)
    {
        setDestination(_destination);
        setHttpMethod(_httpMethod);
        setOwner(_owner);
        setSapPassport(_sapPassport);
        setStatus(_status);
        setType(_type);
        setUser(_user);
    }


    @Accessor(qualifier = "destination", type = Accessor.Type.GETTER)
    public String getDestination()
    {
        return (String)getPersistenceContext().getPropertyValue("destination");
    }


    @Accessor(qualifier = "error", type = Accessor.Type.GETTER)
    public String getError()
    {
        return (String)getPersistenceContext().getPropertyValue("error");
    }


    @Accessor(qualifier = "source", type = Accessor.Type.GETTER)
    public OutboundSource getSource()
    {
        return (OutboundSource)getPersistenceContext().getPropertyValue("source");
    }


    @Accessor(qualifier = "destination", type = Accessor.Type.SETTER)
    public void setDestination(String value)
    {
        getPersistenceContext().setPropertyValue("destination", value);
    }


    @Accessor(qualifier = "error", type = Accessor.Type.SETTER)
    public void setError(String value)
    {
        getPersistenceContext().setPropertyValue("error", value);
    }


    @Accessor(qualifier = "source", type = Accessor.Type.SETTER)
    public void setSource(OutboundSource value)
    {
        getPersistenceContext().setPropertyValue("source", value);
    }
}
