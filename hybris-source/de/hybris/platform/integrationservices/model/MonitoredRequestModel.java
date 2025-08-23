package de.hybris.platform.integrationservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.integrationservices.enums.HttpMethod;
import de.hybris.platform.integrationservices.enums.IntegrationRequestStatus;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class MonitoredRequestModel extends ItemModel
{
    public static final String _TYPECODE = "MonitoredRequest";
    public static final String STATUS = "status";
    public static final String INTEGRATIONKEY = "integrationKey";
    public static final String TYPE = "type";
    public static final String PAYLOAD = "payload";
    public static final String MESSAGEID = "messageId";
    public static final String USER = "user";
    public static final String SAPPASSPORT = "sapPassport";
    public static final String HTTPMETHOD = "httpMethod";


    public MonitoredRequestModel()
    {
    }


    public MonitoredRequestModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MonitoredRequestModel(IntegrationRequestStatus _status, String _type)
    {
        setStatus(_status);
        setType(_type);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MonitoredRequestModel(HttpMethod _httpMethod, ItemModel _owner, String _sapPassport, IntegrationRequestStatus _status, String _type, UserModel _user)
    {
        setHttpMethod(_httpMethod);
        setOwner(_owner);
        setSapPassport(_sapPassport);
        setStatus(_status);
        setType(_type);
        setUser(_user);
    }


    @Accessor(qualifier = "httpMethod", type = Accessor.Type.GETTER)
    public HttpMethod getHttpMethod()
    {
        return (HttpMethod)getPersistenceContext().getPropertyValue("httpMethod");
    }


    @Accessor(qualifier = "integrationKey", type = Accessor.Type.GETTER)
    public String getIntegrationKey()
    {
        return (String)getPersistenceContext().getPropertyValue("integrationKey");
    }


    @Accessor(qualifier = "messageId", type = Accessor.Type.GETTER)
    public String getMessageId()
    {
        return (String)getPersistenceContext().getPropertyValue("messageId");
    }


    @Accessor(qualifier = "payload", type = Accessor.Type.GETTER)
    public IntegrationApiMediaModel getPayload()
    {
        return (IntegrationApiMediaModel)getPersistenceContext().getPropertyValue("payload");
    }


    @Accessor(qualifier = "sapPassport", type = Accessor.Type.GETTER)
    public String getSapPassport()
    {
        return (String)getPersistenceContext().getPropertyValue("sapPassport");
    }


    @Accessor(qualifier = "status", type = Accessor.Type.GETTER)
    public IntegrationRequestStatus getStatus()
    {
        return (IntegrationRequestStatus)getPersistenceContext().getPropertyValue("status");
    }


    @Accessor(qualifier = "type", type = Accessor.Type.GETTER)
    public String getType()
    {
        return (String)getPersistenceContext().getPropertyValue("type");
    }


    @Accessor(qualifier = "user", type = Accessor.Type.GETTER)
    public UserModel getUser()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("user");
    }


    @Accessor(qualifier = "httpMethod", type = Accessor.Type.SETTER)
    public void setHttpMethod(HttpMethod value)
    {
        getPersistenceContext().setPropertyValue("httpMethod", value);
    }


    @Accessor(qualifier = "integrationKey", type = Accessor.Type.SETTER)
    public void setIntegrationKey(String value)
    {
        getPersistenceContext().setPropertyValue("integrationKey", value);
    }


    @Accessor(qualifier = "messageId", type = Accessor.Type.SETTER)
    public void setMessageId(String value)
    {
        getPersistenceContext().setPropertyValue("messageId", value);
    }


    @Accessor(qualifier = "payload", type = Accessor.Type.SETTER)
    public void setPayload(IntegrationApiMediaModel value)
    {
        getPersistenceContext().setPropertyValue("payload", value);
    }


    @Accessor(qualifier = "sapPassport", type = Accessor.Type.SETTER)
    public void setSapPassport(String value)
    {
        getPersistenceContext().setPropertyValue("sapPassport", value);
    }


    @Accessor(qualifier = "status", type = Accessor.Type.SETTER)
    public void setStatus(IntegrationRequestStatus value)
    {
        getPersistenceContext().setPropertyValue("status", value);
    }


    @Accessor(qualifier = "type", type = Accessor.Type.SETTER)
    public void setType(String value)
    {
        getPersistenceContext().setPropertyValue("type", value);
    }


    @Accessor(qualifier = "user", type = Accessor.Type.SETTER)
    public void setUser(UserModel value)
    {
        getPersistenceContext().setPropertyValue("user", value);
    }
}
