package de.hybris.platform.sap.sapmodel.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SAPDeliveryModeModel extends ItemModel
{
    public static final String _TYPECODE = "SAPDeliveryMode";
    public static final String _SAPCONFIGDELIVERYRELATION = "SAPConfigDeliveryRelation";
    public static final String SAPCONFIGURATION = "sapConfiguration";
    public static final String DELIVERYMODE = "deliveryMode";
    public static final String DELIVERYVALUE = "deliveryValue";


    public SAPDeliveryModeModel()
    {
    }


    public SAPDeliveryModeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPDeliveryModeModel(DeliveryModeModel _deliveryMode, String _deliveryValue)
    {
        setDeliveryMode(_deliveryMode);
        setDeliveryValue(_deliveryValue);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPDeliveryModeModel(DeliveryModeModel _deliveryMode, String _deliveryValue, ItemModel _owner, SAPConfigurationModel _sapConfiguration)
    {
        setDeliveryMode(_deliveryMode);
        setDeliveryValue(_deliveryValue);
        setOwner(_owner);
        setSapConfiguration(_sapConfiguration);
    }


    @Accessor(qualifier = "deliveryMode", type = Accessor.Type.GETTER)
    public DeliveryModeModel getDeliveryMode()
    {
        return (DeliveryModeModel)getPersistenceContext().getPropertyValue("deliveryMode");
    }


    @Accessor(qualifier = "deliveryValue", type = Accessor.Type.GETTER)
    public String getDeliveryValue()
    {
        return (String)getPersistenceContext().getPropertyValue("deliveryValue");
    }


    @Accessor(qualifier = "sapConfiguration", type = Accessor.Type.GETTER)
    public SAPConfigurationModel getSapConfiguration()
    {
        return (SAPConfigurationModel)getPersistenceContext().getPropertyValue("sapConfiguration");
    }


    @Accessor(qualifier = "deliveryMode", type = Accessor.Type.SETTER)
    public void setDeliveryMode(DeliveryModeModel value)
    {
        getPersistenceContext().setPropertyValue("deliveryMode", value);
    }


    @Accessor(qualifier = "deliveryValue", type = Accessor.Type.SETTER)
    public void setDeliveryValue(String value)
    {
        getPersistenceContext().setPropertyValue("deliveryValue", value);
    }


    @Accessor(qualifier = "sapConfiguration", type = Accessor.Type.SETTER)
    public void setSapConfiguration(SAPConfigurationModel value)
    {
        getPersistenceContext().setPropertyValue("sapConfiguration", value);
    }
}
