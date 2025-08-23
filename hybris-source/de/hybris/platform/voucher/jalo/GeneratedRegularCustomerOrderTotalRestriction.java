package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedRegularCustomerOrderTotalRestriction extends Restriction
{
    public static final String ALLORDERSTOTAL = "allOrdersTotal";
    public static final String CURRENCY = "currency";
    public static final String NET = "net";
    public static final String VALUEOFGOODSONLY = "valueofgoodsonly";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Restriction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("allOrdersTotal", Item.AttributeMode.INITIAL);
        tmp.put("currency", Item.AttributeMode.INITIAL);
        tmp.put("net", Item.AttributeMode.INITIAL);
        tmp.put("valueofgoodsonly", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Double getAllOrdersTotal(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "allOrdersTotal");
    }


    public Double getAllOrdersTotal()
    {
        return getAllOrdersTotal(getSession().getSessionContext());
    }


    public double getAllOrdersTotalAsPrimitive(SessionContext ctx)
    {
        Double value = getAllOrdersTotal(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getAllOrdersTotalAsPrimitive()
    {
        return getAllOrdersTotalAsPrimitive(getSession().getSessionContext());
    }


    public void setAllOrdersTotal(SessionContext ctx, Double value)
    {
        setProperty(ctx, "allOrdersTotal", value);
    }


    public void setAllOrdersTotal(Double value)
    {
        setAllOrdersTotal(getSession().getSessionContext(), value);
    }


    public void setAllOrdersTotal(SessionContext ctx, double value)
    {
        setAllOrdersTotal(ctx, Double.valueOf(value));
    }


    public void setAllOrdersTotal(double value)
    {
        setAllOrdersTotal(getSession().getSessionContext(), value);
    }


    public Currency getCurrency(SessionContext ctx)
    {
        return (Currency)getProperty(ctx, "currency");
    }


    public Currency getCurrency()
    {
        return getCurrency(getSession().getSessionContext());
    }


    public void setCurrency(SessionContext ctx, Currency value)
    {
        setProperty(ctx, "currency", value);
    }


    public void setCurrency(Currency value)
    {
        setCurrency(getSession().getSessionContext(), value);
    }


    public Boolean isNet(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "net");
    }


    public Boolean isNet()
    {
        return isNet(getSession().getSessionContext());
    }


    public boolean isNetAsPrimitive(SessionContext ctx)
    {
        Boolean value = isNet(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isNetAsPrimitive()
    {
        return isNetAsPrimitive(getSession().getSessionContext());
    }


    public void setNet(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "net", value);
    }


    public void setNet(Boolean value)
    {
        setNet(getSession().getSessionContext(), value);
    }


    public void setNet(SessionContext ctx, boolean value)
    {
        setNet(ctx, Boolean.valueOf(value));
    }


    public void setNet(boolean value)
    {
        setNet(getSession().getSessionContext(), value);
    }


    public Boolean isValueofgoodsonly(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "valueofgoodsonly");
    }


    public Boolean isValueofgoodsonly()
    {
        return isValueofgoodsonly(getSession().getSessionContext());
    }


    public boolean isValueofgoodsonlyAsPrimitive(SessionContext ctx)
    {
        Boolean value = isValueofgoodsonly(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isValueofgoodsonlyAsPrimitive()
    {
        return isValueofgoodsonlyAsPrimitive(getSession().getSessionContext());
    }


    public void setValueofgoodsonly(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "valueofgoodsonly", value);
    }


    public void setValueofgoodsonly(Boolean value)
    {
        setValueofgoodsonly(getSession().getSessionContext(), value);
    }


    public void setValueofgoodsonly(SessionContext ctx, boolean value)
    {
        setValueofgoodsonly(ctx, Boolean.valueOf(value));
    }


    public void setValueofgoodsonly(boolean value)
    {
        setValueofgoodsonly(getSession().getSessionContext(), value);
    }
}
