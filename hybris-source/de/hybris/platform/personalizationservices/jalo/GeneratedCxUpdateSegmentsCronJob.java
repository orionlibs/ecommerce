package de.hybris.platform.personalizationservices.jalo;

import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedCxUpdateSegmentsCronJob extends CronJob
{
    public static final String ALLPROVIDERS = "allProviders";
    public static final String PROVIDERS = "providers";
    public static final String FULLUPDATE = "fullUpdate";
    public static final String ALLBASESITES = "allBaseSites";
    public static final String BASESITES = "baseSites";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("allProviders", Item.AttributeMode.INITIAL);
        tmp.put("providers", Item.AttributeMode.INITIAL);
        tmp.put("fullUpdate", Item.AttributeMode.INITIAL);
        tmp.put("allBaseSites", Item.AttributeMode.INITIAL);
        tmp.put("baseSites", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isAllBaseSites(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "allBaseSites");
    }


    public Boolean isAllBaseSites()
    {
        return isAllBaseSites(getSession().getSessionContext());
    }


    public boolean isAllBaseSitesAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAllBaseSites(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAllBaseSitesAsPrimitive()
    {
        return isAllBaseSitesAsPrimitive(getSession().getSessionContext());
    }


    public void setAllBaseSites(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "allBaseSites", value);
    }


    public void setAllBaseSites(Boolean value)
    {
        setAllBaseSites(getSession().getSessionContext(), value);
    }


    public void setAllBaseSites(SessionContext ctx, boolean value)
    {
        setAllBaseSites(ctx, Boolean.valueOf(value));
    }


    public void setAllBaseSites(boolean value)
    {
        setAllBaseSites(getSession().getSessionContext(), value);
    }


    public Boolean isAllProviders(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "allProviders");
    }


    public Boolean isAllProviders()
    {
        return isAllProviders(getSession().getSessionContext());
    }


    public boolean isAllProvidersAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAllProviders(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAllProvidersAsPrimitive()
    {
        return isAllProvidersAsPrimitive(getSession().getSessionContext());
    }


    public void setAllProviders(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "allProviders", value);
    }


    public void setAllProviders(Boolean value)
    {
        setAllProviders(getSession().getSessionContext(), value);
    }


    public void setAllProviders(SessionContext ctx, boolean value)
    {
        setAllProviders(ctx, Boolean.valueOf(value));
    }


    public void setAllProviders(boolean value)
    {
        setAllProviders(getSession().getSessionContext(), value);
    }


    public Set<BaseSite> getBaseSites(SessionContext ctx)
    {
        Set<BaseSite> coll = (Set<BaseSite>)getProperty(ctx, "baseSites");
        return (coll != null) ? coll : Collections.EMPTY_SET;
    }


    public Set<BaseSite> getBaseSites()
    {
        return getBaseSites(getSession().getSessionContext());
    }


    public void setBaseSites(SessionContext ctx, Set<BaseSite> value)
    {
        setProperty(ctx, "baseSites", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setBaseSites(Set<BaseSite> value)
    {
        setBaseSites(getSession().getSessionContext(), value);
    }


    public Boolean isFullUpdate(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "fullUpdate");
    }


    public Boolean isFullUpdate()
    {
        return isFullUpdate(getSession().getSessionContext());
    }


    public boolean isFullUpdateAsPrimitive(SessionContext ctx)
    {
        Boolean value = isFullUpdate(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isFullUpdateAsPrimitive()
    {
        return isFullUpdateAsPrimitive(getSession().getSessionContext());
    }


    public void setFullUpdate(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "fullUpdate", value);
    }


    public void setFullUpdate(Boolean value)
    {
        setFullUpdate(getSession().getSessionContext(), value);
    }


    public void setFullUpdate(SessionContext ctx, boolean value)
    {
        setFullUpdate(ctx, Boolean.valueOf(value));
    }


    public void setFullUpdate(boolean value)
    {
        setFullUpdate(getSession().getSessionContext(), value);
    }


    public Set<String> getProviders(SessionContext ctx)
    {
        Set<String> coll = (Set<String>)getProperty(ctx, "providers");
        return (coll != null) ? coll : Collections.EMPTY_SET;
    }


    public Set<String> getProviders()
    {
        return getProviders(getSession().getSessionContext());
    }


    public void setProviders(SessionContext ctx, Set<String> value)
    {
        setProperty(ctx, "providers", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setProviders(Set<String> value)
    {
        setProviders(getSession().getSessionContext(), value);
    }
}
