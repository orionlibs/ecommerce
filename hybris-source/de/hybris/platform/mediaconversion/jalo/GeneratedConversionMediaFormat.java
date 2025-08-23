package de.hybris.platform.mediaconversion.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedConversionMediaFormat extends MediaFormat
{
    public static final String MIMETYPE = "mimeType";
    public static final String CONVERSION = "conversion";
    public static final String CONVERSIONSTRATEGY = "conversionStrategy";
    public static final String INPUTFORMAT = "inputFormat";
    public static final String MEDIAADDONS = "mediaAddOns";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(MediaFormat.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("mimeType", Item.AttributeMode.INITIAL);
        tmp.put("conversion", Item.AttributeMode.INITIAL);
        tmp.put("conversionStrategy", Item.AttributeMode.INITIAL);
        tmp.put("inputFormat", Item.AttributeMode.INITIAL);
        tmp.put("mediaAddOns", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getConversion(SessionContext ctx)
    {
        return (String)getProperty(ctx, "conversion");
    }


    public String getConversion()
    {
        return getConversion(getSession().getSessionContext());
    }


    public void setConversion(SessionContext ctx, String value)
    {
        setProperty(ctx, "conversion", value);
    }


    public void setConversion(String value)
    {
        setConversion(getSession().getSessionContext(), value);
    }


    public String getConversionStrategy(SessionContext ctx)
    {
        return (String)getProperty(ctx, "conversionStrategy");
    }


    public String getConversionStrategy()
    {
        return getConversionStrategy(getSession().getSessionContext());
    }


    public void setConversionStrategy(SessionContext ctx, String value)
    {
        setProperty(ctx, "conversionStrategy", value);
    }


    public void setConversionStrategy(String value)
    {
        setConversionStrategy(getSession().getSessionContext(), value);
    }


    public ConversionMediaFormat getInputFormat(SessionContext ctx)
    {
        return (ConversionMediaFormat)getProperty(ctx, "inputFormat");
    }


    public ConversionMediaFormat getInputFormat()
    {
        return getInputFormat(getSession().getSessionContext());
    }


    public void setInputFormat(SessionContext ctx, ConversionMediaFormat value)
    {
        setProperty(ctx, "inputFormat", value);
    }


    public void setInputFormat(ConversionMediaFormat value)
    {
        setInputFormat(getSession().getSessionContext(), value);
    }


    public List<Media> getMediaAddOns(SessionContext ctx)
    {
        List<Media> coll = (List<Media>)getProperty(ctx, "mediaAddOns");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public List<Media> getMediaAddOns()
    {
        return getMediaAddOns(getSession().getSessionContext());
    }


    public void setMediaAddOns(SessionContext ctx, List<Media> value)
    {
        setProperty(ctx, "mediaAddOns", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setMediaAddOns(List<Media> value)
    {
        setMediaAddOns(getSession().getSessionContext(), value);
    }


    public String getMimeType(SessionContext ctx)
    {
        return (String)getProperty(ctx, "mimeType");
    }


    public String getMimeType()
    {
        return getMimeType(getSession().getSessionContext());
    }


    public void setMimeType(SessionContext ctx, String value)
    {
        setProperty(ctx, "mimeType", value);
    }


    public void setMimeType(String value)
    {
        setMimeType(getSession().getSessionContext(), value);
    }
}
