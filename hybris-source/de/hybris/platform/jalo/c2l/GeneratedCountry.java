package de.hybris.platform.jalo.c2l;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCountry extends C2LItem
{
    public static final String REGIONS = "regions";
    protected static final OneToManyHandler<Region> REGIONSHANDLER = new OneToManyHandler(GeneratedCoreConstants.TC.REGION, true, "country", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(C2LItem.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<Region> getRegions(SessionContext ctx)
    {
        return REGIONSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<Region> getRegions()
    {
        return getRegions(getSession().getSessionContext());
    }


    public void setRegions(SessionContext ctx, Collection<Region> value)
    {
        REGIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setRegions(Collection<Region> value)
    {
        setRegions(getSession().getSessionContext(), value);
    }


    public void addToRegions(SessionContext ctx, Region value)
    {
        REGIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToRegions(Region value)
    {
        addToRegions(getSession().getSessionContext(), value);
    }


    public void removeFromRegions(SessionContext ctx, Region value)
    {
        REGIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromRegions(Region value)
    {
        removeFromRegions(getSession().getSessionContext(), value);
    }
}
