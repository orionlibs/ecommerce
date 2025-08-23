package de.hybris.platform.variants.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class VariantCategoryModel extends CategoryModel
{
    public static final String _TYPECODE = "VariantCategory";
    public static final String HASIMAGE = "hasImage";


    public VariantCategoryModel()
    {
    }


    public VariantCategoryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public VariantCategoryModel(CatalogVersionModel _catalogVersion, String _code)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public VariantCategoryModel(CatalogVersionModel _catalogVersion, String _code, ItemModel _owner)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "hasImage", type = Accessor.Type.GETTER)
    public Boolean getHasImage()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("hasImage");
    }


    @Accessor(qualifier = "hasImage", type = Accessor.Type.SETTER)
    public void setHasImage(Boolean value)
    {
        getPersistenceContext().setPropertyValue("hasImage", value);
    }
}
