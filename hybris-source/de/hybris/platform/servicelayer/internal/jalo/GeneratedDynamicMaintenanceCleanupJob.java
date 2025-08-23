package de.hybris.platform.servicelayer.internal.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedDynamicMaintenanceCleanupJob extends MaintenanceCleanupJob
{
    public static final String SEARCHSCRIPT = "searchScript";
    public static final String PROCESSSCRIPT = "processScript";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(MaintenanceCleanupJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("searchScript", Item.AttributeMode.INITIAL);
        tmp.put("processScript", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getProcessScript(SessionContext ctx)
    {
        return (String)getProperty(ctx, "processScript");
    }


    public String getProcessScript()
    {
        return getProcessScript(getSession().getSessionContext());
    }


    public void setProcessScript(SessionContext ctx, String value)
    {
        setProperty(ctx, "processScript", value);
    }


    public void setProcessScript(String value)
    {
        setProcessScript(getSession().getSessionContext(), value);
    }


    public String getSearchScript(SessionContext ctx)
    {
        return (String)getProperty(ctx, "searchScript");
    }


    public String getSearchScript()
    {
        return getSearchScript(getSession().getSessionContext());
    }


    public void setSearchScript(SessionContext ctx, String value)
    {
        setProperty(ctx, "searchScript", value);
    }


    public void setSearchScript(String value)
    {
        setSearchScript(getSession().getSessionContext(), value);
    }
}
