package de.hybris.platform.oauth2.jalo;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.BruteForceOAuthDisabledAudit;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.oauth2.constants.GeneratedOAuth2Constants;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.webservicescommons.jalo.OAuthAccessToken;
import de.hybris.platform.webservicescommons.jalo.OAuthClientDetails;
import de.hybris.platform.webservicescommons.jalo.OAuthRefreshToken;
import de.hybris.platform.webservicescommons.jalo.OpenIDClientDetails;
import de.hybris.platform.webservicescommons.jalo.OpenIDExternalScopes;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SLDSafe
public class OAuth2Manager extends Extension
{
    protected static final OneToManyHandler<OAuthAccessToken> USER2TOKENRELATIONTOKENSHANDLER = new OneToManyHandler(GeneratedOAuth2Constants.TC.OAUTHACCESSTOKEN, true, "user", null, false, true, 0);
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public BruteForceOAuthDisabledAudit createBruteForceOAuthDisabledAudit(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("BruteForceOAuthDisabledAudit");
            return (BruteForceOAuthDisabledAudit)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating BruteForceOAuthDisabledAudit : " + e.getMessage(), 0);
        }
    }


    public BruteForceOAuthDisabledAudit createBruteForceOAuthDisabledAudit(Map attributeValues)
    {
        return createBruteForceOAuthDisabledAudit(getSession().getSessionContext(), attributeValues);
    }


    public OAuthAccessToken createOAuthAccessToken(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("OAuthAccessToken");
            return (OAuthAccessToken)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating OAuthAccessToken : " + e.getMessage(), 0);
        }
    }


    public OAuthAccessToken createOAuthAccessToken(Map attributeValues)
    {
        return createOAuthAccessToken(getSession().getSessionContext(), attributeValues);
    }


    public OAuthAuthorizationCode createOAuthAuthorizationCode(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("OAuthAuthorizationCode");
            return (OAuthAuthorizationCode)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating OAuthAuthorizationCode : " + e.getMessage(), 0);
        }
    }


    public OAuthAuthorizationCode createOAuthAuthorizationCode(Map attributeValues)
    {
        return createOAuthAuthorizationCode(getSession().getSessionContext(), attributeValues);
    }


    public OAuthClientDetails createOAuthClientDetails(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("OAuthClientDetails");
            return (OAuthClientDetails)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating OAuthClientDetails : " + e.getMessage(), 0);
        }
    }


    public OAuthClientDetails createOAuthClientDetails(Map attributeValues)
    {
        return createOAuthClientDetails(getSession().getSessionContext(), attributeValues);
    }


    public OAuthRefreshToken createOAuthRefreshToken(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("OAuthRefreshToken");
            return (OAuthRefreshToken)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating OAuthRefreshToken : " + e.getMessage(), 0);
        }
    }


    public OAuthRefreshToken createOAuthRefreshToken(Map attributeValues)
    {
        return createOAuthRefreshToken(getSession().getSessionContext(), attributeValues);
    }


    public OpenIDClientDetails createOpenIDClientDetails(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("OpenIDClientDetails");
            return (OpenIDClientDetails)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating OpenIDClientDetails : " + e.getMessage(), 0);
        }
    }


    public OpenIDClientDetails createOpenIDClientDetails(Map attributeValues)
    {
        return createOpenIDClientDetails(getSession().getSessionContext(), attributeValues);
    }


    public OpenIDExternalScopes createOpenIDExternalScopes(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("OpenIDExternalScopes");
            return (OpenIDExternalScopes)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating OpenIDExternalScopes : " + e.getMessage(), 0);
        }
    }


    public OpenIDExternalScopes createOpenIDExternalScopes(Map attributeValues)
    {
        return createOpenIDExternalScopes(getSession().getSessionContext(), attributeValues);
    }


    public static final OAuth2Manager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (OAuth2Manager)em.getExtension("oauth2");
    }


    public String getName()
    {
        return "oauth2";
    }


    public Collection<OAuthAccessToken> getTokens(SessionContext ctx, User item)
    {
        return USER2TOKENRELATIONTOKENSHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<OAuthAccessToken> getTokens(User item)
    {
        return getTokens(getSession().getSessionContext(), item);
    }


    public void setTokens(SessionContext ctx, User item, Collection<OAuthAccessToken> value)
    {
        USER2TOKENRELATIONTOKENSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setTokens(User item, Collection<OAuthAccessToken> value)
    {
        setTokens(getSession().getSessionContext(), item, value);
    }


    public void addToTokens(SessionContext ctx, User item, OAuthAccessToken value)
    {
        USER2TOKENRELATIONTOKENSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToTokens(User item, OAuthAccessToken value)
    {
        addToTokens(getSession().getSessionContext(), item, value);
    }


    public void removeFromTokens(SessionContext ctx, User item, OAuthAccessToken value)
    {
        USER2TOKENRELATIONTOKENSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromTokens(User item, OAuthAccessToken value)
    {
        removeFromTokens(getSession().getSessionContext(), item, value);
    }
}
