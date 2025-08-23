package de.hybris.platform.webservicescommons.jalo;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SLDSafe
public class OAuthClientDetails extends GenericItem
{
    public static final String CLIENTID = "clientId";
    public static final String RESOURCEIDS = "resourceIds";
    public static final String CLIENTSECRET = "clientSecret";
    public static final String SCOPE = "scope";
    public static final String AUTHORIZEDGRANTTYPES = "authorizedGrantTypes";
    public static final String REGISTEREDREDIRECTURI = "registeredRedirectUri";
    public static final String AUTHORITIES = "authorities";
    public static final String ACCESSTOKENVALIDITYSECONDS = "accessTokenValiditySeconds";
    public static final String REFRESHTOKENVALIDITYSECONDS = "refreshTokenValiditySeconds";
    public static final String AUTOAPPROVE = "autoApprove";
    public static final String DISABLED = "disabled";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("clientId", Item.AttributeMode.INITIAL);
        tmp.put("resourceIds", Item.AttributeMode.INITIAL);
        tmp.put("clientSecret", Item.AttributeMode.INITIAL);
        tmp.put("scope", Item.AttributeMode.INITIAL);
        tmp.put("authorizedGrantTypes", Item.AttributeMode.INITIAL);
        tmp.put("registeredRedirectUri", Item.AttributeMode.INITIAL);
        tmp.put("authorities", Item.AttributeMode.INITIAL);
        tmp.put("accessTokenValiditySeconds", Item.AttributeMode.INITIAL);
        tmp.put("refreshTokenValiditySeconds", Item.AttributeMode.INITIAL);
        tmp.put("autoApprove", Item.AttributeMode.INITIAL);
        tmp.put("disabled", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getAccessTokenValiditySeconds(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "accessTokenValiditySeconds".intern());
    }


    public Integer getAccessTokenValiditySeconds()
    {
        return getAccessTokenValiditySeconds(getSession().getSessionContext());
    }


    public int getAccessTokenValiditySecondsAsPrimitive(SessionContext ctx)
    {
        Integer value = getAccessTokenValiditySeconds(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getAccessTokenValiditySecondsAsPrimitive()
    {
        return getAccessTokenValiditySecondsAsPrimitive(getSession().getSessionContext());
    }


    public void setAccessTokenValiditySeconds(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "accessTokenValiditySeconds".intern(), value);
    }


    public void setAccessTokenValiditySeconds(Integer value)
    {
        setAccessTokenValiditySeconds(getSession().getSessionContext(), value);
    }


    public void setAccessTokenValiditySeconds(SessionContext ctx, int value)
    {
        setAccessTokenValiditySeconds(ctx, Integer.valueOf(value));
    }


    public void setAccessTokenValiditySeconds(int value)
    {
        setAccessTokenValiditySeconds(getSession().getSessionContext(), value);
    }


    public Set<String> getAuthorities(SessionContext ctx)
    {
        Set<String> coll = (Set<String>)getProperty(ctx, "authorities".intern());
        return (coll != null) ? coll : Collections.EMPTY_SET;
    }


    public Set<String> getAuthorities()
    {
        return getAuthorities(getSession().getSessionContext());
    }


    public void setAuthorities(SessionContext ctx, Set<String> value)
    {
        setProperty(ctx, "authorities".intern(), (value == null || !value.isEmpty()) ? value : null);
    }


    public void setAuthorities(Set<String> value)
    {
        setAuthorities(getSession().getSessionContext(), value);
    }


    public Set<String> getAuthorizedGrantTypes(SessionContext ctx)
    {
        Set<String> coll = (Set<String>)getProperty(ctx, "authorizedGrantTypes".intern());
        return (coll != null) ? coll : Collections.EMPTY_SET;
    }


    public Set<String> getAuthorizedGrantTypes()
    {
        return getAuthorizedGrantTypes(getSession().getSessionContext());
    }


    public void setAuthorizedGrantTypes(SessionContext ctx, Set<String> value)
    {
        setProperty(ctx, "authorizedGrantTypes".intern(), (value == null || !value.isEmpty()) ? value : null);
    }


    public void setAuthorizedGrantTypes(Set<String> value)
    {
        setAuthorizedGrantTypes(getSession().getSessionContext(), value);
    }


    public Set<String> getAutoApprove(SessionContext ctx)
    {
        Set<String> coll = (Set<String>)getProperty(ctx, "autoApprove".intern());
        return (coll != null) ? coll : Collections.EMPTY_SET;
    }


    public Set<String> getAutoApprove()
    {
        return getAutoApprove(getSession().getSessionContext());
    }


    public void setAutoApprove(SessionContext ctx, Set<String> value)
    {
        setProperty(ctx, "autoApprove".intern(), (value == null || !value.isEmpty()) ? value : null);
    }


    public void setAutoApprove(Set<String> value)
    {
        setAutoApprove(getSession().getSessionContext(), value);
    }


    public String getClientId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "clientId".intern());
    }


    public String getClientId()
    {
        return getClientId(getSession().getSessionContext());
    }


    protected void setClientId(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute '" + "clientId".intern() + "' is not changeable", 0);
        }
        setProperty(ctx, "clientId".intern(), value);
    }


    protected void setClientId(String value)
    {
        setClientId(getSession().getSessionContext(), value);
    }


    public String getClientSecret(SessionContext ctx)
    {
        return (String)getProperty(ctx, "clientSecret".intern());
    }


    public String getClientSecret()
    {
        return getClientSecret(getSession().getSessionContext());
    }


    public void setClientSecret(SessionContext ctx, String value)
    {
        setProperty(ctx, "clientSecret".intern(), value);
    }


    public void setClientSecret(String value)
    {
        setClientSecret(getSession().getSessionContext(), value);
    }


    public Boolean isDisabled(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "disabled".intern());
    }


    public Boolean isDisabled()
    {
        return isDisabled(getSession().getSessionContext());
    }


    public boolean isDisabledAsPrimitive(SessionContext ctx)
    {
        Boolean value = isDisabled(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isDisabledAsPrimitive()
    {
        return isDisabledAsPrimitive(getSession().getSessionContext());
    }


    public void setDisabled(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "disabled".intern(), value);
    }


    public void setDisabled(Boolean value)
    {
        setDisabled(getSession().getSessionContext(), value);
    }


    public void setDisabled(SessionContext ctx, boolean value)
    {
        setDisabled(ctx, Boolean.valueOf(value));
    }


    public void setDisabled(boolean value)
    {
        setDisabled(getSession().getSessionContext(), value);
    }


    public Integer getRefreshTokenValiditySeconds(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "refreshTokenValiditySeconds".intern());
    }


    public Integer getRefreshTokenValiditySeconds()
    {
        return getRefreshTokenValiditySeconds(getSession().getSessionContext());
    }


    public int getRefreshTokenValiditySecondsAsPrimitive(SessionContext ctx)
    {
        Integer value = getRefreshTokenValiditySeconds(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getRefreshTokenValiditySecondsAsPrimitive()
    {
        return getRefreshTokenValiditySecondsAsPrimitive(getSession().getSessionContext());
    }


    public void setRefreshTokenValiditySeconds(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "refreshTokenValiditySeconds".intern(), value);
    }


    public void setRefreshTokenValiditySeconds(Integer value)
    {
        setRefreshTokenValiditySeconds(getSession().getSessionContext(), value);
    }


    public void setRefreshTokenValiditySeconds(SessionContext ctx, int value)
    {
        setRefreshTokenValiditySeconds(ctx, Integer.valueOf(value));
    }


    public void setRefreshTokenValiditySeconds(int value)
    {
        setRefreshTokenValiditySeconds(getSession().getSessionContext(), value);
    }


    public Set<String> getRegisteredRedirectUri(SessionContext ctx)
    {
        Set<String> coll = (Set<String>)getProperty(ctx, "registeredRedirectUri".intern());
        return (coll != null) ? coll : Collections.EMPTY_SET;
    }


    public Set<String> getRegisteredRedirectUri()
    {
        return getRegisteredRedirectUri(getSession().getSessionContext());
    }


    public void setRegisteredRedirectUri(SessionContext ctx, Set<String> value)
    {
        setProperty(ctx, "registeredRedirectUri".intern(), (value == null || !value.isEmpty()) ? value : null);
    }


    public void setRegisteredRedirectUri(Set<String> value)
    {
        setRegisteredRedirectUri(getSession().getSessionContext(), value);
    }


    public Set<String> getResourceIds(SessionContext ctx)
    {
        Set<String> coll = (Set<String>)getProperty(ctx, "resourceIds".intern());
        return (coll != null) ? coll : Collections.EMPTY_SET;
    }


    public Set<String> getResourceIds()
    {
        return getResourceIds(getSession().getSessionContext());
    }


    public void setResourceIds(SessionContext ctx, Set<String> value)
    {
        setProperty(ctx, "resourceIds".intern(), (value == null || !value.isEmpty()) ? value : null);
    }


    public void setResourceIds(Set<String> value)
    {
        setResourceIds(getSession().getSessionContext(), value);
    }


    public Set<String> getScope(SessionContext ctx)
    {
        Set<String> coll = (Set<String>)getProperty(ctx, "scope".intern());
        return (coll != null) ? coll : Collections.EMPTY_SET;
    }


    public Set<String> getScope()
    {
        return getScope(getSession().getSessionContext());
    }


    public void setScope(SessionContext ctx, Set<String> value)
    {
        setProperty(ctx, "scope".intern(), (value == null || !value.isEmpty()) ? value : null);
    }


    public void setScope(Set<String> value)
    {
        setScope(getSession().getSessionContext(), value);
    }
}
