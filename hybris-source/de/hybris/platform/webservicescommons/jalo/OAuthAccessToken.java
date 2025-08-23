package de.hybris.platform.webservicescommons.jalo;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.oauth2.constants.GeneratedOAuth2Constants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SLDSafe
public class OAuthAccessToken extends GenericItem
{
    public static final String TOKENID = "tokenId";
    public static final String TOKEN = "token";
    public static final String AUTHENTICATIONID = "authenticationId";
    public static final String CLIENT = "client";
    public static final String AUTHENTICATION = "authentication";
    public static final String REFRESHTOKEN = "refreshToken";
    public static final String USER = "user";
    protected static final BidirectionalOneToManyHandler<OAuthAccessToken> USERHANDLER = new BidirectionalOneToManyHandler(GeneratedOAuth2Constants.TC.OAUTHACCESSTOKEN, false, "user", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("tokenId", Item.AttributeMode.INITIAL);
        tmp.put("token", Item.AttributeMode.INITIAL);
        tmp.put("authenticationId", Item.AttributeMode.INITIAL);
        tmp.put("client", Item.AttributeMode.INITIAL);
        tmp.put("authentication", Item.AttributeMode.INITIAL);
        tmp.put("refreshToken", Item.AttributeMode.INITIAL);
        tmp.put("user", Item.AttributeMode.INITIAL);
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


    public String getAuthenticationId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "authenticationId".intern());
    }


    public String getAuthenticationId()
    {
        return getAuthenticationId(getSession().getSessionContext());
    }


    protected void setAuthenticationId(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute '" + "authenticationId".intern() + "' is not changeable", 0);
        }
        setProperty(ctx, "authenticationId".intern(), value);
    }


    protected void setAuthenticationId(String value)
    {
        setAuthenticationId(getSession().getSessionContext(), value);
    }


    public OAuthClientDetails getClient(SessionContext ctx)
    {
        return (OAuthClientDetails)getProperty(ctx, "client".intern());
    }


    public OAuthClientDetails getClient()
    {
        return getClient(getSession().getSessionContext());
    }


    protected void setClient(SessionContext ctx, OAuthClientDetails value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute '" + "client".intern() + "' is not changeable", 0);
        }
        setProperty(ctx, "client".intern(), value);
    }


    protected void setClient(OAuthClientDetails value)
    {
        setClient(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        USERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public OAuthRefreshToken getRefreshToken(SessionContext ctx)
    {
        return (OAuthRefreshToken)getProperty(ctx, "refreshToken".intern());
    }


    public OAuthRefreshToken getRefreshToken()
    {
        return getRefreshToken(getSession().getSessionContext());
    }


    protected void setRefreshToken(SessionContext ctx, OAuthRefreshToken value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute '" + "refreshToken".intern() + "' is not changeable", 0);
        }
        setProperty(ctx, "refreshToken".intern(), value);
    }


    protected void setRefreshToken(OAuthRefreshToken value)
    {
        setRefreshToken(getSession().getSessionContext(), value);
    }


    public Object getToken(SessionContext ctx)
    {
        return getProperty(ctx, "token".intern());
    }


    public Object getToken()
    {
        return getToken(getSession().getSessionContext());
    }


    protected void setToken(SessionContext ctx, Object value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute '" + "token".intern() + "' is not changeable", 0);
        }
        setProperty(ctx, "token".intern(), value);
    }


    protected void setToken(Object value)
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


    public User getUser(SessionContext ctx)
    {
        return (User)getProperty(ctx, "user".intern());
    }


    public User getUser()
    {
        return getUser(getSession().getSessionContext());
    }


    public void setUser(SessionContext ctx, User value)
    {
        USERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setUser(User value)
    {
        setUser(getSession().getSessionContext(), value);
    }
}
