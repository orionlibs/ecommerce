package de.hybris.platform.core.model.order.price;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

public class DiscountModel extends ItemModel
{
    public static final String _TYPECODE = "Discount";
    public static final String _ORDERDISCOUNTRELATION = "OrderDiscountRelation";
    public static final String ABSOLUTE = "absolute";
    public static final String CODE = "code";
    public static final String CURRENCY = "currency";
    public static final String GLOBAL = "global";
    public static final String NAME = "name";
    public static final String PRIORITY = "priority";
    public static final String VALUE = "value";
    public static final String DISCOUNTSTRING = "discountString";
    public static final String ORDERS = "orders";


    public DiscountModel()
    {
    }


    public DiscountModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DiscountModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DiscountModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "absolute", type = Accessor.Type.GETTER)
    public Boolean getAbsolute()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("absolute");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
    }


    @Deprecated(since = "ages", forRemoval = true)
    public String getDiscountstring()
    {
        return getDiscountString();
    }


    @Accessor(qualifier = "discountString", type = Accessor.Type.GETTER)
    public String getDiscountString()
    {
        return (String)getPersistenceContext().getPropertyValue("discountString");
    }


    @Accessor(qualifier = "global", type = Accessor.Type.GETTER)
    public Boolean getGlobal()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("global");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "orders", type = Accessor.Type.GETTER)
    public Collection<AbstractOrderModel> getOrders()
    {
        return (Collection<AbstractOrderModel>)getPersistenceContext().getPropertyValue("orders");
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
    public Integer getPriority()
    {
        Integer value = (Integer)getPersistenceContext().getPropertyValue("priority");
        return (value != null) ? value : Integer.valueOf(0);
    }


    @Accessor(qualifier = "value", type = Accessor.Type.GETTER)
    public Double getValue()
    {
        Double value = (Double)getPersistenceContext().getPropertyValue("value");
        return (value != null) ? value : Double.valueOf(0.0D);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
    public void setCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("currency", value);
    }


    @Accessor(qualifier = "global", type = Accessor.Type.SETTER)
    public void setGlobal(Boolean value)
    {
        getPersistenceContext().setPropertyValue("global", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "orders", type = Accessor.Type.SETTER)
    public void setOrders(Collection<AbstractOrderModel> value)
    {
        getPersistenceContext().setPropertyValue("orders", value);
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
    public void setPriority(Integer value)
    {
        getPersistenceContext().setPropertyValue("priority", value);
    }


    @Accessor(qualifier = "value", type = Accessor.Type.SETTER)
    public void setValue(Double value)
    {
        getPersistenceContext().setPropertyValue("value", value);
    }
}
