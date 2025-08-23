package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.order.daos.DeliveryModeDao;
import de.hybris.platform.warehousing.util.builder.DeliveryModeModelBuilder;
import de.hybris.platform.warehousing.util.builder.PickUpDeliveryModeModelBuilder;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Required;

public class DeliveryModes extends AbstractItems<DeliveryModeModel>
{
    public static final String CODE_PICKUP = "pickup";
    public static final String CODE_REGULAR = "regular";
    public static final String CODE_STANDARD_SHIPMENT = "standard-shipment";
    public static final String DELIVERY_MODE_SHIPING = "free-standard-shipping";
    private DeliveryModeDao deliveryModeDao;


    public DeliveryModeModel Pickup()
    {
        return (DeliveryModeModel)getFromCollectionOrSaveAndReturn(() -> getDeliveryModeDao().findDeliveryModesByCode("pickup"), () -> PickUpDeliveryModeModelBuilder.aModel().withCode("pickup").withActive(Boolean.TRUE).withName("Pickup", Locale.ENGLISH).build());
    }


    public DeliveryModeModel Regular()
    {
        return (DeliveryModeModel)getFromCollectionOrSaveAndReturn(() -> getDeliveryModeDao().findDeliveryModesByCode("regular"), () -> DeliveryModeModelBuilder.aModel().withCode("regular").withActive(Boolean.TRUE).withName("Regular Delivery", Locale.ENGLISH).build());
    }


    public DeliveryModeModel standardShipment()
    {
        return (DeliveryModeModel)getFromCollectionOrSaveAndReturn(() -> getDeliveryModeDao().findDeliveryModesByCode("standard-shipment"), () -> DeliveryModeModelBuilder.aModel().withCode("standard-shipment").withName("free-standard-shipping", Locale.ENGLISH).withActive(Boolean.TRUE).build());
    }


    public DeliveryModeDao getDeliveryModeDao()
    {
        return this.deliveryModeDao;
    }


    @Required
    public void setDeliveryModeDao(DeliveryModeDao deliveryModeDao)
    {
        this.deliveryModeDao = deliveryModeDao;
    }
}
