package de.hybris.platform.jalo.user;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedBruteForceLoginDisabledAudit extends AbstractUserAudit
{
    public static final String FAILEDLOGINS = "failedLogins";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractUserAudit.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("failedLogins", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getFailedLogins(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "failedLogins");
    }


    public Integer getFailedLogins()
    {
        return getFailedLogins(getSession().getSessionContext());
    }


    public int getFailedLoginsAsPrimitive(SessionContext ctx)
    {
        Integer value = getFailedLogins(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getFailedLoginsAsPrimitive()
    {
        return getFailedLoginsAsPrimitive(getSession().getSessionContext());
    }


    protected void setFailedLogins(SessionContext ctx, Integer value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'failedLogins' is not changeable", 0);
        }
        setProperty(ctx, "failedLogins", value);
    }


    protected void setFailedLogins(Integer value)
    {
        setFailedLogins(getSession().getSessionContext(), value);
    }


    protected void setFailedLogins(SessionContext ctx, int value)
    {
        setFailedLogins(ctx, Integer.valueOf(value));
    }


    protected void setFailedLogins(int value)
    {
        setFailedLogins(getSession().getSessionContext(), value);
    }
}
