package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

public class ProductPerfectPartnerBundlePromotionModel extends ProductPromotionModel
{
    public static final String _TYPECODE = "ProductPerfectPartnerBundlePromotion";
    public static final String BASEPRODUCT = "baseProduct";
    public static final String PARTNERPRODUCTS = "partnerProducts";
    public static final String QUALIFYINGCOUNT = "qualifyingCount";
    public static final String BUNDLEPRICES = "bundlePrices";
    public static final String MESSAGEFIRED = "messageFired";
    public static final String MESSAGECOULDHAVEFIRED = "messageCouldHaveFired";


    public ProductPerfectPartnerBundlePromotionModel()
    {
    }


    public ProductPerfectPartnerBundlePromotionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductPerfectPartnerBundlePromotionModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductPerfectPartnerBundlePromotionModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "baseProduct", type = Accessor.Type.GETTER)
    public ProductModel getBaseProduct()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("baseProduct");
    }


    @Accessor(qualifier = "bundlePrices", type = Accessor.Type.GETTER)
    public Collection<PromotionPriceRowModel> getBundlePrices()
    {
        return (Collection<PromotionPriceRowModel>)getPersistenceContext().getPropertyValue("bundlePrices");
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


    @Accessor(qualifier = "partnerProducts", type = Accessor.Type.GETTER)
    public Collection<ProductModel> getPartnerProducts()
    {
        return (Collection<ProductModel>)getPersistenceContext().getPropertyValue("partnerProducts");
    }


    @Accessor(qualifier = "qualifyingCount", type = Accessor.Type.GETTER)
    public Integer getQualifyingCount()
    {
        return (Integer)getPersistenceContext().getPropertyValue("qualifyingCount");
    }


    @Accessor(qualifier = "baseProduct", type = Accessor.Type.SETTER)
    public void setBaseProduct(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("baseProduct", value);
    }


    @Accessor(qualifier = "bundlePrices", type = Accessor.Type.SETTER)
    public void setBundlePrices(Collection<PromotionPriceRowModel> value)
    {
        getPersistenceContext().setPropertyValue("bundlePrices", value);
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


    @Accessor(qualifier = "partnerProducts", type = Accessor.Type.SETTER)
    public void setPartnerProducts(Collection<ProductModel> value)
    {
        getPersistenceContext().setPropertyValue("partnerProducts", value);
    }


    @Accessor(qualifier = "qualifyingCount", type = Accessor.Type.SETTER)
    public void setQualifyingCount(Integer value)
    {
        getPersistenceContext().setPropertyValue("qualifyingCount", value);
    }
}
