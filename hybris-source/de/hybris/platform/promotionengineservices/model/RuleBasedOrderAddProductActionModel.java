package de.hybris.platform.promotionengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class RuleBasedOrderAddProductActionModel extends AbstractRuleBasedPromotionActionModel
{
    public static final String _TYPECODE = "RuleBasedOrderAddProductAction";
    public static final String PRODUCT = "product";
    public static final String QUANTITY = "quantity";


    public RuleBasedOrderAddProductActionModel()
    {
    }


    public RuleBasedOrderAddProductActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RuleBasedOrderAddProductActionModel(ProductModel _product, Long _quantity)
    {
        setProduct(_product);
        setQuantity(_quantity);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RuleBasedOrderAddProductActionModel(ItemModel _owner, ProductModel _product, Long _quantity)
    {
        setOwner(_owner);
        setProduct(_product);
        setQuantity(_quantity);
    }


    @Accessor(qualifier = "product", type = Accessor.Type.GETTER)
    public ProductModel getProduct()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("product");
    }


    @Accessor(qualifier = "quantity", type = Accessor.Type.GETTER)
    public Long getQuantity()
    {
        return (Long)getPersistenceContext().getPropertyValue("quantity");
    }


    @Accessor(qualifier = "product", type = Accessor.Type.SETTER)
    public void setProduct(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("product", value);
    }


    @Accessor(qualifier = "quantity", type = Accessor.Type.SETTER)
    public void setQuantity(Long value)
    {
        getPersistenceContext().setPropertyValue("quantity", value);
    }
}
