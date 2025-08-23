package de.hybris.platform.webservicescommons.jalo;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SLDSafe
public class OpenIDClientDetails extends OAuthClientDetails
{
    public static final String EXTERNALSCOPECLAIMNAME = "externalScopeClaimName";
    public static final String ISSUER = "issuer";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(OAuthClientDetails.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("externalScopeClaimName", Item.AttributeMode.INITIAL);
        tmp.put("issuer", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getExternalScopeClaimName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "externalScopeClaimName".intern());
    }


    public String getExternalScopeClaimName()
    {
        return getExternalScopeClaimName(getSession().getSessionContext());
    }


    public void setExternalScopeClaimName(SessionContext ctx, String value)
    {
        setProperty(ctx, "externalScopeClaimName".intern(), value);
    }


    public void setExternalScopeClaimName(String value)
    {
        setExternalScopeClaimName(getSession().getSessionContext(), value);
    }


    public String getIssuer(SessionContext ctx)
    {
        return (String)getProperty(ctx, "issuer".intern());
    }


    public String getIssuer()
    {
        return getIssuer(getSession().getSessionContext());
    }


    public void setIssuer(SessionContext ctx, String value)
    {
        setProperty(ctx, "issuer".intern(), value);
    }


    public void setIssuer(String value)
    {
        setIssuer(getSession().getSessionContext(), value);
    }
}
