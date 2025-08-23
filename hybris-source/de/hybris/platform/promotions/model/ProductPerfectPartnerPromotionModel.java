package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

public class ProductPerfectPartnerPromotionModel extends ProductPromotionModel
{
    public static final String _TYPECODE = "ProductPerfectPartnerPromotion";
    public static final String PARTNERPRODUCTS = "partnerProducts";
    public static final String PARTNERPRICES = "partnerPrices";
    public static final String MESSAGEFIRED = "messageFired";
    public static final String MESSAGECOULDHAVEFIRED = "messageCouldHaveFired";


    public ProductPerfectPartnerPromotionModel()
    {
    }


    public ProductPerfectPartnerPromotionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductPerfectPartnerPromotionModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductPerfectPartnerPromotionModel(String _code, ItemModel _owner)
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


    @Accessor(qualifier = "partnerPrices", type = Accessor.Type.GETTER)
    public Collection<PromotionPriceRowModel> getPartnerPrices()
    {
        return (Collection<PromotionPriceRowModel>)getPersistenceContext().getPropertyValue("partnerPrices");
    }


    @Accessor(qualifier = "partnerProducts", type = Accessor.Type.GETTER)
    public Collection<ProductModel> getPartnerProducts()
    {
        return (Collection<ProductModel>)getPersistenceContext().getPropertyValue("partnerProducts");
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


    @Accessor(qualifier = "partnerPrices", type = Accessor.Type.SETTER)
    public void setPartnerPrices(Collection<PromotionPriceRowModel> value)
    {
        getPersistenceContext().setPropertyValue("partnerPrices", value);
    }


    @Accessor(qualifier = "partnerProducts", type = Accessor.Type.SETTER)
    public void setPartnerProducts(Collection<ProductModel> value)
    {
        getPersistenceContext().setPropertyValue("partnerProducts", value);
    }
}
