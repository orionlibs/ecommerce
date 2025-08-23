package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class PromotionPriceRowModel extends ItemModel
{
    public static final String _TYPECODE = "PromotionPriceRow";
    public static final String CURRENCY = "currency";
    public static final String PRICE = "price";


    public PromotionPriceRowModel()
    {
    }


    public PromotionPriceRowModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionPriceRowModel(CurrencyModel _currency)
    {
        setCurrency(_currency);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionPriceRowModel(CurrencyModel _currency, ItemModel _owner)
    {
        setCurrency(_currency);
        setOwner(_owner);
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
    }


    @Accessor(qualifier = "price", type = Accessor.Type.GETTER)
    public Double getPrice()
    {
        return (Double)getPersistenceContext().getPropertyValue("price");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
    public void setCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("currency", value);
    }


    @Accessor(qualifier = "price", type = Accessor.Type.SETTER)
    public void setPrice(Double value)
    {
        getPersistenceContext().setPropertyValue("price", value);
    }
}
