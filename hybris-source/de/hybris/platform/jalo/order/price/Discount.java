package de.hybris.platform.jalo.order.price;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.localization.Localization;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class Discount extends GeneratedDiscount
{
    public static final String EXTERNAL_KEY = "Discount.externalKey";
    public static final String GLOBAL = "global";
    public static final String VALUE_START = "Discount.valueStart";
    public static final String VALUE_END = "Discount.valueEnd";


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(allAttributes.get("code") == null)
        {
            throw new JaloInvalidParameterException(Localization.getLocalizedString("exception.discount.createitem.jaloinvalidparameterexception1", new Object[] {"code"}), 0);
        }
        allAttributes.setAttributeMode("code", Item.AttributeMode.INITIAL);
        String code = (String)allAttributes.get("code");
        if(OrderManager.getInstance().getDiscountByCode(code) != null)
        {
            throw new ConsistencyCheckException(Localization.getLocalizedString("exception.discount.createitem.duplicatecode", new Object[] {code}), 0);
        }
        return super.createItem(ctx, type, allAttributes);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Map getAllNames(SessionContext ctx)
    {
        return getAllName(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setAllNames(SessionContext ctx, Map names)
    {
        setAllName(ctx, names);
    }


    @ForceJALO(reason = "consistency check")
    public void setCode(SessionContext ctx, String code) throws ConsistencyCheckException
    {
        if(OrderManager.getInstance().getDiscountByCode(code) != null)
        {
            throw new ConsistencyCheckException(Localization.getLocalizedString("exception.discount.createitem.duplicatecode", new Object[] {code}), 0);
        }
        super.setCode(ctx, code);
    }


    @ForceJALO(reason = "something else")
    public Double getValue(SessionContext ctx)
    {
        Double result = super.getValue(ctx);
        return (result != null) ? result : Double.valueOf(0.0D);
    }


    @ForceJALO(reason = "something else")
    public Integer getPriority(SessionContext ctx)
    {
        Integer result = super.getPriority(ctx);
        return (result != null) ? result : Integer.valueOf(0);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addOrder(AbstractOrder order)
    {
        addToOrders(order);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeOrder(AbstractOrder order)
    {
        removeFromOrders(order);
    }


    public DiscountValue getDiscountValue(AbstractOrder order)
    {
        Currency currency = getCurrency();
        return new DiscountValue(getCode(), getValue().doubleValue(), isAbsolute().booleanValue(), 0.0D,
                        (currency != null) ? currency.getIsoCode() : null);
    }


    public void notifyOrderCalculated(AbstractOrder order)
    {
    }


    public void notifyOrderRemoval(AbstractOrder order)
    {
    }


    @ForceJALO(reason = "abstract method implementation")
    public Boolean isAbsolute(SessionContext ctx)
    {
        return Boolean.valueOf((getCurrency() != null));
    }


    @ForceJALO(reason = "abstract method implementation")
    public String getDiscountString(SessionContext ctx)
    {
        Locale loc = ctx.getLocale();
        if(isAbsolute().booleanValue())
        {
            Currency curr = getCurrency();
            return NumberFormat.getNumberInstance(loc).format(getValue()) + " " + NumberFormat.getNumberInstance(loc).format(getValue());
        }
        return NumberFormat.getInstance(loc).format(getValue()) + " %";
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected String getDiscountstring()
    {
        return getDiscountString();
    }
}
