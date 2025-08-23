package de.hybris.platform.ruleengine.jalo;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCatalogVersionToRuleEngineContextMapping extends GenericItem
{
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String CONTEXT = "context";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("catalogVersion", Item.AttributeMode.INITIAL);
        tmp.put("context", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public CatalogVersion getCatalogVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "catalogVersion");
    }


    public CatalogVersion getCatalogVersion()
    {
        return getCatalogVersion(getSession().getSessionContext());
    }


    public void setCatalogVersion(SessionContext ctx, CatalogVersion value)
    {
        setProperty(ctx, "catalogVersion", value);
    }


    public void setCatalogVersion(CatalogVersion value)
    {
        setCatalogVersion(getSession().getSessionContext(), value);
    }


    public AbstractRuleEngineContext getContext(SessionContext ctx)
    {
        return (AbstractRuleEngineContext)getProperty(ctx, "context");
    }


    public AbstractRuleEngineContext getContext()
    {
        return getContext(getSession().getSessionContext());
    }


    public void setContext(SessionContext ctx, AbstractRuleEngineContext value)
    {
        setProperty(ctx, "context", value);
    }


    public void setContext(AbstractRuleEngineContext value)
    {
        setContext(getSession().getSessionContext(), value);
    }
}
