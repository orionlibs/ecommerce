package com.hybris.backoffice.searchservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.searchservices.jalo.SnIndexConfiguration;
import de.hybris.platform.searchservices.jalo.SnIndexType;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedBackofficeIndexedTypeToSearchservicesIndexConfig extends GenericItem
{
    public static final String INDEXEDTYPE = "indexedType";
    public static final String SNINDEXCONFIGURATION = "snIndexConfiguration";
    public static final String SNINDEXTYPE = "snIndexType";
    public static final String ACTIVE = "active";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("indexedType", Item.AttributeMode.INITIAL);
        tmp.put("snIndexConfiguration", Item.AttributeMode.INITIAL);
        tmp.put("snIndexType", Item.AttributeMode.INITIAL);
        tmp.put("active", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isActive(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "active");
    }


    public Boolean isActive()
    {
        return isActive(getSession().getSessionContext());
    }


    public boolean isActiveAsPrimitive(SessionContext ctx)
    {
        Boolean value = isActive(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isActiveAsPrimitive()
    {
        return isActiveAsPrimitive(getSession().getSessionContext());
    }


    public void setActive(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "active", value);
    }


    public void setActive(Boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    public void setActive(SessionContext ctx, boolean value)
    {
        setActive(ctx, Boolean.valueOf(value));
    }


    public void setActive(boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    public ComposedType getIndexedType(SessionContext ctx)
    {
        return (ComposedType)getProperty(ctx, "indexedType");
    }


    public ComposedType getIndexedType()
    {
        return getIndexedType(getSession().getSessionContext());
    }


    public void setIndexedType(SessionContext ctx, ComposedType value)
    {
        setProperty(ctx, "indexedType", value);
    }


    public void setIndexedType(ComposedType value)
    {
        setIndexedType(getSession().getSessionContext(), value);
    }


    public SnIndexConfiguration getSnIndexConfiguration(SessionContext ctx)
    {
        return (SnIndexConfiguration)getProperty(ctx, "snIndexConfiguration");
    }


    public SnIndexConfiguration getSnIndexConfiguration()
    {
        return getSnIndexConfiguration(getSession().getSessionContext());
    }


    public void setSnIndexConfiguration(SessionContext ctx, SnIndexConfiguration value)
    {
        setProperty(ctx, "snIndexConfiguration", value);
    }


    public void setSnIndexConfiguration(SnIndexConfiguration value)
    {
        setSnIndexConfiguration(getSession().getSessionContext(), value);
    }


    public SnIndexType getSnIndexType(SessionContext ctx)
    {
        return (SnIndexType)getProperty(ctx, "snIndexType");
    }


    public SnIndexType getSnIndexType()
    {
        return getSnIndexType(getSession().getSessionContext());
    }


    public void setSnIndexType(SessionContext ctx, SnIndexType value)
    {
        setProperty(ctx, "snIndexType", value);
    }


    public void setSnIndexType(SnIndexType value)
    {
        setSnIndexType(getSession().getSessionContext(), value);
    }
}
