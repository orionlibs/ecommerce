package de.hybris.platform.webservicescommons.jaxb.adapters;

import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateAdapter extends XmlAdapter<String, Date>
{
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ").withZoneUTC();


    public String marshal(Date d)
    {
        if(d == null)
        {
            return null;
        }
        return dateFormat.print(d.getTime());
    }


    public Date unmarshal(String d)
    {
        if(StringUtils.isBlank(d))
        {
            return null;
        }
        return dateFormat.parseDateTime(d).toDate();
    }
}
