package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class PromotionOrderEntryConsumedModel extends ItemModel
{
    public static final String _TYPECODE = "PromotionOrderEntryConsumed";
    public static final String CODE = "code";
    public static final String PROMOTIONRESULT = "promotionResult";
    public static final String QUANTITY = "quantity";
    public static final String ADJUSTEDUNITPRICE = "adjustedUnitPrice";
    public static final String ORDERENTRY = "orderEntry";
    public static final String ORDERENTRYNUMBER = "orderEntryNumber";
    public static final String ORDERENTRYNUMBERWITHFALLBACK = "orderEntryNumberWithFallback";


    public PromotionOrderEntryConsumedModel()
    {
    }


    public PromotionOrderEntryConsumedModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionOrderEntryConsumedModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "adjustedUnitPrice", type = Accessor.Type.GETTER)
    public Double getAdjustedUnitPrice()
    {
        return (Double)getPersistenceContext().getPropertyValue("adjustedUnitPrice");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "orderEntry", type = Accessor.Type.GETTER)
    public AbstractOrderEntryModel getOrderEntry()
    {
        return (AbstractOrderEntryModel)getPersistenceContext().getPropertyValue("orderEntry");
    }


    @Accessor(qualifier = "orderEntryNumber", type = Accessor.Type.GETTER)
    public Integer getOrderEntryNumber()
    {
        return (Integer)getPersistenceContext().getPropertyValue("orderEntryNumber");
    }


    @Accessor(qualifier = "orderEntryNumberWithFallback", type = Accessor.Type.GETTER)
    public Integer getOrderEntryNumberWithFallback()
    {
        return (Integer)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "orderEntryNumberWithFallback");
    }


    @Accessor(qualifier = "promotionResult", type = Accessor.Type.GETTER)
    public PromotionResultModel getPromotionResult()
    {
        return (PromotionResultModel)getPersistenceContext().getPropertyValue("promotionResult");
    }


    @Accessor(qualifier = "quantity", type = Accessor.Type.GETTER)
    public Long getQuantity()
    {
        return (Long)getPersistenceContext().getPropertyValue("quantity");
    }


    @Accessor(qualifier = "adjustedUnitPrice", type = Accessor.Type.SETTER)
    public void setAdjustedUnitPrice(Double value)
    {
        getPersistenceContext().setPropertyValue("adjustedUnitPrice", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "orderEntry", type = Accessor.Type.SETTER)
    public void setOrderEntry(AbstractOrderEntryModel value)
    {
        getPersistenceContext().setPropertyValue("orderEntry", value);
    }


    @Accessor(qualifier = "orderEntryNumber", type = Accessor.Type.SETTER)
    public void setOrderEntryNumber(Integer value)
    {
        getPersistenceContext().setPropertyValue("orderEntryNumber", value);
    }


    @Accessor(qualifier = "promotionResult", type = Accessor.Type.SETTER)
    public void setPromotionResult(PromotionResultModel value)
    {
        getPersistenceContext().setPropertyValue("promotionResult", value);
    }


    @Accessor(qualifier = "quantity", type = Accessor.Type.SETTER)
    public void setQuantity(Long value)
    {
        getPersistenceContext().setPropertyValue("quantity", value);
    }
}
