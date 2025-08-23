package de.hybris.platform.webservicescommons.jalo;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SLDSafe
public class OpenIDExternalScopes extends GenericItem
{
    public static final String CODE = "code";
    public static final String CLIENTDETAILSID = "clientDetailsId";
    public static final String PERMITTEDPRINCIPALS = "permittedPrincipals";
    public static final String SCOPE = "scope";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("clientDetailsId", Item.AttributeMode.INITIAL);
        tmp.put("permittedPrincipals", Item.AttributeMode.INITIAL);
        tmp.put("scope", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public OpenIDClientDetails getClientDetailsId(SessionContext ctx)
    {
        return (OpenIDClientDetails)getProperty(ctx, "clientDetailsId".intern());
    }


    public OpenIDClientDetails getClientDetailsId()
    {
        return getClientDetailsId(getSession().getSessionContext());
    }


    public void setClientDetailsId(SessionContext ctx, OpenIDClientDetails value)
    {
        setProperty(ctx, "clientDetailsId".intern(), value);
    }


    public void setClientDetailsId(OpenIDClientDetails value)
    {
        setClientDetailsId(getSession().getSessionContext(), value);
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


    public Collection<Principal> getPermittedPrincipals(SessionContext ctx)
    {
        Collection<Principal> coll = (Collection<Principal>)getProperty(ctx, "permittedPrincipals".intern());
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Principal> getPermittedPrincipals()
    {
        return getPermittedPrincipals(getSession().getSessionContext());
    }


    public void setPermittedPrincipals(SessionContext ctx, Collection<Principal> value)
    {
        setProperty(ctx, "permittedPrincipals".intern(), (value == null || !value.isEmpty()) ? value : null);
    }


    public void setPermittedPrincipals(Collection<Principal> value)
    {
        setPermittedPrincipals(getSession().getSessionContext(), value);
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
