package de.hybris.platform.integrationservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class IntegrationObjectVirtualAttributeDescriptorModel extends ItemModel
{
    public static final String _TYPECODE = "IntegrationObjectVirtualAttributeDescriptor";
    public static final String CODE = "code";
    public static final String LOGICLOCATION = "logicLocation";
    public static final String TYPE = "type";


    public IntegrationObjectVirtualAttributeDescriptorModel()
    {
    }


    public IntegrationObjectVirtualAttributeDescriptorModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public IntegrationObjectVirtualAttributeDescriptorModel(String _code, String _logicLocation)
    {
        setCode(_code);
        setLogicLocation(_logicLocation);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public IntegrationObjectVirtualAttributeDescriptorModel(String _code, String _logicLocation, ItemModel _owner)
    {
        setCode(_code);
        setLogicLocation(_logicLocation);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "logicLocation", type = Accessor.Type.GETTER)
    public String getLogicLocation()
    {
        return (String)getPersistenceContext().getPropertyValue("logicLocation");
    }


    @Accessor(qualifier = "type", type = Accessor.Type.GETTER)
    public TypeModel getType()
    {
        return (TypeModel)getPersistenceContext().getPropertyValue("type");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "logicLocation", type = Accessor.Type.SETTER)
    public void setLogicLocation(String value)
    {
        getPersistenceContext().setPropertyValue("logicLocation", value);
    }


    @Accessor(qualifier = "type", type = Accessor.Type.SETTER)
    public void setType(TypeModel value)
    {
        getPersistenceContext().setPropertyValue("type", value);
    }
}
