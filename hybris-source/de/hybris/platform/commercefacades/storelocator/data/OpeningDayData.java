package de.hybris.platform.commercefacades.storelocator.data;

import java.io.Serializable;

public class OpeningDayData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private TimeData openingTime;
    private TimeData closingTime;


    public void setOpeningTime(TimeData openingTime)
    {
        this.openingTime = openingTime;
    }


    public TimeData getOpeningTime()
    {
        return this.openingTime;
    }


    public void setClosingTime(TimeData closingTime)
    {
        this.closingTime = closingTime;
    }


    public TimeData getClosingTime()
    {
        return this.closingTime;
    }
}
