package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedJobMedia extends Media
{
    public static final String LOCKED = "locked";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Media.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isLocked()
    {
        return isLocked(getSession().getSessionContext());
    }


    public boolean isLockedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isLocked(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isLockedAsPrimitive()
    {
        return isLockedAsPrimitive(getSession().getSessionContext());
    }


    public void setLocked(Boolean value)
    {
        setLocked(getSession().getSessionContext(), value);
    }


    public void setLocked(SessionContext ctx, boolean value)
    {
        setLocked(ctx, Boolean.valueOf(value));
    }


    public void setLocked(boolean value)
    {
        setLocked(getSession().getSessionContext(), value);
    }


    public abstract Boolean isLocked(SessionContext paramSessionContext);


    public abstract void setLocked(SessionContext paramSessionContext, Boolean paramBoolean);
}
