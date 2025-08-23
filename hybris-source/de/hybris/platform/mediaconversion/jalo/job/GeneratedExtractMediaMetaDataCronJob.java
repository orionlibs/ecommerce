package de.hybris.platform.mediaconversion.jalo.job;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedExtractMediaMetaDataCronJob extends AbstractMediaCronJob
{
    public static final String INCLUDECONVERTED = "includeConverted";
    public static final String CONTAINERMEDIASONLY = "containerMediasOnly";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractMediaCronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("includeConverted", Item.AttributeMode.INITIAL);
        tmp.put("containerMediasOnly", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isContainerMediasOnly(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "containerMediasOnly");
    }


    public Boolean isContainerMediasOnly()
    {
        return isContainerMediasOnly(getSession().getSessionContext());
    }


    public boolean isContainerMediasOnlyAsPrimitive(SessionContext ctx)
    {
        Boolean value = isContainerMediasOnly(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isContainerMediasOnlyAsPrimitive()
    {
        return isContainerMediasOnlyAsPrimitive(getSession().getSessionContext());
    }


    public void setContainerMediasOnly(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "containerMediasOnly", value);
    }


    public void setContainerMediasOnly(Boolean value)
    {
        setContainerMediasOnly(getSession().getSessionContext(), value);
    }


    public void setContainerMediasOnly(SessionContext ctx, boolean value)
    {
        setContainerMediasOnly(ctx, Boolean.valueOf(value));
    }


    public void setContainerMediasOnly(boolean value)
    {
        setContainerMediasOnly(getSession().getSessionContext(), value);
    }


    public Boolean isIncludeConverted(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "includeConverted");
    }


    public Boolean isIncludeConverted()
    {
        return isIncludeConverted(getSession().getSessionContext());
    }


    public boolean isIncludeConvertedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isIncludeConverted(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isIncludeConvertedAsPrimitive()
    {
        return isIncludeConvertedAsPrimitive(getSession().getSessionContext());
    }


    public void setIncludeConverted(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "includeConverted", value);
    }


    public void setIncludeConverted(Boolean value)
    {
        setIncludeConverted(getSession().getSessionContext(), value);
    }


    public void setIncludeConverted(SessionContext ctx, boolean value)
    {
        setIncludeConverted(ctx, Boolean.valueOf(value));
    }


    public void setIncludeConverted(boolean value)
    {
        setIncludeConverted(getSession().getSessionContext(), value);
    }
}
