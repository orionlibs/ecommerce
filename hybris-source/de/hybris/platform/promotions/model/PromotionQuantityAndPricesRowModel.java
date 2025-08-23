package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class PromotionQuantityAndPricesRowModel extends ItemModel
{
    public static final String _TYPECODE = "PromotionQuantityAndPricesRow";
    public static final String QUANTITY = "quantity";
    public static final String PRICES = "prices";


    public PromotionQuantityAndPricesRowModel()
    {
    }


    public PromotionQuantityAndPricesRowModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionQuantityAndPricesRowModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "prices", type = Accessor.Type.GETTER)
    public Collection<PromotionPriceRowModel> getPrices()
    {
        return (Collection<PromotionPriceRowModel>)getPersistenceContext().getPropertyValue("prices");
    }


    @Accessor(qualifier = "quantity", type = Accessor.Type.GETTER)
    public Long getQuantity()
    {
        return (Long)getPersistenceContext().getPropertyValue("quantity");
    }


    @Accessor(qualifier = "prices", type = Accessor.Type.SETTER)
    public void setPrices(Collection<PromotionPriceRowModel> value)
    {
        getPersistenceContext().setPropertyValue("prices", value);
    }


    @Accessor(qualifier = "quantity", type = Accessor.Type.SETTER)
    public void setQuantity(Long value)
    {
        getPersistenceContext().setPropertyValue("quantity", value);
    }
}
