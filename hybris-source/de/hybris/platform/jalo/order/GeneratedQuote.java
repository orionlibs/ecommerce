package de.hybris.platform.jalo.order;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedQuote extends AbstractOrder
{
    public static final String VERSION = "version";
    public static final String STATE = "state";
    protected static final OneToManyHandler<AbstractOrderEntry> ENTRIESHANDLER = new OneToManyHandler(GeneratedCoreConstants.TC.QUOTEENTRY, true, "order", "entryNumber", false, true, 2);
    protected static final BidirectionalOneToManyHandler<GeneratedQuote> USERHANDLER = new BidirectionalOneToManyHandler(GeneratedCoreConstants.TC.USER, false, "user", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractOrder.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("version", Item.AttributeMode.INITIAL);
        tmp.put("state", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        USERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public List<AbstractOrderEntry> getEntries(SessionContext ctx)
    {
        return (List<AbstractOrderEntry>)ENTRIESHANDLER.getValues(ctx, (Item)this);
    }


    public List<AbstractOrderEntry> getEntries()
    {
        return getEntries(getSession().getSessionContext());
    }


    public void setEntries(SessionContext ctx, List<AbstractOrderEntry> value)
    {
        ENTRIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setEntries(List<AbstractOrderEntry> value)
    {
        setEntries(getSession().getSessionContext(), value);
    }


    public void addToEntries(SessionContext ctx, QuoteEntry value)
    {
        ENTRIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToEntries(QuoteEntry value)
    {
        addToEntries(getSession().getSessionContext(), value);
    }


    public void removeFromEntries(SessionContext ctx, QuoteEntry value)
    {
        ENTRIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromEntries(QuoteEntry value)
    {
        removeFromEntries(getSession().getSessionContext(), value);
    }


    public EnumerationValue getState(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "state");
    }


    public EnumerationValue getState()
    {
        return getState(getSession().getSessionContext());
    }


    public void setState(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "state", value);
    }


    public void setState(EnumerationValue value)
    {
        setState(getSession().getSessionContext(), value);
    }


    public Integer getVersion(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "version");
    }


    public Integer getVersion()
    {
        return getVersion(getSession().getSessionContext());
    }


    public int getVersionAsPrimitive(SessionContext ctx)
    {
        Integer value = getVersion(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getVersionAsPrimitive()
    {
        return getVersionAsPrimitive(getSession().getSessionContext());
    }


    protected void setVersion(SessionContext ctx, Integer value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'version' is not changeable", 0);
        }
        setProperty(ctx, "version", value);
    }


    protected void setVersion(Integer value)
    {
        setVersion(getSession().getSessionContext(), value);
    }


    protected void setVersion(SessionContext ctx, int value)
    {
        setVersion(ctx, Integer.valueOf(value));
    }


    protected void setVersion(int value)
    {
        setVersion(getSession().getSessionContext(), value);
    }
}
