package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

public class OrderThresholdPerfectPartnerPromotionModel extends OrderPromotionModel
{
    public static final String _TYPECODE = "OrderThresholdPerfectPartnerPromotion";
    public static final String THRESHOLDTOTALS = "thresholdTotals";
    public static final String DISCOUNTPRODUCT = "discountProduct";
    public static final String PRODUCTPRICES = "productPrices";
    public static final String INCLUDEDISCOUNTEDPRICEINTHRESHOLD = "includeDiscountedPriceInThreshold";
    public static final String MESSAGEFIRED = "messageFired";
    public static final String MESSAGECOULDHAVEFIRED = "messageCouldHaveFired";
    public static final String MESSAGEPRODUCTNOTHRESHOLD = "messageProductNoThreshold";
    public static final String MESSAGETHRESHOLDNOPRODUCT = "messageThresholdNoProduct";


    public OrderThresholdPerfectPartnerPromotionModel()
    {
    }


    public OrderThresholdPerfectPartnerPromotionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderThresholdPerfectPartnerPromotionModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderThresholdPerfectPartnerPromotionModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "discountProduct", type = Accessor.Type.GETTER)
    public ProductModel getDiscountProduct()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("discountProduct");
    }


    @Accessor(qualifier = "includeDiscountedPriceInThreshold", type = Accessor.Type.GETTER)
    public Boolean getIncludeDiscountedPriceInThreshold()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("includeDiscountedPriceInThreshold");
    }


    @Accessor(qualifier = "messageCouldHaveFired", type = Accessor.Type.GETTER)
    public String getMessageCouldHaveFired()
    {
        return getMessageCouldHaveFired(null);
    }


    @Accessor(qualifier = "messageCouldHaveFired", type = Accessor.Type.GETTER)
    public String getMessageCouldHaveFired(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("messageCouldHaveFired", loc);
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


    @Accessor(qualifier = "messageProductNoThreshold", type = Accessor.Type.GETTER)
    public String getMessageProductNoThreshold()
    {
        return getMessageProductNoThreshold(null);
    }


    @Accessor(qualifier = "messageProductNoThreshold", type = Accessor.Type.GETTER)
    public String getMessageProductNoThreshold(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("messageProductNoThreshold", loc);
    }


    @Accessor(qualifier = "messageThresholdNoProduct", type = Accessor.Type.GETTER)
    public String getMessageThresholdNoProduct()
    {
        return getMessageThresholdNoProduct(null);
    }


    @Accessor(qualifier = "messageThresholdNoProduct", type = Accessor.Type.GETTER)
    public String getMessageThresholdNoProduct(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("messageThresholdNoProduct", loc);
    }


    @Accessor(qualifier = "productPrices", type = Accessor.Type.GETTER)
    public Collection<PromotionPriceRowModel> getProductPrices()
    {
        return (Collection<PromotionPriceRowModel>)getPersistenceContext().getPropertyValue("productPrices");
    }


    @Accessor(qualifier = "thresholdTotals", type = Accessor.Type.GETTER)
    public Collection<PromotionPriceRowModel> getThresholdTotals()
    {
        return (Collection<PromotionPriceRowModel>)getPersistenceContext().getPropertyValue("thresholdTotals");
    }


    @Accessor(qualifier = "discountProduct", type = Accessor.Type.SETTER)
    public void setDiscountProduct(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("discountProduct", value);
    }


    @Accessor(qualifier = "includeDiscountedPriceInThreshold", type = Accessor.Type.SETTER)
    public void setIncludeDiscountedPriceInThreshold(Boolean value)
    {
        getPersistenceContext().setPropertyValue("includeDiscountedPriceInThreshold", value);
    }


    @Accessor(qualifier = "messageCouldHaveFired", type = Accessor.Type.SETTER)
    public void setMessageCouldHaveFired(String value)
    {
        setMessageCouldHaveFired(value, null);
    }


    @Accessor(qualifier = "messageCouldHaveFired", type = Accessor.Type.SETTER)
    public void setMessageCouldHaveFired(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("messageCouldHaveFired", loc, value);
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


    @Accessor(qualifier = "messageProductNoThreshold", type = Accessor.Type.SETTER)
    public void setMessageProductNoThreshold(String value)
    {
        setMessageProductNoThreshold(value, null);
    }


    @Accessor(qualifier = "messageProductNoThreshold", type = Accessor.Type.SETTER)
    public void setMessageProductNoThreshold(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("messageProductNoThreshold", loc, value);
    }


    @Accessor(qualifier = "messageThresholdNoProduct", type = Accessor.Type.SETTER)
    public void setMessageThresholdNoProduct(String value)
    {
        setMessageThresholdNoProduct(value, null);
    }


    @Accessor(qualifier = "messageThresholdNoProduct", type = Accessor.Type.SETTER)
    public void setMessageThresholdNoProduct(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("messageThresholdNoProduct", loc, value);
    }


    @Accessor(qualifier = "productPrices", type = Accessor.Type.SETTER)
    public void setProductPrices(Collection<PromotionPriceRowModel> value)
    {
        getPersistenceContext().setPropertyValue("productPrices", value);
    }


    @Accessor(qualifier = "thresholdTotals", type = Accessor.Type.SETTER)
    public void setThresholdTotals(Collection<PromotionPriceRowModel> value)
    {
        getPersistenceContext().setPropertyValue("thresholdTotals", value);
    }
}
