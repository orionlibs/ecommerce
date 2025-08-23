package de.hybris.platform.b2bacceleratorservices.model.promotions;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.b2bacceleratorservices.enums.CheckoutPaymentType;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionPriceRowModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

public class ProductPriceDiscountPromotionByPaymentTypeModel extends ProductPromotionModel
{
    public static final String _TYPECODE = "ProductPriceDiscountPromotionByPaymentType";
    public static final String PRODUCTDISCOUNTPRICE = "productDiscountPrice";
    public static final String MESSAGEFIRED = "messageFired";
    public static final String MESSAGECOULDHAVEFIRED = "messageCouldHaveFired";
    public static final String PAYMENTTYPE = "paymentType";


    public ProductPriceDiscountPromotionByPaymentTypeModel()
    {
    }


    public ProductPriceDiscountPromotionByPaymentTypeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductPriceDiscountPromotionByPaymentTypeModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductPriceDiscountPromotionByPaymentTypeModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
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


    @Accessor(qualifier = "paymentType", type = Accessor.Type.GETTER)
    public CheckoutPaymentType getPaymentType()
    {
        return (CheckoutPaymentType)getPersistenceContext().getPropertyValue("paymentType");
    }


    @Accessor(qualifier = "productDiscountPrice", type = Accessor.Type.GETTER)
    public Collection<PromotionPriceRowModel> getProductDiscountPrice()
    {
        return (Collection<PromotionPriceRowModel>)getPersistenceContext().getPropertyValue("productDiscountPrice");
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


    @Accessor(qualifier = "paymentType", type = Accessor.Type.SETTER)
    public void setPaymentType(CheckoutPaymentType value)
    {
        getPersistenceContext().setPropertyValue("paymentType", value);
    }


    @Accessor(qualifier = "productDiscountPrice", type = Accessor.Type.SETTER)
    public void setProductDiscountPrice(Collection<PromotionPriceRowModel> value)
    {
        getPersistenceContext().setPropertyValue("productDiscountPrice", value);
    }
}
