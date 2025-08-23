package de.hybris.platform.cockpit.reports.jalo;

import de.hybris.platform.cockpit.jalo.DynamicWidgetPreferences;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedJasperWidgetPreferences extends DynamicWidgetPreferences
{
    public static final String REPORT = "report";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(DynamicWidgetPreferences.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("report", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public JasperMedia getReport(SessionContext ctx)
    {
        return (JasperMedia)getProperty(ctx, "report");
    }


    public JasperMedia getReport()
    {
        return getReport(getSession().getSessionContext());
    }


    public void setReport(SessionContext ctx, JasperMedia value)
    {
        setProperty(ctx, "report", value);
    }


    public void setReport(JasperMedia value)
    {
        setReport(getSession().getSessionContext(), value);
    }
}
