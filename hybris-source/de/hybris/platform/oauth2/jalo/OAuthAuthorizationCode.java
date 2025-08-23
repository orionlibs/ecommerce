package de.hybris.platform.oauth2.jalo;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SLDSafe
public class OAuthAuthorizationCode extends GenericItem
{
    public static final String CODE = "code";
    public static final String AUTHENTICATION = "authentication";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("authentication", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Object getAuthentication(SessionContext ctx)
    {
        return getProperty(ctx, "authentication".intern());
    }


    public Object getAuthentication()
    {
        return getAuthentication(getSession().getSessionContext());
    }


    public void setAuthentication(SessionContext ctx, Object value)
    {
        setProperty(ctx, "authentication".intern(), value);
    }


    public void setAuthentication(Object value)
    {
        setAuthentication(getSession().getSessionContext(), value);
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code".intern());
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code".intern(), value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }
}
