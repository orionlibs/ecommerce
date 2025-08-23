package de.hybris.platform.acceleratorcms.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class CMSActionTypeModel extends CMSComponentTypeModel
{
    public static final String _TYPECODE = "CMSActionType";
    public static final String COMPONENTTYPES = "componentTypes";


    public CMSActionTypeModel()
    {
    }


    public CMSActionTypeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSActionTypeModel(Boolean _catalogItemType, String _code, Boolean _generate, Boolean _singleton, ComposedTypeModel _superType)
    {
        setCatalogItemType(_catalogItemType);
        setCode(_code);
        setGenerate(_generate);
        setSingleton(_singleton);
        setSuperType(_superType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSActionTypeModel(Boolean _catalogItemType, String _code, Boolean _generate, ItemModel _owner, Boolean _singleton, ComposedTypeModel _superType)
    {
        setCatalogItemType(_catalogItemType);
        setCode(_code);
        setGenerate(_generate);
        setOwner(_owner);
        setSingleton(_singleton);
        setSuperType(_superType);
    }


    @Accessor(qualifier = "componentTypes", type = Accessor.Type.GETTER)
    public Collection<CMSComponentTypeModel> getComponentTypes()
    {
        return (Collection<CMSComponentTypeModel>)getPersistenceContext().getPropertyValue("componentTypes");
    }


    @Accessor(qualifier = "componentTypes", type = Accessor.Type.SETTER)
    public void setComponentTypes(Collection<CMSComponentTypeModel> value)
    {
        getPersistenceContext().setPropertyValue("componentTypes", value);
    }
}
