package de.hybris.y2ysync.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerJob;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedY2YSyncJob extends ServicelayerJob
{
    public static final String STREAMCONFIGURATIONCONTAINER = "streamConfigurationContainer";
    public static final String SYNCTYPE = "syncType";
    public static final String DATAHUBURL = "dataHubUrl";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(ServicelayerJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("streamConfigurationContainer", Item.AttributeMode.INITIAL);
        tmp.put("syncType", Item.AttributeMode.INITIAL);
        tmp.put("dataHubUrl", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getDataHubUrl(SessionContext ctx)
    {
        return (String)getProperty(ctx, "dataHubUrl");
    }


    public String getDataHubUrl()
    {
        return getDataHubUrl(getSession().getSessionContext());
    }


    public void setDataHubUrl(SessionContext ctx, String value)
    {
        setProperty(ctx, "dataHubUrl", value);
    }


    public void setDataHubUrl(String value)
    {
        setDataHubUrl(getSession().getSessionContext(), value);
    }


    public Y2YStreamConfigurationContainer getStreamConfigurationContainer(SessionContext ctx)
    {
        return (Y2YStreamConfigurationContainer)getProperty(ctx, "streamConfigurationContainer");
    }


    public Y2YStreamConfigurationContainer getStreamConfigurationContainer()
    {
        return getStreamConfigurationContainer(getSession().getSessionContext());
    }


    protected void setStreamConfigurationContainer(SessionContext ctx, Y2YStreamConfigurationContainer value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'streamConfigurationContainer' is not changeable", 0);
        }
        setProperty(ctx, "streamConfigurationContainer", value);
    }


    protected void setStreamConfigurationContainer(Y2YStreamConfigurationContainer value)
    {
        setStreamConfigurationContainer(getSession().getSessionContext(), value);
    }


    public EnumerationValue getSyncType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "syncType");
    }


    public EnumerationValue getSyncType()
    {
        return getSyncType(getSession().getSessionContext());
    }


    protected void setSyncType(SessionContext ctx, EnumerationValue value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'syncType' is not changeable", 0);
        }
        setProperty(ctx, "syncType", value);
    }


    protected void setSyncType(EnumerationValue value)
    {
        setSyncType(getSession().getSessionContext(), value);
    }
}
