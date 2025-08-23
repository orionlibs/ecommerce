package de.hybris.platform.webservicescommons.jalo;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SLDSafe
public class OAuthRefreshToken extends GenericItem
{
    public static final String TOKENID = "tokenId";
    public static final String TOKEN = "token";
    public static final String AUTHENTICATION = "authentication";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("tokenId", Item.AttributeMode.INITIAL);
        tmp.put("token", Item.AttributeMode.INITIAL);
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


    public Object getToken(SessionContext ctx)
    {
        return getProperty(ctx, "token".intern());
    }


    public Object getToken()
    {
        return getToken(getSession().getSessionContext());
    }


    public void setToken(SessionContext ctx, Object value)
    {
        setProperty(ctx, "token".intern(), value);
    }


    public void setToken(Object value)
    {
        setToken(getSession().getSessionContext(), value);
    }


    public String getTokenId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "tokenId".intern());
    }


    public String getTokenId()
    {
        return getTokenId(getSession().getSessionContext());
    }


    public void setTokenId(SessionContext ctx, String value)
    {
        setProperty(ctx, "tokenId".intern(), value);
    }


    public void setTokenId(String value)
    {
        setTokenId(getSession().getSessionContext(), value);
    }
}
