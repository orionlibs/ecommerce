package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedDateRestriction extends Restriction
{
    public static final String STARTDATE = "startDate";
    public static final String ENDDATE = "endDate";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Restriction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("startDate", Item.AttributeMode.INITIAL);
        tmp.put("endDate", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Date getEndDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "endDate");
    }


    public Date getEndDate()
    {
        return getEndDate(getSession().getSessionContext());
    }


    public void setEndDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "endDate", value);
    }


    public void setEndDate(Date value)
    {
        setEndDate(getSession().getSessionContext(), value);
    }


    public Date getStartDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "startDate");
    }


    public Date getStartDate()
    {
        return getStartDate(getSession().getSessionContext());
    }


    public void setStartDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "startDate", value);
    }


    public void setStartDate(Date value)
    {
        setStartDate(getSession().getSessionContext(), value);
    }
}
