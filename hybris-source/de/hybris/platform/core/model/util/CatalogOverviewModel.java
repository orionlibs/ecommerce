package de.hybris.platform.core.model.util;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CatalogOverviewModel extends ItemModel
{
    public static final String _TYPECODE = "CatalogOverview";
    public static final String CAT = "cat";
    public static final String TYPECODE = "typeCode";
    public static final String AMOUNT = "amount";
    public static final String CATALOGVERSION = "catalogversion";


    public CatalogOverviewModel()
    {
    }


    public CatalogOverviewModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CatalogOverviewModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "amount", type = Accessor.Type.GETTER)
    public Integer getAmount()
    {
        return (Integer)getPersistenceContext().getPropertyValue("amount");
    }


    @Accessor(qualifier = "cat", type = Accessor.Type.GETTER)
    public CatalogModel getCat()
    {
        return (CatalogModel)getPersistenceContext().getPropertyValue("cat");
    }


    @Accessor(qualifier = "catalogversion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getCatalogversion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("catalogversion");
    }


    @Accessor(qualifier = "typeCode", type = Accessor.Type.GETTER)
    public ComposedTypeModel getTypeCode()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("typeCode");
    }
}
