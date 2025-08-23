package de.hybris.datasupplier.jalo;

import de.hybris.platform.catalog.jalo.CatalogUnawareMedia;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedDataSupplierCronJob extends CronJob
{
    public static final String SAVEPAYLOAD = "savePayload";
    public static final String RECENTPAYLOAD = "recentPayload";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("savePayload", Item.AttributeMode.INITIAL);
        tmp.put("recentPayload", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public CatalogUnawareMedia getRecentPayload(SessionContext ctx)
    {
        return (CatalogUnawareMedia)getProperty(ctx, "recentPayload");
    }


    public CatalogUnawareMedia getRecentPayload()
    {
        return getRecentPayload(getSession().getSessionContext());
    }


    public void setRecentPayload(SessionContext ctx, CatalogUnawareMedia value)
    {
        setProperty(ctx, "recentPayload", value);
    }


    public void setRecentPayload(CatalogUnawareMedia value)
    {
        setRecentPayload(getSession().getSessionContext(), value);
    }


    public Boolean isSavePayload(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "savePayload");
    }


    public Boolean isSavePayload()
    {
        return isSavePayload(getSession().getSessionContext());
    }


    public boolean isSavePayloadAsPrimitive(SessionContext ctx)
    {
        Boolean value = isSavePayload(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isSavePayloadAsPrimitive()
    {
        return isSavePayloadAsPrimitive(getSession().getSessionContext());
    }


    public void setSavePayload(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "savePayload", value);
    }


    public void setSavePayload(Boolean value)
    {
        setSavePayload(getSession().getSessionContext(), value);
    }


    public void setSavePayload(SessionContext ctx, boolean value)
    {
        setSavePayload(ctx, Boolean.valueOf(value));
    }


    public void setSavePayload(boolean value)
    {
        setSavePayload(getSession().getSessionContext(), value);
    }
}
