package de.hybris.platform.cms2.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;
import java.util.Set;

public class ComponentTypeGroupModel extends ItemModel
{
    public static final String _TYPECODE = "ComponentTypeGroup";
    public static final String CODE = "code";
    public static final String DESCRIPTION = "description";
    public static final String CMSCOMPONENTTYPES = "cmsComponentTypes";


    public ComponentTypeGroupModel()
    {
    }


    public ComponentTypeGroupModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ComponentTypeGroupModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ComponentTypeGroupModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "cmsComponentTypes", type = Accessor.Type.GETTER)
    public Set<CMSComponentTypeModel> getCmsComponentTypes()
    {
        return (Set<CMSComponentTypeModel>)getPersistenceContext().getPropertyValue("cmsComponentTypes");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return getDescription(null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("description", loc);
    }


    @Accessor(qualifier = "cmsComponentTypes", type = Accessor.Type.SETTER)
    public void setCmsComponentTypes(Set<CMSComponentTypeModel> value)
    {
        getPersistenceContext().setPropertyValue("cmsComponentTypes", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        setDescription(value, null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("description", loc, value);
    }
}
