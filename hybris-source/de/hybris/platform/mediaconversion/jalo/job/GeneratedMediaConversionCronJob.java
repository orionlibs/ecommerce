package de.hybris.platform.mediaconversion.jalo.job;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.mediaconversion.jalo.ConversionMediaFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedMediaConversionCronJob extends AbstractMediaCronJob
{
    public static final String INCLUDEDFORMATS = "includedFormats";
    public static final String ASYNCHRONOUS = "asynchronous";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractMediaCronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("includedFormats", Item.AttributeMode.INITIAL);
        tmp.put("asynchronous", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isAsynchronous(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "asynchronous");
    }


    public Boolean isAsynchronous()
    {
        return isAsynchronous(getSession().getSessionContext());
    }


    public boolean isAsynchronousAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAsynchronous(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAsynchronousAsPrimitive()
    {
        return isAsynchronousAsPrimitive(getSession().getSessionContext());
    }


    public void setAsynchronous(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "asynchronous", value);
    }


    public void setAsynchronous(Boolean value)
    {
        setAsynchronous(getSession().getSessionContext(), value);
    }


    public void setAsynchronous(SessionContext ctx, boolean value)
    {
        setAsynchronous(ctx, Boolean.valueOf(value));
    }


    public void setAsynchronous(boolean value)
    {
        setAsynchronous(getSession().getSessionContext(), value);
    }


    public Collection<ConversionMediaFormat> getIncludedFormats(SessionContext ctx)
    {
        Collection<ConversionMediaFormat> coll = (Collection<ConversionMediaFormat>)getProperty(ctx, "includedFormats");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<ConversionMediaFormat> getIncludedFormats()
    {
        return getIncludedFormats(getSession().getSessionContext());
    }


    public void setIncludedFormats(SessionContext ctx, Collection<ConversionMediaFormat> value)
    {
        setProperty(ctx, "includedFormats", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setIncludedFormats(Collection<ConversionMediaFormat> value)
    {
        setIncludedFormats(getSession().getSessionContext(), value);
    }
}
