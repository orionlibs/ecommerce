package de.hybris.platform.apiregistryservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class EndpointModel extends ItemModel
{
    public static final String _TYPECODE = "Endpoint";
    public static final String ID = "id";
    public static final String VERSION = "version";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String SPECURL = "specUrl";
    public static final String SPECDATA = "specData";
    public static final String EXTENSIONNAME = "extensionName";
    public static final String DESTINATIONS = "destinations";


    public EndpointModel()
    {
    }


    public EndpointModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EndpointModel(String _id, String _name, String _version)
    {
        setId(_id);
        setName(_name);
        setVersion(_version);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EndpointModel(String _id, String _name, ItemModel _owner, String _version)
    {
        setId(_id);
        setName(_name);
        setOwner(_owner);
        setVersion(_version);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return (String)getPersistenceContext().getPropertyValue("description");
    }


    @Accessor(qualifier = "destinations", type = Accessor.Type.GETTER)
    public Collection<AbstractDestinationModel> getDestinations()
    {
        return (Collection<AbstractDestinationModel>)getPersistenceContext().getPropertyValue("destinations");
    }


    @Accessor(qualifier = "extensionName", type = Accessor.Type.GETTER)
    public String getExtensionName()
    {
        return (String)getPersistenceContext().getPropertyValue("extensionName");
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "specData", type = Accessor.Type.GETTER)
    public String getSpecData()
    {
        return (String)getPersistenceContext().getPropertyValue("specData");
    }


    @Accessor(qualifier = "specUrl", type = Accessor.Type.GETTER)
    public String getSpecUrl()
    {
        return (String)getPersistenceContext().getPropertyValue("specUrl");
    }


    @Accessor(qualifier = "version", type = Accessor.Type.GETTER)
    public String getVersion()
    {
        return (String)getPersistenceContext().getPropertyValue("version");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        getPersistenceContext().setPropertyValue("description", value);
    }


    @Accessor(qualifier = "destinations", type = Accessor.Type.SETTER)
    public void setDestinations(Collection<AbstractDestinationModel> value)
    {
        getPersistenceContext().setPropertyValue("destinations", value);
    }


    @Accessor(qualifier = "extensionName", type = Accessor.Type.SETTER)
    public void setExtensionName(String value)
    {
        getPersistenceContext().setPropertyValue("extensionName", value);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "specData", type = Accessor.Type.SETTER)
    public void setSpecData(String value)
    {
        getPersistenceContext().setPropertyValue("specData", value);
    }


    @Accessor(qualifier = "specUrl", type = Accessor.Type.SETTER)
    public void setSpecUrl(String value)
    {
        getPersistenceContext().setPropertyValue("specUrl", value);
    }


    @Accessor(qualifier = "version", type = Accessor.Type.SETTER)
    public void setVersion(String value)
    {
        getPersistenceContext().setPropertyValue("version", value);
    }
}
