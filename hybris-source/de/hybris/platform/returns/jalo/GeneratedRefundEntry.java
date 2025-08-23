package de.hybris.platform.returns.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedRefundEntry extends ReturnEntry
{
    public static final String REASON = "reason";
    public static final String AMOUNT = "amount";
    public static final String CURRENCY = "currency";
    public static final String REFUNDEDDATE = "refundedDate";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(ReturnEntry.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("reason", Item.AttributeMode.INITIAL);
        tmp.put("amount", Item.AttributeMode.INITIAL);
        tmp.put("refundedDate", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public BigDecimal getAmount(SessionContext ctx)
    {
        return (BigDecimal)getProperty(ctx, "amount");
    }


    public BigDecimal getAmount()
    {
        return getAmount(getSession().getSessionContext());
    }


    public void setAmount(SessionContext ctx, BigDecimal value)
    {
        setProperty(ctx, "amount", value);
    }


    public void setAmount(BigDecimal value)
    {
        setAmount(getSession().getSessionContext(), value);
    }


    public Currency getCurrency()
    {
        return getCurrency(getSession().getSessionContext());
    }


    public EnumerationValue getReason(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "reason");
    }


    public EnumerationValue getReason()
    {
        return getReason(getSession().getSessionContext());
    }


    public void setReason(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "reason", value);
    }


    public void setReason(EnumerationValue value)
    {
        setReason(getSession().getSessionContext(), value);
    }


    public Date getRefundedDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "refundedDate");
    }


    public Date getRefundedDate()
    {
        return getRefundedDate(getSession().getSessionContext());
    }


    public void setRefundedDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "refundedDate", value);
    }


    public void setRefundedDate(Date value)
    {
        setRefundedDate(getSession().getSessionContext(), value);
    }


    public abstract Currency getCurrency(SessionContext paramSessionContext);
}
