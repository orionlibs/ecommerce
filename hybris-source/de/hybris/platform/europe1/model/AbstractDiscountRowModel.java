package de.hybris.platform.europe1.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.enums.ProductDiscountGroup;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AbstractDiscountRowModel extends PDTRowModel
{
    public static final String _TYPECODE = "AbstractDiscountRow";
    public static final String CURRENCY = "currency";
    public static final String DISCOUNT = "discount";
    public static final String ABSOLUTE = "absolute";
    public static final String VALUE = "value";
    public static final String DISCOUNTSTRING = "discountString";


    public AbstractDiscountRowModel()
    {
    }


    public AbstractDiscountRowModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractDiscountRowModel(DiscountModel _discount)
    {
        setDiscount(_discount);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractDiscountRowModel(DiscountModel _discount, ItemModel _owner, ProductDiscountGroup _pg, ProductModel _product, String _productId)
    {
        setDiscount(_discount);
        setOwner(_owner);
        setPg((HybrisEnumValue)_pg);
        setProduct(_product);
        setProductId(_productId);
    }


    @Accessor(qualifier = "absolute", type = Accessor.Type.GETTER)
    public Boolean getAbsolute()
    {
        return (Boolean)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "absolute");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
    }


    @Accessor(qualifier = "discount", type = Accessor.Type.GETTER)
    public DiscountModel getDiscount()
    {
        return (DiscountModel)getPersistenceContext().getPropertyValue("discount");
    }


    @Accessor(qualifier = "discountString", type = Accessor.Type.GETTER)
    public String getDiscountString()
    {
        return (String)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "discountString");
    }


    @Accessor(qualifier = "value", type = Accessor.Type.GETTER)
    public Double getValue()
    {
        return (Double)getPersistenceContext().getPropertyValue("value");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
    public void setCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("currency", value);
    }


    @Accessor(qualifier = "discount", type = Accessor.Type.SETTER)
    public void setDiscount(DiscountModel value)
    {
        getPersistenceContext().setPropertyValue("discount", value);
    }


    @Accessor(qualifier = "pg", type = Accessor.Type.SETTER)
    public void setPg(HybrisEnumValue value)
    {
        if(value == null || value instanceof ProductDiscountGroup)
        {
            super.setPg(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.europe1.enums.ProductDiscountGroup");
        }
    }


    @Accessor(qualifier = "value", type = Accessor.Type.SETTER)
    public void setValue(Double value)
    {
        getPersistenceContext().setPropertyValue("value", value);
    }
}
