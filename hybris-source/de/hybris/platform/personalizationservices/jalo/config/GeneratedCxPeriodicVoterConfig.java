package de.hybris.platform.personalizationservices.jalo.config;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.personalizationservices.constants.GeneratedPersonalizationservicesConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCxPeriodicVoterConfig extends CxAbstractCalcConfig
{
    public static final String USERMINTIME = "userMinTime";
    public static final String USERMINREQUESTNUMBER = "userMinRequestNumber";
    public static final String CXCONFIG = "cxConfig";
    protected static final BidirectionalOneToManyHandler<GeneratedCxPeriodicVoterConfig> CXCONFIGHANDLER = new BidirectionalOneToManyHandler(GeneratedPersonalizationservicesConstants.TC.CXPERIODICVOTERCONFIG, false, "cxConfig", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CxAbstractCalcConfig.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("userMinTime", Item.AttributeMode.INITIAL);
        tmp.put("userMinRequestNumber", Item.AttributeMode.INITIAL);
        tmp.put("cxConfig", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        CXCONFIGHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public CxConfig getCxConfig(SessionContext ctx)
    {
        return (CxConfig)getProperty(ctx, "cxConfig");
    }


    public CxConfig getCxConfig()
    {
        return getCxConfig(getSession().getSessionContext());
    }


    public void setCxConfig(SessionContext ctx, CxConfig value)
    {
        CXCONFIGHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setCxConfig(CxConfig value)
    {
        setCxConfig(getSession().getSessionContext(), value);
    }


    public Integer getUserMinRequestNumber(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "userMinRequestNumber");
    }


    public Integer getUserMinRequestNumber()
    {
        return getUserMinRequestNumber(getSession().getSessionContext());
    }


    public int getUserMinRequestNumberAsPrimitive(SessionContext ctx)
    {
        Integer value = getUserMinRequestNumber(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getUserMinRequestNumberAsPrimitive()
    {
        return getUserMinRequestNumberAsPrimitive(getSession().getSessionContext());
    }


    public void setUserMinRequestNumber(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "userMinRequestNumber", value);
    }


    public void setUserMinRequestNumber(Integer value)
    {
        setUserMinRequestNumber(getSession().getSessionContext(), value);
    }


    public void setUserMinRequestNumber(SessionContext ctx, int value)
    {
        setUserMinRequestNumber(ctx, Integer.valueOf(value));
    }


    public void setUserMinRequestNumber(int value)
    {
        setUserMinRequestNumber(getSession().getSessionContext(), value);
    }


    public Long getUserMinTime(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "userMinTime");
    }


    public Long getUserMinTime()
    {
        return getUserMinTime(getSession().getSessionContext());
    }


    public long getUserMinTimeAsPrimitive(SessionContext ctx)
    {
        Long value = getUserMinTime(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getUserMinTimeAsPrimitive()
    {
        return getUserMinTimeAsPrimitive(getSession().getSessionContext());
    }


    public void setUserMinTime(SessionContext ctx, Long value)
    {
        setProperty(ctx, "userMinTime", value);
    }


    public void setUserMinTime(Long value)
    {
        setUserMinTime(getSession().getSessionContext(), value);
    }


    public void setUserMinTime(SessionContext ctx, long value)
    {
        setUserMinTime(ctx, Long.valueOf(value));
    }


    public void setUserMinTime(long value)
    {
        setUserMinTime(getSession().getSessionContext(), value);
    }
}
