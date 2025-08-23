package de.hybris.y2ysync.jalo;

import de.hybris.deltadetection.jalo.StreamConfiguration;
import de.hybris.deltadetection.jalo.StreamConfigurationContainer;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.y2ysync.constants.GeneratedY2ysyncConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedY2YStreamConfigurationContainer extends StreamConfigurationContainer
{
    public static final String FEED = "feed";
    public static final String POOL = "pool";
    public static final String TARGETSYSTEM = "targetSystem";
    public static final String DATAHUBEXTENSION = "dataHubExtension";
    public static final String CATALOGVERSION = "catalogVersion";
    protected static final OneToManyHandler<StreamConfiguration> CONFIGURATIONSHANDLER = new OneToManyHandler(GeneratedY2ysyncConstants.TC.Y2YSTREAMCONFIGURATION, true, "container", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(StreamConfigurationContainer.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("feed", Item.AttributeMode.INITIAL);
        tmp.put("pool", Item.AttributeMode.INITIAL);
        tmp.put("targetSystem", Item.AttributeMode.INITIAL);
        tmp.put("dataHubExtension", Item.AttributeMode.INITIAL);
        tmp.put("catalogVersion", Item.AttributeMode.INITIAL);
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


    public Set<StreamConfiguration> getConfigurations(SessionContext ctx)
    {
        return (Set<StreamConfiguration>)CONFIGURATIONSHANDLER.getValues(ctx, (Item)this);
    }


    public Set<StreamConfiguration> getConfigurations()
    {
        return getConfigurations(getSession().getSessionContext());
    }


    public void setConfigurations(SessionContext ctx, Set<StreamConfiguration> value)
    {
        CONFIGURATIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setConfigurations(Set<StreamConfiguration> value)
    {
        setConfigurations(getSession().getSessionContext(), value);
    }


    public void addToConfigurations(SessionContext ctx, Y2YStreamConfiguration value)
    {
        CONFIGURATIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToConfigurations(Y2YStreamConfiguration value)
    {
        addToConfigurations(getSession().getSessionContext(), value);
    }


    public void removeFromConfigurations(SessionContext ctx, Y2YStreamConfiguration value)
    {
        CONFIGURATIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromConfigurations(Y2YStreamConfiguration value)
    {
        removeFromConfigurations(getSession().getSessionContext(), value);
    }


    public String getDataHubExtension(SessionContext ctx)
    {
        return (String)getProperty(ctx, "dataHubExtension");
    }


    public String getDataHubExtension()
    {
        return getDataHubExtension(getSession().getSessionContext());
    }


    public void setDataHubExtension(SessionContext ctx, String value)
    {
        setProperty(ctx, "dataHubExtension", value);
    }


    public void setDataHubExtension(String value)
    {
        setDataHubExtension(getSession().getSessionContext(), value);
    }


    public String getFeed(SessionContext ctx)
    {
        return (String)getProperty(ctx, "feed");
    }


    public String getFeed()
    {
        return getFeed(getSession().getSessionContext());
    }


    public void setFeed(SessionContext ctx, String value)
    {
        setProperty(ctx, "feed", value);
    }


    public void setFeed(String value)
    {
        setFeed(getSession().getSessionContext(), value);
    }


    public String getPool(SessionContext ctx)
    {
        return (String)getProperty(ctx, "pool");
    }


    public String getPool()
    {
        return getPool(getSession().getSessionContext());
    }


    public void setPool(SessionContext ctx, String value)
    {
        setProperty(ctx, "pool", value);
    }


    public void setPool(String value)
    {
        setPool(getSession().getSessionContext(), value);
    }


    public String getTargetSystem(SessionContext ctx)
    {
        return (String)getProperty(ctx, "targetSystem");
    }


    public String getTargetSystem()
    {
        return getTargetSystem(getSession().getSessionContext());
    }


    public void setTargetSystem(SessionContext ctx, String value)
    {
        setProperty(ctx, "targetSystem", value);
    }


    public void setTargetSystem(String value)
    {
        setTargetSystem(getSession().getSessionContext(), value);
    }
}
