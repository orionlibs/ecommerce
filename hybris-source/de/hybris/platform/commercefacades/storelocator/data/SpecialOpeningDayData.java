package de.hybris.platform.commercefacades.storelocator.data;

import java.util.Date;

public class SpecialOpeningDayData extends OpeningDayData
{
    private Date date;
    private String formattedDate;
    private boolean closed;
    private String name;
    private String comment;


    public void setDate(Date date)
    {
        this.date = date;
    }


    public Date getDate()
    {
        return this.date;
    }


    public void setFormattedDate(String formattedDate)
    {
        this.formattedDate = formattedDate;
    }


    public String getFormattedDate()
    {
        return this.formattedDate;
    }


    public void setClosed(boolean closed)
    {
        this.closed = closed;
    }


    public boolean isClosed()
    {
        return this.closed;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setComment(String comment)
    {
        this.comment = comment;
    }


    public String getComment()
    {
        return this.comment;
    }
}
