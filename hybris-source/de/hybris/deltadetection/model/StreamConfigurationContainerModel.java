package de.hybris.deltadetection.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

public class StreamConfigurationContainerModel extends ItemModel
{
    public static final String _TYPECODE = "StreamConfigurationContainer";
    public static final String ID = "id";
    public static final String CONFIGURATIONS = "configurations";


    public StreamConfigurationContainerModel()
    {
    }


    public StreamConfigurationContainerModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public StreamConfigurationContainerModel(String _id)
    {
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public StreamConfigurationContainerModel(String _id, ItemModel _owner)
    {
        setId(_id);
        setOwner(_owner);
    }


    @Accessor(qualifier = "configurations", type = Accessor.Type.GETTER)
    public Set<StreamConfigurationModel> getConfigurations()
    {
        return (Set<StreamConfigurationModel>)getPersistenceContext().getPropertyValue("configurations");
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
    }


    @Accessor(qualifier = "configurations", type = Accessor.Type.SETTER)
    public void setConfigurations(Set<StreamConfigurationModel> value)
    {
        getPersistenceContext().setPropertyValue("configurations", value);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
    }
}
