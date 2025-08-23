package de.hybris.y2ysync.jalo.media;

import de.hybris.platform.catalog.jalo.CatalogUnawareMedia;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedConsumeMarkerMedia extends CatalogUnawareMedia
{
    public static final String SYNCEXECUTIONID = "syncExecutionID";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CatalogUnawareMedia.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("syncExecutionID", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getSyncExecutionID(SessionContext ctx)
    {
        return (String)getProperty(ctx, "syncExecutionID");
    }


    public String getSyncExecutionID()
    {
        return getSyncExecutionID(getSession().getSessionContext());
    }


    protected void setSyncExecutionID(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'syncExecutionID' is not changeable", 0);
        }
        setProperty(ctx, "syncExecutionID", value);
    }


    protected void setSyncExecutionID(String value)
    {
        setSyncExecutionID(getSession().getSessionContext(), value);
    }
}
