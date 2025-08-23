package de.hybris.platform.storelocator.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedOpeningDay extends GenericItem
{
    public static final String OPENINGTIME = "openingTime";
    public static final String CLOSINGTIME = "closingTime";
    public static final String OPENINGSCHEDULE = "openingSchedule";
    protected static final BidirectionalOneToManyHandler<GeneratedOpeningDay> OPENINGSCHEDULEHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.OPENINGDAY, false, "openingSchedule", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("openingTime", Item.AttributeMode.INITIAL);
        tmp.put("closingTime", Item.AttributeMode.INITIAL);
        tmp.put("openingSchedule", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Date getClosingTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "closingTime");
    }


    public Date getClosingTime()
    {
        return getClosingTime(getSession().getSessionContext());
    }


    public void setClosingTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "closingTime", value);
    }


    public void setClosingTime(Date value)
    {
        setClosingTime(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        OPENINGSCHEDULEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public OpeningSchedule getOpeningSchedule(SessionContext ctx)
    {
        return (OpeningSchedule)getProperty(ctx, "openingSchedule");
    }


    public OpeningSchedule getOpeningSchedule()
    {
        return getOpeningSchedule(getSession().getSessionContext());
    }


    public void setOpeningSchedule(SessionContext ctx, OpeningSchedule value)
    {
        OPENINGSCHEDULEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setOpeningSchedule(OpeningSchedule value)
    {
        setOpeningSchedule(getSession().getSessionContext(), value);
    }


    public Date getOpeningTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "openingTime");
    }


    public Date getOpeningTime()
    {
        return getOpeningTime(getSession().getSessionContext());
    }


    public void setOpeningTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "openingTime", value);
    }


    public void setOpeningTime(Date value)
    {
        setOpeningTime(getSession().getSessionContext(), value);
    }
}
