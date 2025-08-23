package de.hybris.platform.commercefacades.storelocator.data;

import java.io.Serializable;

public class TimeData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private byte hour;
    private byte minute;
    private String formattedHour;


    public void setHour(byte hour)
    {
        this.hour = hour;
    }


    public byte getHour()
    {
        return this.hour;
    }


    public void setMinute(byte minute)
    {
        this.minute = minute;
    }


    public byte getMinute()
    {
        return this.minute;
    }


    public void setFormattedHour(String formattedHour)
    {
        this.formattedHour = formattedHour;
    }


    public String getFormattedHour()
    {
        return this.formattedHour;
    }
}
