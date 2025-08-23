package de.hybris.platform.platformbackoffice.renderers.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

class ClientTimeZoneDateFormatter implements DateFormatter
{
    public static final String DEFAULT_DATE_FORMAT = "MMM dd, yyyy hh:mm a";
    static final String ORG_ZKOSS_WEB_PREFERRED_TIME_ZONE = "org.zkoss.web.preferred.timeZone";
    private String format;


    public String format(Date date, String format, Locale locale)
    {
        String requiredFormat = getRequiredFormat(format);
        String mappedFormat = (new DateFormatMapper()).map(requiredFormat, locale);
        DateFormat dateFormatter = getDateFormatter(mappedFormat, locale);
        return dateFormatter.format(date);
    }


    private String getRequiredFormat(String format)
    {
        return StringUtils.isNotEmpty(format) ? format : getCommonFormat();
    }


    private String getCommonFormat()
    {
        return StringUtils.isNotEmpty(this.format) ? this.format : "MMM dd, yyyy hh:mm a";
    }


    public void setFormat(String format)
    {
        this.format = format;
    }


    public String getFormat()
    {
        return this.format;
    }


    protected DateFormat getDateFormatter(String format, Locale locale)
    {
        DateFormat df = new SimpleDateFormat(format, locale);
        TimeZone timeZone = getClientTimeZone();
        if(timeZone != null)
        {
            df.setTimeZone(timeZone);
        }
        return df;
    }


    private TimeZone getClientTimeZone()
    {
        Session session = Sessions.getCurrent();
        if(session != null)
        {
            Object timeZone = session.getAttribute("org.zkoss.web.preferred.timeZone");
            if(timeZone instanceof TimeZone)
            {
                return (TimeZone)timeZone;
            }
        }
        return null;
    }
}
