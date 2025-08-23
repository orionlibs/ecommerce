package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

public class ProductFixedPricePromotionModel extends ProductPromotionModel
{
    public static final String _TYPECODE = "ProductFixedPricePromotion";
    public static final String PRODUCTFIXEDUNITPRICE = "productFixedUnitPrice";
    public static final String MESSAGEFIRED = "messageFired";


    public ProductFixedPricePromotionModel()
    {
    }


    public ProductFixedPricePromotionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductFixedPricePromotionModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductFixedPricePromotionModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "messageFired", type = Accessor.Type.GETTER)
    public String getMessageFired()
    {
        return getMessageFired(null);
    }


    @Accessor(qualifier = "messageFired", type = Accessor.Type.GETTER)
    public String getMessageFired(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("messageFired", loc);
    }


    @Accessor(qualifier = "productFixedUnitPrice", type = Accessor.Type.GETTER)
    public Collection<PromotionPriceRowModel> getProductFixedUnitPrice()
    {
        return (Collection<PromotionPriceRowModel>)getPersistenceContext().getPropertyValue("productFixedUnitPrice");
    }


    @Accessor(qualifier = "messageFired", type = Accessor.Type.SETTER)
    public void setMessageFired(String value)
    {
        setMessageFired(value, null);
    }


    @Accessor(qualifier = "messageFired", type = Accessor.Type.SETTER)
    public void setMessageFired(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("messageFired", loc, value);
    }


    @Accessor(qualifier = "productFixedUnitPrice", type = Accessor.Type.SETTER)
    public void setProductFixedUnitPrice(Collection<PromotionPriceRowModel> value)
    {
        getPersistenceContext().setPropertyValue("productFixedUnitPrice", value);
    }
}
