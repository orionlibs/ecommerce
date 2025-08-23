package de.hybris.platform.cockpit.reports.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCompiledJasperMedia extends JasperMedia
{
    public static final String COMPILEDREPORT = "compiledReport";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(JasperMedia.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("compiledReport", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Media getCompiledReport(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "compiledReport");
    }


    public Media getCompiledReport()
    {
        return getCompiledReport(getSession().getSessionContext());
    }


    public void setCompiledReport(SessionContext ctx, Media value)
    {
        setProperty(ctx, "compiledReport", value);
    }


    public void setCompiledReport(Media value)
    {
        setCompiledReport(getSession().getSessionContext(), value);
    }
}
