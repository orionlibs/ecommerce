package de.hybris.platform.europe1.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.enums.ProductDiscountGroup;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class DiscountRowModel extends AbstractDiscountRowModel
{
    public static final String _TYPECODE = "DiscountRow";
    public static final String _PRODUCT2OWNEUROPE1DISCOUNTS = "Product2OwnEurope1Discounts";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String ASTARGETPRICE = "asTargetPrice";
    public static final String SAPCONDITIONID = "sapConditionId";


    public DiscountRowModel()
    {
    }


    public DiscountRowModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DiscountRowModel(DiscountModel _discount)
    {
        setDiscount(_discount);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DiscountRowModel(DiscountModel _discount, ItemModel _owner, ProductDiscountGroup _pg, ProductModel _product, String _productId)
    {
        setDiscount(_discount);
        setOwner(_owner);
        setPg((HybrisEnumValue)_pg);
        setProduct(_product);
        setProductId(_productId);
    }


    @Accessor(qualifier = "asTargetPrice", type = Accessor.Type.GETTER)
    public Boolean getAsTargetPrice()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("asTargetPrice");
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getCatalogVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("catalogVersion");
    }


    @Accessor(qualifier = "sapConditionId", type = Accessor.Type.GETTER)
    public String getSapConditionId()
    {
        return (String)getPersistenceContext().getPropertyValue("sapConditionId");
    }


    @Accessor(qualifier = "asTargetPrice", type = Accessor.Type.SETTER)
    public void setAsTargetPrice(Boolean value)
    {
        getPersistenceContext().setPropertyValue("asTargetPrice", value);
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
    public void setCatalogVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("catalogVersion", value);
    }


    @Accessor(qualifier = "product", type = Accessor.Type.SETTER)
    public void setProduct(ProductModel value)
    {
        super.setProduct(value);
    }


    @Accessor(qualifier = "sapConditionId", type = Accessor.Type.SETTER)
    public void setSapConditionId(String value)
    {
        getPersistenceContext().setPropertyValue("sapConditionId", value);
    }
}
