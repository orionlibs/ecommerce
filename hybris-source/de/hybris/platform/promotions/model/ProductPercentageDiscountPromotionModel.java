package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class ProductPercentageDiscountPromotionModel extends ProductPromotionModel
{
    public static final String _TYPECODE = "ProductPercentageDiscountPromotion";
    public static final String PERCENTAGEDISCOUNT = "percentageDiscount";
    public static final String MESSAGEFIRED = "messageFired";


    public ProductPercentageDiscountPromotionModel()
    {
    }


    public ProductPercentageDiscountPromotionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductPercentageDiscountPromotionModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductPercentageDiscountPromotionModel(String _code, ItemModel _owner)
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


    @Accessor(qualifier = "percentageDiscount", type = Accessor.Type.GETTER)
    public Double getPercentageDiscount()
    {
        return (Double)getPersistenceContext().getPropertyValue("percentageDiscount");
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


    @Accessor(qualifier = "percentageDiscount", type = Accessor.Type.SETTER)
    public void setPercentageDiscount(Double value)
    {
        getPersistenceContext().setPropertyValue("percentageDiscount", value);
    }
}
