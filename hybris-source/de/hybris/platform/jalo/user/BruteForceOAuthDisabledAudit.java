package de.hybris.platform.jalo.user;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SLDSafe
public class BruteForceOAuthDisabledAudit extends AbstractUserAudit
{
    public static final String FAILEDOAUTHAUTHORIZATIONS = "failedOAuthAuthorizations";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractUserAudit.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("failedOAuthAuthorizations", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getFailedOAuthAuthorizations(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "failedOAuthAuthorizations".intern());
    }


    public Integer getFailedOAuthAuthorizations()
    {
        return getFailedOAuthAuthorizations(getSession().getSessionContext());
    }


    public int getFailedOAuthAuthorizationsAsPrimitive(SessionContext ctx)
    {
        Integer value = getFailedOAuthAuthorizations(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getFailedOAuthAuthorizationsAsPrimitive()
    {
        return getFailedOAuthAuthorizationsAsPrimitive(getSession().getSessionContext());
    }


    protected void setFailedOAuthAuthorizations(SessionContext ctx, Integer value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute '" + "failedOAuthAuthorizations".intern() + "' is not changeable", 0);
        }
        setProperty(ctx, "failedOAuthAuthorizations".intern(), value);
    }


    protected void setFailedOAuthAuthorizations(Integer value)
    {
        setFailedOAuthAuthorizations(getSession().getSessionContext(), value);
    }


    protected void setFailedOAuthAuthorizations(SessionContext ctx, int value)
    {
        setFailedOAuthAuthorizations(ctx, Integer.valueOf(value));
    }


    protected void setFailedOAuthAuthorizations(int value)
    {
        setFailedOAuthAuthorizations(getSession().getSessionContext(), value);
    }
}
