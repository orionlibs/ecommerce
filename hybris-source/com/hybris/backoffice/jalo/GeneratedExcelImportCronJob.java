package com.hybris.backoffice.jalo;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedExcelImportCronJob extends CronJob
{
    public static final String EXCELFILE = "excelFile";
    public static final String REFERENCEDCONTENT = "referencedContent";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("excelFile", Item.AttributeMode.INITIAL);
        tmp.put("referencedContent", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Media getExcelFile(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "excelFile");
    }


    public Media getExcelFile()
    {
        return getExcelFile(getSession().getSessionContext());
    }


    public void setExcelFile(SessionContext ctx, Media value)
    {
        setProperty(ctx, "excelFile", value);
    }


    public void setExcelFile(Media value)
    {
        setExcelFile(getSession().getSessionContext(), value);
    }


    public Media getReferencedContent(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "referencedContent");
    }


    public Media getReferencedContent()
    {
        return getReferencedContent(getSession().getSessionContext());
    }


    public void setReferencedContent(SessionContext ctx, Media value)
    {
        setProperty(ctx, "referencedContent", value);
    }


    public void setReferencedContent(Media value)
    {
        setReferencedContent(getSession().getSessionContext(), value);
    }
}
