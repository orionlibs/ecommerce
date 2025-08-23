package de.hybris.platform.acceleratorcms.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class PurchasedCategorySuggestionComponentModel extends SimpleSuggestionComponentModel
{
    public static final String _TYPECODE = "PurchasedCategorySuggestionComponent";
    public static final String CATEGORY = "category";


    public PurchasedCategorySuggestionComponentModel()
    {
    }


    public PurchasedCategorySuggestionComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PurchasedCategorySuggestionComponentModel(CatalogVersionModel _catalogVersion, CategoryModel _category, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setCategory(_category);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PurchasedCategorySuggestionComponentModel(CatalogVersionModel _catalogVersion, CategoryModel _category, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setCategory(_category);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "category", type = Accessor.Type.GETTER)
    public CategoryModel getCategory()
    {
        return (CategoryModel)getPersistenceContext().getPropertyValue("category");
    }


    @Accessor(qualifier = "category", type = Accessor.Type.SETTER)
    public void setCategory(CategoryModel value)
    {
        getPersistenceContext().setPropertyValue("category", value);
    }
}
