package de.hybris.platform.sap.sapcpiadapter.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SAPCpiOutboundConfigModel extends ItemModel
{
    public static final String _TYPECODE = "SAPCpiOutboundConfig";
    public static final String URL = "url";
    public static final String USERNAME = "username";
    public static final String SENDERNAME = "senderName";
    public static final String SENDERPORT = "senderPort";
    public static final String RECEIVERNAME = "receiverName";
    public static final String RECEIVERPORT = "receiverPort";
    public static final String CLIENT = "client";


    public SAPCpiOutboundConfigModel()
    {
    }


    public SAPCpiOutboundConfigModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPCpiOutboundConfigModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "client", type = Accessor.Type.GETTER)
    public String getClient()
    {
        return (String)getPersistenceContext().getPropertyValue("client");
    }


    @Accessor(qualifier = "receiverName", type = Accessor.Type.GETTER)
    public String getReceiverName()
    {
        return (String)getPersistenceContext().getPropertyValue("receiverName");
    }


    @Accessor(qualifier = "receiverPort", type = Accessor.Type.GETTER)
    public String getReceiverPort()
    {
        return (String)getPersistenceContext().getPropertyValue("receiverPort");
    }


    @Accessor(qualifier = "senderName", type = Accessor.Type.GETTER)
    public String getSenderName()
    {
        return (String)getPersistenceContext().getPropertyValue("senderName");
    }


    @Accessor(qualifier = "senderPort", type = Accessor.Type.GETTER)
    public String getSenderPort()
    {
        return (String)getPersistenceContext().getPropertyValue("senderPort");
    }


    @Accessor(qualifier = "url", type = Accessor.Type.GETTER)
    public String getUrl()
    {
        return (String)getPersistenceContext().getPropertyValue("url");
    }


    @Accessor(qualifier = "username", type = Accessor.Type.GETTER)
    public String getUsername()
    {
        return (String)getPersistenceContext().getPropertyValue("username");
    }


    @Accessor(qualifier = "client", type = Accessor.Type.SETTER)
    public void setClient(String value)
    {
        getPersistenceContext().setPropertyValue("client", value);
    }


    @Accessor(qualifier = "receiverName", type = Accessor.Type.SETTER)
    public void setReceiverName(String value)
    {
        getPersistenceContext().setPropertyValue("receiverName", value);
    }


    @Accessor(qualifier = "receiverPort", type = Accessor.Type.SETTER)
    public void setReceiverPort(String value)
    {
        getPersistenceContext().setPropertyValue("receiverPort", value);
    }


    @Accessor(qualifier = "senderName", type = Accessor.Type.SETTER)
    public void setSenderName(String value)
    {
        getPersistenceContext().setPropertyValue("senderName", value);
    }


    @Accessor(qualifier = "senderPort", type = Accessor.Type.SETTER)
    public void setSenderPort(String value)
    {
        getPersistenceContext().setPropertyValue("senderPort", value);
    }


    @Accessor(qualifier = "url", type = Accessor.Type.SETTER)
    public void setUrl(String value)
    {
        getPersistenceContext().setPropertyValue("url", value);
    }


    @Accessor(qualifier = "username", type = Accessor.Type.SETTER)
    public void setUsername(String value)
    {
        getPersistenceContext().setPropertyValue("username", value);
    }
}
