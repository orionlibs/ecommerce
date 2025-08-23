package de.hybris.platform.voucher.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class ProductRestrictionModel extends RestrictionModel
{
    public static final String _TYPECODE = "ProductRestriction";
    public static final String PRODUCTS = "products";


    public ProductRestrictionModel()
    {
    }


    public ProductRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductRestrictionModel(VoucherModel _voucher)
    {
        setVoucher(_voucher);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductRestrictionModel(ItemModel _owner, VoucherModel _voucher)
    {
        setOwner(_owner);
        setVoucher(_voucher);
    }


    @Accessor(qualifier = "products", type = Accessor.Type.GETTER)
    public Collection<ProductModel> getProducts()
    {
        return (Collection<ProductModel>)getPersistenceContext().getPropertyValue("products");
    }


    @Accessor(qualifier = "products", type = Accessor.Type.SETTER)
    public void setProducts(Collection<ProductModel> value)
    {
        getPersistenceContext().setPropertyValue("products", value);
    }
}
