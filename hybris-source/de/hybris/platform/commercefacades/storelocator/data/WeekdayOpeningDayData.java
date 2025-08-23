package de.hybris.platform.commercefacades.storelocator.data;

public class WeekdayOpeningDayData extends OpeningDayData
{
    private String weekDay;
    private boolean closed;


    public void setWeekDay(String weekDay)
    {
        this.weekDay = weekDay;
    }


    public String getWeekDay()
    {
        return this.weekDay;
    }


    public void setClosed(boolean closed)
    {
        this.closed = closed;
    }


    public boolean isClosed()
    {
        return this.closed;
    }
}
