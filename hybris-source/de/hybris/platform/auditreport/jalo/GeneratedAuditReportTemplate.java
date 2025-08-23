package de.hybris.platform.auditreport.jalo;

import de.hybris.platform.commons.jalo.renderer.RendererTemplate;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAuditReportTemplate extends RendererTemplate
{
    public static final String INCLUDETEXT = "includeText";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(RendererTemplate.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("includeText", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isIncludeText(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "includeText");
    }


    public Boolean isIncludeText()
    {
        return isIncludeText(getSession().getSessionContext());
    }


    public boolean isIncludeTextAsPrimitive(SessionContext ctx)
    {
        Boolean value = isIncludeText(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isIncludeTextAsPrimitive()
    {
        return isIncludeTextAsPrimitive(getSession().getSessionContext());
    }


    public void setIncludeText(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "includeText", value);
    }


    public void setIncludeText(Boolean value)
    {
        setIncludeText(getSession().getSessionContext(), value);
    }


    public void setIncludeText(SessionContext ctx, boolean value)
    {
        setIncludeText(ctx, Boolean.valueOf(value));
    }


    public void setIncludeText(boolean value)
    {
        setIncludeText(getSession().getSessionContext(), value);
    }
}
