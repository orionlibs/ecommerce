package de.hybris.platform.storelocator.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedWeekdayOpeningDay extends OpeningDay
{
    public static final String DAYOFWEEK = "dayOfWeek";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(OpeningDay.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("dayOfWeek", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public EnumerationValue getDayOfWeek(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "dayOfWeek");
    }


    public EnumerationValue getDayOfWeek()
    {
        return getDayOfWeek(getSession().getSessionContext());
    }


    public void setDayOfWeek(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "dayOfWeek", value);
    }


    public void setDayOfWeek(EnumerationValue value)
    {
        setDayOfWeek(getSession().getSessionContext(), value);
    }
}
