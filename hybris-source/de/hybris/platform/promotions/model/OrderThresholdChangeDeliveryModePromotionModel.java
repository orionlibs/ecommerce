package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

public class OrderThresholdChangeDeliveryModePromotionModel extends OrderPromotionModel
{
    public static final String _TYPECODE = "OrderThresholdChangeDeliveryModePromotion";
    public static final String THRESHOLDTOTALS = "thresholdTotals";
    public static final String DELIVERYMODE = "deliveryMode";
    public static final String MESSAGEFIRED = "messageFired";
    public static final String MESSAGECOULDHAVEFIRED = "messageCouldHaveFired";


    public OrderThresholdChangeDeliveryModePromotionModel()
    {
    }


    public OrderThresholdChangeDeliveryModePromotionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderThresholdChangeDeliveryModePromotionModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderThresholdChangeDeliveryModePromotionModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "deliveryMode", type = Accessor.Type.GETTER)
    public DeliveryModeModel getDeliveryMode()
    {
        return (DeliveryModeModel)getPersistenceContext().getPropertyValue("deliveryMode");
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


    @Accessor(qualifier = "thresholdTotals", type = Accessor.Type.GETTER)
    public Collection<PromotionPriceRowModel> getThresholdTotals()
    {
        return (Collection<PromotionPriceRowModel>)getPersistenceContext().getPropertyValue("thresholdTotals");
    }


    @Accessor(qualifier = "deliveryMode", type = Accessor.Type.SETTER)
    public void setDeliveryMode(DeliveryModeModel value)
    {
        getPersistenceContext().setPropertyValue("deliveryMode", value);
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


    @Accessor(qualifier = "thresholdTotals", type = Accessor.Type.SETTER)
    public void setThresholdTotals(Collection<PromotionPriceRowModel> value)
    {
        getPersistenceContext().setPropertyValue("thresholdTotals", value);
    }
}
