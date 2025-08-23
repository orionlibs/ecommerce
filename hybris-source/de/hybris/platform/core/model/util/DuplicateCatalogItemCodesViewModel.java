package de.hybris.platform.core.model.util;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class DuplicateCatalogItemCodesViewModel extends ItemModel
{
    public static final String _TYPECODE = "DuplicateCatalogItemCodesView";
    public static final String TYPECODE = "typeCode";
    public static final String CODE = "code";
    public static final String COUNT = "count";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String CV = "cv";


    public DuplicateCatalogItemCodesViewModel()
    {
    }


    public DuplicateCatalogItemCodesViewModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DuplicateCatalogItemCodesViewModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getCatalogVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("catalogVersion");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "count", type = Accessor.Type.GETTER)
    public Integer getCount()
    {
        return (Integer)getPersistenceContext().getPropertyValue("count");
    }


    @Accessor(qualifier = "cv", type = Accessor.Type.GETTER)
    public CatalogVersionModel getCv()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("cv");
    }


    @Accessor(qualifier = "typeCode", type = Accessor.Type.GETTER)
    public ComposedTypeModel getTypeCode()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("typeCode");
    }
}
