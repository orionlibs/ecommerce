package de.hybris.platform.commerceservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commerceservices.enums.PickupInStoreMode;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class PickUpDeliveryModeModel extends DeliveryModeModel
{
    public static final String _TYPECODE = "PickUpDeliveryMode";
    public static final String SUPPORTEDMODE = "supportedMode";


    public PickUpDeliveryModeModel()
    {
    }


    public PickUpDeliveryModeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PickUpDeliveryModeModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PickUpDeliveryModeModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "supportedMode", type = Accessor.Type.GETTER)
    public PickupInStoreMode getSupportedMode()
    {
        return (PickupInStoreMode)getPersistenceContext().getPropertyValue("supportedMode");
    }


    @Accessor(qualifier = "supportedMode", type = Accessor.Type.SETTER)
    public void setSupportedMode(PickupInStoreMode value)
    {
        getPersistenceContext().setPropertyValue("supportedMode", value);
    }
}
