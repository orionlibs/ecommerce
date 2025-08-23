package de.hybris.platform.util;

import java.util.Date;

public class EJBStandardDateRange implements EJBDateRange
{
    private final Date start;
    private final Date end;


    public EJBStandardDateRange(Date start, Date end)
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


    public String toString()
    {
        return "StandardDateRange[start=" + getStart() + ",end=" + getEnd() + "]";
    }
}
