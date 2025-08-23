package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.commerceservices.delivery.dao.PickupDeliveryModeDao;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.order.daos.OrderDao;
import org.springframework.beans.factory.annotation.Required;

public class PickUpDeliveryModes extends AbstractItems<DeliveryModeModel>
{
    public static final String CODE_PICKUP = "pickup";
    public static final String DELIVERY_MODE = "pickup";
    public static final String CODE_REGULAR = "regular";
    public static final String CODE_STANDARD_SHIPMENT = "standard-shipment";
    public static final String DELIVERY_MODE_SHIPING = "free-standard-shipping";
    private PickupDeliveryModeDao pickUpDeliveryModeDao;
    private OrderDao orderDao;


    public PickupDeliveryModeDao getPickUpDeliveryModeDao()
    {
        return this.pickUpDeliveryModeDao;
    }


    @Required
    public void setDeliveryModeDao(PickupDeliveryModeDao pickUpDeliveryModeDao)
    {
        this.pickUpDeliveryModeDao = pickUpDeliveryModeDao;
    }


    public OrderDao getOrderDao()
    {
        return this.orderDao;
    }


    @Required
    public void setOrderDao(OrderDao orderDao)
    {
        this.orderDao = orderDao;
    }
}
