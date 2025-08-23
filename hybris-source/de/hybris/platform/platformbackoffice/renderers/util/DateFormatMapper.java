package de.hybris.platform.platformbackoffice.renderers.util;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.text.DateFormats;

class DateFormatMapper
{
    private static final Logger LOG = LoggerFactory.getLogger(DateFormatMapper.class);
    protected static final int FORMAT_DEFAULT = 2;
    private static final Map<String, Integer> FORMAT_MAPPING;
    private static final int FORMAT_NONE = -1;

    static
    {
        Map<String, Integer> tmpDateformatMapping = new HashMap<>();
        tmpDateformatMapping.put("short", Integer.valueOf(3));
        tmpDateformatMapping.put("medium", Integer.valueOf(2));
        tmpDateformatMapping.put("long", Integer.valueOf(1));
        tmpDateformatMapping.put("full", Integer.valueOf(0));
        tmpDateformatMapping.put("none", Integer.valueOf(-1));
        FORMAT_MAPPING = Collections.unmodifiableMap(tmpDateformatMapping);
    }

    public String map(String format, Locale locale)
    {
        if(format == null)
        {
            return null;
        }
        return getNamedDateFormat(format, locale).orElseGet(() -> (String)getPatternDateFormat(format, locale).orElse(null));
    }


    private Optional<String> getNamedDateFormat(String format, Locale locale)
    {
        if(FORMAT_MAPPING.containsKey(format))
        {
            int dateStyle = ((Integer)FORMAT_MAPPING.get(format)).intValue();
            if(dateStyle == -1)
            {
                dateStyle = 2;
            }
            return Optional.ofNullable(getDateTimeFormat(dateStyle, dateStyle, locale));
        }
        return Optional.empty();
    }


    private String getDateTimeFormat(int dateStyle, int timeStyle, Locale locale)
    {
        return DateFormats.getDateTimeFormat(dateStyle, timeStyle, locale, null);
    }


    private Optional<String> getPatternDateFormat(String formatInput, Locale locale)
    {
        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatInput, locale);
            simpleDateFormat.toString();
            return Optional.of(formatInput);
        }
        catch(IllegalArgumentException e)
        {
            String message = String.format("Date format '%s' is invalid! Default format is used.", new Object[] {formatInput});
            if(LOG.isDebugEnabled())
            {
                LOG.warn(message, e);
            }
            else
            {
                LOG.warn(message);
            }
            return Optional.empty();
        }
    }
}
