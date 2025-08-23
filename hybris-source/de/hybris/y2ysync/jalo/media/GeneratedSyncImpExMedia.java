package de.hybris.y2ysync.jalo.media;

import de.hybris.platform.catalog.jalo.CatalogUnawareMedia;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.y2ysync.jalo.Y2YSyncCronJob;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSyncImpExMedia extends CatalogUnawareMedia
{
    public static final String SYNCTYPE = "syncType";
    public static final String IMPEXHEADER = "impexHeader";
    public static final String DATAHUBCOLUMNS = "dataHubColumns";
    public static final String MEDIAARCHIVE = "mediaArchive";
    public static final String EXPORTCRONJOB = "exportCronJob";
    public static final String DATAHUBTYPE = "dataHubType";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CatalogUnawareMedia.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("syncType", Item.AttributeMode.INITIAL);
        tmp.put("impexHeader", Item.AttributeMode.INITIAL);
        tmp.put("dataHubColumns", Item.AttributeMode.INITIAL);
        tmp.put("mediaArchive", Item.AttributeMode.INITIAL);
        tmp.put("exportCronJob", Item.AttributeMode.INITIAL);
        tmp.put("dataHubType", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getDataHubColumns(SessionContext ctx)
    {
        return (String)getProperty(ctx, "dataHubColumns");
    }


    public String getDataHubColumns()
    {
        return getDataHubColumns(getSession().getSessionContext());
    }


    public void setDataHubColumns(SessionContext ctx, String value)
    {
        setProperty(ctx, "dataHubColumns", value);
    }


    public void setDataHubColumns(String value)
    {
        setDataHubColumns(getSession().getSessionContext(), value);
    }


    public String getDataHubType(SessionContext ctx)
    {
        return (String)getProperty(ctx, "dataHubType");
    }


    public String getDataHubType()
    {
        return getDataHubType(getSession().getSessionContext());
    }


    public void setDataHubType(SessionContext ctx, String value)
    {
        setProperty(ctx, "dataHubType", value);
    }


    public void setDataHubType(String value)
    {
        setDataHubType(getSession().getSessionContext(), value);
    }


    public Y2YSyncCronJob getExportCronJob(SessionContext ctx)
    {
        return (Y2YSyncCronJob)getProperty(ctx, "exportCronJob");
    }


    public Y2YSyncCronJob getExportCronJob()
    {
        return getExportCronJob(getSession().getSessionContext());
    }


    public void setExportCronJob(SessionContext ctx, Y2YSyncCronJob value)
    {
        setProperty(ctx, "exportCronJob", value);
    }


    public void setExportCronJob(Y2YSyncCronJob value)
    {
        setExportCronJob(getSession().getSessionContext(), value);
    }


    public String getImpexHeader(SessionContext ctx)
    {
        return (String)getProperty(ctx, "impexHeader");
    }


    public String getImpexHeader()
    {
        return getImpexHeader(getSession().getSessionContext());
    }


    public void setImpexHeader(SessionContext ctx, String value)
    {
        setProperty(ctx, "impexHeader", value);
    }


    public void setImpexHeader(String value)
    {
        setImpexHeader(getSession().getSessionContext(), value);
    }


    public Media getMediaArchive(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "mediaArchive");
    }


    public Media getMediaArchive()
    {
        return getMediaArchive(getSession().getSessionContext());
    }


    public void setMediaArchive(SessionContext ctx, Media value)
    {
        setProperty(ctx, "mediaArchive", value);
    }


    public void setMediaArchive(Media value)
    {
        setMediaArchive(getSession().getSessionContext(), value);
    }


    public ComposedType getSyncType(SessionContext ctx)
    {
        return (ComposedType)getProperty(ctx, "syncType");
    }


    public ComposedType getSyncType()
    {
        return getSyncType(getSession().getSessionContext());
    }


    protected void setSyncType(SessionContext ctx, ComposedType value)
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


    protected void setSyncType(ComposedType value)
    {
        setSyncType(getSession().getSessionContext(), value);
    }
}
