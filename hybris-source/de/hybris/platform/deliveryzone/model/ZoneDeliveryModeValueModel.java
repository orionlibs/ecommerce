package de.hybris.platform.deliveryzone.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ZoneDeliveryModeValueModel extends ItemModel
{
    public static final String _TYPECODE = "ZoneDeliveryModeValue";
    public static final String _MODEVALUESRELATION = "ModeValuesRelation";
    public static final String CURRENCY = "currency";
    public static final String MINIMUM = "minimum";
    public static final String VALUE = "value";
    public static final String ZONE = "zone";
    public static final String DELIVERYMODE = "deliveryMode";


    public ZoneDeliveryModeValueModel()
    {
    }


    public ZoneDeliveryModeValueModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ZoneDeliveryModeValueModel(CurrencyModel _currency, ZoneDeliveryModeModel _deliveryMode, Double _minimum, Double _value, ZoneModel _zone)
    {
        setCurrency(_currency);
        setDeliveryMode(_deliveryMode);
        setMinimum(_minimum);
        setValue(_value);
        setZone(_zone);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ZoneDeliveryModeValueModel(CurrencyModel _currency, ZoneDeliveryModeModel _deliveryMode, Double _minimum, ItemModel _owner, Double _value, ZoneModel _zone)
    {
        setCurrency(_currency);
        setDeliveryMode(_deliveryMode);
        setMinimum(_minimum);
        setOwner(_owner);
        setValue(_value);
        setZone(_zone);
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
    }


    @Accessor(qualifier = "deliveryMode", type = Accessor.Type.GETTER)
    public ZoneDeliveryModeModel getDeliveryMode()
    {
        return (ZoneDeliveryModeModel)getPersistenceContext().getPropertyValue("deliveryMode");
    }


    @Accessor(qualifier = "minimum", type = Accessor.Type.GETTER)
    public Double getMinimum()
    {
        return (Double)getPersistenceContext().getPropertyValue("minimum");
    }


    @Accessor(qualifier = "value", type = Accessor.Type.GETTER)
    public Double getValue()
    {
        return (Double)getPersistenceContext().getPropertyValue("value");
    }


    @Accessor(qualifier = "zone", type = Accessor.Type.GETTER)
    public ZoneModel getZone()
    {
        return (ZoneModel)getPersistenceContext().getPropertyValue("zone");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
    public void setCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("currency", value);
    }


    @Accessor(qualifier = "deliveryMode", type = Accessor.Type.SETTER)
    public void setDeliveryMode(ZoneDeliveryModeModel value)
    {
        getPersistenceContext().setPropertyValue("deliveryMode", value);
    }


    @Accessor(qualifier = "minimum", type = Accessor.Type.SETTER)
    public void setMinimum(Double value)
    {
        getPersistenceContext().setPropertyValue("minimum", value);
    }


    @Accessor(qualifier = "value", type = Accessor.Type.SETTER)
    public void setValue(Double value)
    {
        getPersistenceContext().setPropertyValue("value", value);
    }


    @Accessor(qualifier = "zone", type = Accessor.Type.SETTER)
    public void setZone(ZoneModel value)
    {
        getPersistenceContext().setPropertyValue("zone", value);
    }
}
