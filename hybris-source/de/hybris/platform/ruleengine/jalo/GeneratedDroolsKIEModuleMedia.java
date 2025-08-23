package de.hybris.platform.ruleengine.jalo;

import de.hybris.platform.catalog.jalo.CatalogUnawareMedia;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedDroolsKIEModuleMedia extends CatalogUnawareMedia
{
    public static final String KIEMODULENAME = "kieModuleName";
    public static final String RELEASEID = "releaseId";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CatalogUnawareMedia.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("kieModuleName", Item.AttributeMode.INITIAL);
        tmp.put("releaseId", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getKieModuleName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "kieModuleName");
    }


    public String getKieModuleName()
    {
        return getKieModuleName(getSession().getSessionContext());
    }


    public void setKieModuleName(SessionContext ctx, String value)
    {
        setProperty(ctx, "kieModuleName", value);
    }


    public void setKieModuleName(String value)
    {
        setKieModuleName(getSession().getSessionContext(), value);
    }


    public String getReleaseId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "releaseId");
    }


    public String getReleaseId()
    {
        return getReleaseId(getSession().getSessionContext());
    }


    public void setReleaseId(SessionContext ctx, String value)
    {
        setProperty(ctx, "releaseId", value);
    }


    public void setReleaseId(String value)
    {
        setReleaseId(getSession().getSessionContext(), value);
    }
}
