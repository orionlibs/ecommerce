package de.hybris.platform.util;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

public class StandardDateRange implements DateRange, Serializable
{
    private final Date start;
    private final Date end;
    static final long serialVersionUID = 606153875285740630L;
    public static final String DATe_FORMAT_FACTORY = "standard.datarange.factory.class";


    public StandardDateRange(Date start, Date end)
    {
        if(start == null || end == null || end.before(start))
        {
            throw new IllegalArgumentException("wrong dates for daterange [start=" + start + ",end=" + end + "]");
        }
        this.start = start;
        this.end = end;
    }


    public Date getStart()
    {
        return this.start;
    }


    public Date getEnd()
    {
        return this.end;
    }


    public boolean encloses(Date check)
    {
        return (check != null && (this.start.before(check) || this.start.equals(check)) && (this.end.after(check) || this.end.equals(check)));
    }


    private static DateFormatUtil bootstrapFromSystemProperty(String prop)
    {
        DateFormatUtil tmp = null;
        String className = readClassNameFromProperty(prop, "de.hybris.platform.util.DateFormatUtilImpl");
        try
        {
            ClassLoader ctxLoader = Thread.currentThread().getContextClassLoader();
            tmp = (DateFormatUtil)((ctxLoader == null) ? Class.forName(className) : Class.forName(className, true, ctxLoader)).newInstance();
        }
        catch(Exception e)
        {
            throw new IllegalStateException("date format util factory '" + className + "' is illegal", e);
        }
        return tmp;
    }


    private static String readClassNameFromProperty(String property, String defaultClassName)
    {
        String ret = System.getProperty(property);
        return (ret == null) ? defaultClassName : ret;
    }


    public String toString()
    {
        DateFormat dateFormat = (this.start != null || this.end != null) ? DateFormatUtilFactoryHolder.factory.getDateTimeInstance() : null;
        return "StandardDateRange[start=" + ((this.start != null) ? dateFormat.format(this.start) : "null") + ",end=" + (
                        (this.end != null) ? dateFormat.format(this.end) : "null") + "]";
    }


    public boolean equals(Object object)
    {
        boolean res = (object != null && object instanceof StandardDateRange && this.start.equals(((StandardDateRange)object).start) && this.end.equals(((StandardDateRange)object).end));
        return res;
    }


    public int hashCode()
    {
        return this.start.hashCode() ^ this.end.hashCode();
    }
}
