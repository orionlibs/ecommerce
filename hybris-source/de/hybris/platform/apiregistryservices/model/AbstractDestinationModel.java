package de.hybris.platform.apiregistryservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Map;

public class AbstractDestinationModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractDestination";
    public static final String _ENDPOINT2ABSTRACTDESTINATION = "Endpoint2AbstractDestination";
    public static final String _DESTINATIONTARGET2DESTINATION = "DestinationTarget2Destination";
    public static final String ID = "id";
    public static final String URL = "url";
    public static final String ACTIVE = "active";
    public static final String ADDITIONALPROPERTIES = "additionalProperties";
    public static final String CREDENTIAL = "credential";
    public static final String ENDPOINT = "endpoint";
    public static final String DESTINATIONTARGET = "destinationTarget";


    public AbstractDestinationModel()
    {
    }


    public AbstractDestinationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractDestinationModel(DestinationTargetModel _destinationTarget, EndpointModel _endpoint, String _id, String _url)
    {
        setDestinationTarget(_destinationTarget);
        setEndpoint(_endpoint);
        setId(_id);
        setUrl(_url);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractDestinationModel(DestinationTargetModel _destinationTarget, EndpointModel _endpoint, String _id, ItemModel _owner, String _url)
    {
        setDestinationTarget(_destinationTarget);
        setEndpoint(_endpoint);
        setId(_id);
        setOwner(_owner);
        setUrl(_url);
    }


    @Accessor(qualifier = "additionalProperties", type = Accessor.Type.GETTER)
    public Map<String, String> getAdditionalProperties()
    {
        return (Map<String, String>)getPersistenceContext().getPropertyValue("additionalProperties");
    }


    @Accessor(qualifier = "credential", type = Accessor.Type.GETTER)
    public AbstractCredentialModel getCredential()
    {
        return (AbstractCredentialModel)getPersistenceContext().getPropertyValue("credential");
    }


    @Accessor(qualifier = "destinationTarget", type = Accessor.Type.GETTER)
    public DestinationTargetModel getDestinationTarget()
    {
        return (DestinationTargetModel)getPersistenceContext().getPropertyValue("destinationTarget");
    }


    @Accessor(qualifier = "endpoint", type = Accessor.Type.GETTER)
    public EndpointModel getEndpoint()
    {
        return (EndpointModel)getPersistenceContext().getPropertyValue("endpoint");
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
    }


    @Accessor(qualifier = "url", type = Accessor.Type.GETTER)
    public String getUrl()
    {
        return (String)getPersistenceContext().getPropertyValue("url");
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public boolean isActive()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("active"));
    }


    @Accessor(qualifier = "active", type = Accessor.Type.SETTER)
    public void setActive(boolean value)
    {
        getPersistenceContext().setPropertyValue("active", toObject(value));
    }


    @Accessor(qualifier = "additionalProperties", type = Accessor.Type.SETTER)
    public void setAdditionalProperties(Map<String, String> value)
    {
        getPersistenceContext().setPropertyValue("additionalProperties", value);
    }


    @Accessor(qualifier = "credential", type = Accessor.Type.SETTER)
    public void setCredential(AbstractCredentialModel value)
    {
        getPersistenceContext().setPropertyValue("credential", value);
    }


    @Accessor(qualifier = "destinationTarget", type = Accessor.Type.SETTER)
    public void setDestinationTarget(DestinationTargetModel value)
    {
        getPersistenceContext().setPropertyValue("destinationTarget", value);
    }


    @Accessor(qualifier = "endpoint", type = Accessor.Type.SETTER)
    public void setEndpoint(EndpointModel value)
    {
        getPersistenceContext().setPropertyValue("endpoint", value);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
    }


    @Accessor(qualifier = "url", type = Accessor.Type.SETTER)
    public void setUrl(String value)
    {
        getPersistenceContext().setPropertyValue("url", value);
    }
}
