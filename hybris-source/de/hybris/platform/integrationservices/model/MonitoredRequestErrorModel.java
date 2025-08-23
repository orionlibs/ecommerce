package de.hybris.platform.integrationservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class MonitoredRequestErrorModel extends ItemModel
{
    public static final String _TYPECODE = "MonitoredRequestError";
    public static final String CODE = "code";
    public static final String MESSAGE = "message";


    public MonitoredRequestErrorModel()
    {
    }


    public MonitoredRequestErrorModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MonitoredRequestErrorModel(String _code, String _message)
    {
        setCode(_code);
        setMessage(_message);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MonitoredRequestErrorModel(String _code, String _message, ItemModel _owner)
    {
        setCode(_code);
        setMessage(_message);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "message", type = Accessor.Type.GETTER)
    public String getMessage()
    {
        return (String)getPersistenceContext().getPropertyValue("message");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "message", type = Accessor.Type.SETTER)
    public void setMessage(String value)
    {
        getPersistenceContext().setPropertyValue("message", value);
    }
}
