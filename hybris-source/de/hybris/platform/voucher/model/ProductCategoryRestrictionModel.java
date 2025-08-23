package de.hybris.platform.voucher.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class ProductCategoryRestrictionModel extends ProductRestrictionModel
{
    public static final String _TYPECODE = "ProductCategoryRestriction";
    public static final String CATEGORIES = "categories";


    public ProductCategoryRestrictionModel()
    {
    }


    public ProductCategoryRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductCategoryRestrictionModel(VoucherModel _voucher)
    {
        setVoucher(_voucher);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductCategoryRestrictionModel(ItemModel _owner, VoucherModel _voucher)
    {
        setOwner(_owner);
        setVoucher(_voucher);
    }


    @Accessor(qualifier = "categories", type = Accessor.Type.GETTER)
    public Collection<CategoryModel> getCategories()
    {
        return (Collection<CategoryModel>)getPersistenceContext().getPropertyValue("categories");
    }


    @Accessor(qualifier = "categories", type = Accessor.Type.SETTER)
    public void setCategories(Collection<CategoryModel> value)
    {
        getPersistenceContext().setPropertyValue("categories", value);
    }
}
