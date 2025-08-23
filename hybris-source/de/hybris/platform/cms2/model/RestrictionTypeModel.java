package de.hybris.platform.cms2.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class RestrictionTypeModel extends ComposedTypeModel
{
    public static final String _TYPECODE = "RestrictionType";
    public static final String PAGETYPES = "pageTypes";


    public RestrictionTypeModel()
    {
    }


    public RestrictionTypeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RestrictionTypeModel(Boolean _catalogItemType, String _code, Boolean _generate, Boolean _singleton, ComposedTypeModel _superType)
    {
        setCatalogItemType(_catalogItemType);
        setCode(_code);
        setGenerate(_generate);
        setSingleton(_singleton);
        setSuperType(_superType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RestrictionTypeModel(Boolean _catalogItemType, String _code, Boolean _generate, ItemModel _owner, Boolean _singleton, ComposedTypeModel _superType)
    {
        setCatalogItemType(_catalogItemType);
        setCode(_code);
        setGenerate(_generate);
        setOwner(_owner);
        setSingleton(_singleton);
        setSuperType(_superType);
    }


    @Accessor(qualifier = "pageTypes", type = Accessor.Type.GETTER)
    public Collection<CMSPageTypeModel> getPageTypes()
    {
        return (Collection<CMSPageTypeModel>)getPersistenceContext().getPropertyValue("pageTypes");
    }


    @Accessor(qualifier = "pageTypes", type = Accessor.Type.SETTER)
    public void setPageTypes(Collection<CMSPageTypeModel> value)
    {
        getPersistenceContext().setPropertyValue("pageTypes", value);
    }
}
