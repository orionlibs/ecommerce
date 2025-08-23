package de.hybris.platform.variants.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class VariantValueCategoryModel extends CategoryModel
{
    public static final String _TYPECODE = "VariantValueCategory";
    public static final String SEQUENCE = "sequence";


    public VariantValueCategoryModel()
    {
    }


    public VariantValueCategoryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public VariantValueCategoryModel(CatalogVersionModel _catalogVersion, String _code)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public VariantValueCategoryModel(CatalogVersionModel _catalogVersion, String _code, ItemModel _owner)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "sequence", type = Accessor.Type.GETTER)
    public Integer getSequence()
    {
        return (Integer)getPersistenceContext().getPropertyValue("sequence");
    }


    @Accessor(qualifier = "sequence", type = Accessor.Type.SETTER)
    public void setSequence(Integer value)
    {
        getPersistenceContext().setPropertyValue("sequence", value);
    }
}
