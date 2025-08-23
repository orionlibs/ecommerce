package de.hybris.platform.deliveryzone.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

public class ZoneDeliveryModeModel extends DeliveryModeModel
{
    public static final String _TYPECODE = "ZoneDeliveryMode";
    public static final String PROPERTYNAME = "propertyName";
    public static final String NET = "net";
    public static final String VALUES = "values";


    public ZoneDeliveryModeModel()
    {
    }


    public ZoneDeliveryModeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ZoneDeliveryModeModel(String _code, Boolean _net)
    {
        setCode(_code);
        setNet(_net);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ZoneDeliveryModeModel(String _code, Boolean _net, ItemModel _owner)
    {
        setCode(_code);
        setNet(_net);
        setOwner(_owner);
    }


    @Accessor(qualifier = "net", type = Accessor.Type.GETTER)
    public Boolean getNet()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("net");
    }


    @Accessor(qualifier = "propertyName", type = Accessor.Type.GETTER)
    public String getPropertyName()
    {
        return (String)getPersistenceContext().getPropertyValue("propertyName");
    }


    @Accessor(qualifier = "values", type = Accessor.Type.GETTER)
    public Set<ZoneDeliveryModeValueModel> getValues()
    {
        return (Set<ZoneDeliveryModeValueModel>)getPersistenceContext().getPropertyValue("values");
    }


    @Accessor(qualifier = "net", type = Accessor.Type.SETTER)
    public void setNet(Boolean value)
    {
        getPersistenceContext().setPropertyValue("net", value);
    }


    @Accessor(qualifier = "propertyName", type = Accessor.Type.SETTER)
    public void setPropertyName(String value)
    {
        getPersistenceContext().setPropertyValue("propertyName", value);
    }


    @Accessor(qualifier = "values", type = Accessor.Type.SETTER)
    public void setValues(Set<ZoneDeliveryModeValueModel> value)
    {
        getPersistenceContext().setPropertyValue("values", value);
    }
}
