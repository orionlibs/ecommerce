package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class PromotionProductRestrictionModel extends AbstractPromotionRestrictionModel
{
    public static final String _TYPECODE = "PromotionProductRestriction";
    public static final String PRODUCTS = "products";


    public PromotionProductRestrictionModel()
    {
    }


    public PromotionProductRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionProductRestrictionModel(ItemModel _owner)
    {
        setOwner(_owner);
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
