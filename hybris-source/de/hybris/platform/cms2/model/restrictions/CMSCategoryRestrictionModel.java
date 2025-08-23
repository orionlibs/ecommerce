package de.hybris.platform.cms2.model.restrictions;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;

public class CMSCategoryRestrictionModel extends AbstractRestrictionModel
{
    public static final String _TYPECODE = "CMSCategoryRestriction";
    public static final String RECURSIVE = "recursive";
    public static final String CATEGORYCODES = "categoryCodes";
    public static final String CATEGORIES = "categories";


    public CMSCategoryRestrictionModel()
    {
    }


    public CMSCategoryRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSCategoryRestrictionModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSCategoryRestrictionModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "categories", type = Accessor.Type.GETTER)
    public Collection<CategoryModel> getCategories()
    {
        return (Collection<CategoryModel>)getPersistenceContext().getPropertyValue("categories");
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "categoryCodes", type = Accessor.Type.GETTER)
    public List<String> getCategoryCodes()
    {
        return (List<String>)getPersistenceContext().getPropertyValue("categoryCodes");
    }


    @Accessor(qualifier = "recursive", type = Accessor.Type.GETTER)
    public boolean isRecursive()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("recursive"));
    }


    @Accessor(qualifier = "categories", type = Accessor.Type.SETTER)
    public void setCategories(Collection<CategoryModel> value)
    {
        getPersistenceContext().setPropertyValue("categories", value);
    }


    @Accessor(qualifier = "recursive", type = Accessor.Type.SETTER)
    public void setRecursive(boolean value)
    {
        getPersistenceContext().setPropertyValue("recursive", toObject(value));
    }
}
