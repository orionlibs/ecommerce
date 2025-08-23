package de.hybris.platform.ordercancel.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OrderCancelConfigModel extends ItemModel
{
    public static final String _TYPECODE = "OrderCancelConfig";
    public static final String ORDERCANCELALLOWED = "orderCancelAllowed";
    public static final String CANCELAFTERWAREHOUSEALLOWED = "cancelAfterWarehouseAllowed";
    public static final String COMPLETECANCELAFTERSHIPPINGSTARTEDALLOWED = "completeCancelAfterShippingStartedAllowed";
    public static final String PARTIALCANCELALLOWED = "partialCancelAllowed";
    public static final String PARTIALORDERENTRYCANCELALLOWED = "partialOrderEntryCancelAllowed";
    public static final String QUEUEDORDERWAITINGTIME = "queuedOrderWaitingTime";


    public OrderCancelConfigModel()
    {
    }


    public OrderCancelConfigModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderCancelConfigModel(boolean _cancelAfterWarehouseAllowed, boolean _completeCancelAfterShippingStartedAllowed, boolean _orderCancelAllowed, boolean _partialCancelAllowed, boolean _partialOrderEntryCancelAllowed, int _queuedOrderWaitingTime)
    {
        setCancelAfterWarehouseAllowed(_cancelAfterWarehouseAllowed);
        setCompleteCancelAfterShippingStartedAllowed(_completeCancelAfterShippingStartedAllowed);
        setOrderCancelAllowed(_orderCancelAllowed);
        setPartialCancelAllowed(_partialCancelAllowed);
        setPartialOrderEntryCancelAllowed(_partialOrderEntryCancelAllowed);
        setQueuedOrderWaitingTime(_queuedOrderWaitingTime);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderCancelConfigModel(boolean _cancelAfterWarehouseAllowed, boolean _completeCancelAfterShippingStartedAllowed, boolean _orderCancelAllowed, ItemModel _owner, boolean _partialCancelAllowed, boolean _partialOrderEntryCancelAllowed, int _queuedOrderWaitingTime)
    {
        setCancelAfterWarehouseAllowed(_cancelAfterWarehouseAllowed);
        setCompleteCancelAfterShippingStartedAllowed(_completeCancelAfterShippingStartedAllowed);
        setOrderCancelAllowed(_orderCancelAllowed);
        setOwner(_owner);
        setPartialCancelAllowed(_partialCancelAllowed);
        setPartialOrderEntryCancelAllowed(_partialOrderEntryCancelAllowed);
        setQueuedOrderWaitingTime(_queuedOrderWaitingTime);
    }


    @Accessor(qualifier = "queuedOrderWaitingTime", type = Accessor.Type.GETTER)
    public int getQueuedOrderWaitingTime()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("queuedOrderWaitingTime"));
    }


    @Accessor(qualifier = "cancelAfterWarehouseAllowed", type = Accessor.Type.GETTER)
    public boolean isCancelAfterWarehouseAllowed()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("cancelAfterWarehouseAllowed"));
    }


    @Accessor(qualifier = "completeCancelAfterShippingStartedAllowed", type = Accessor.Type.GETTER)
    public boolean isCompleteCancelAfterShippingStartedAllowed()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("completeCancelAfterShippingStartedAllowed"));
    }


    @Accessor(qualifier = "orderCancelAllowed", type = Accessor.Type.GETTER)
    public boolean isOrderCancelAllowed()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("orderCancelAllowed"));
    }


    @Accessor(qualifier = "partialCancelAllowed", type = Accessor.Type.GETTER)
    public boolean isPartialCancelAllowed()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("partialCancelAllowed"));
    }


    @Accessor(qualifier = "partialOrderEntryCancelAllowed", type = Accessor.Type.GETTER)
    public boolean isPartialOrderEntryCancelAllowed()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("partialOrderEntryCancelAllowed"));
    }


    @Accessor(qualifier = "cancelAfterWarehouseAllowed", type = Accessor.Type.SETTER)
    public void setCancelAfterWarehouseAllowed(boolean value)
    {
        getPersistenceContext().setPropertyValue("cancelAfterWarehouseAllowed", toObject(value));
    }


    @Accessor(qualifier = "completeCancelAfterShippingStartedAllowed", type = Accessor.Type.SETTER)
    public void setCompleteCancelAfterShippingStartedAllowed(boolean value)
    {
        getPersistenceContext().setPropertyValue("completeCancelAfterShippingStartedAllowed", toObject(value));
    }


    @Accessor(qualifier = "orderCancelAllowed", type = Accessor.Type.SETTER)
    public void setOrderCancelAllowed(boolean value)
    {
        getPersistenceContext().setPropertyValue("orderCancelAllowed", toObject(value));
    }


    @Accessor(qualifier = "partialCancelAllowed", type = Accessor.Type.SETTER)
    public void setPartialCancelAllowed(boolean value)
    {
        getPersistenceContext().setPropertyValue("partialCancelAllowed", toObject(value));
    }


    @Accessor(qualifier = "partialOrderEntryCancelAllowed", type = Accessor.Type.SETTER)
    public void setPartialOrderEntryCancelAllowed(boolean value)
    {
        getPersistenceContext().setPropertyValue("partialOrderEntryCancelAllowed", toObject(value));
    }


    @Accessor(qualifier = "queuedOrderWaitingTime", type = Accessor.Type.SETTER)
    public void setQueuedOrderWaitingTime(int value)
    {
        getPersistenceContext().setPropertyValue("queuedOrderWaitingTime", toObject(value));
    }
}
