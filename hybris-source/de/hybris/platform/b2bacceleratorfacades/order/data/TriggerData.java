package de.hybris.platform.b2bacceleratorfacades.order.data;

import de.hybris.platform.cronjob.enums.DayOfWeek;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TriggerData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Integer weekInterval;
    private Integer day;
    private List<DayOfWeek> daysOfWeek;
    private Boolean relative;
    private Date activationTime;
    private Integer month;
    private String displayTimeTable;
    private Date creationTime;
    private Integer hour;
    private Integer minute;


    public void setWeekInterval(Integer weekInterval)
    {
        this.weekInterval = weekInterval;
    }


    public Integer getWeekInterval()
    {
        return this.weekInterval;
    }


    public void setDay(Integer day)
    {
        this.day = day;
    }


    public Integer getDay()
    {
        return this.day;
    }


    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek)
    {
        this.daysOfWeek = daysOfWeek;
    }


    public List<DayOfWeek> getDaysOfWeek()
    {
        return this.daysOfWeek;
    }


    public void setRelative(Boolean relative)
    {
        this.relative = relative;
    }


    public Boolean getRelative()
    {
        return this.relative;
    }


    public void setActivationTime(Date activationTime)
    {
        this.activationTime = activationTime;
    }


    public Date getActivationTime()
    {
        return this.activationTime;
    }


    public void setMonth(Integer month)
    {
        this.month = month;
    }


    public Integer getMonth()
    {
        return this.month;
    }


    public void setDisplayTimeTable(String displayTimeTable)
    {
        this.displayTimeTable = displayTimeTable;
    }


    public String getDisplayTimeTable()
    {
        return this.displayTimeTable;
    }


    public void setCreationTime(Date creationTime)
    {
        this.creationTime = creationTime;
    }


    public Date getCreationTime()
    {
        return this.creationTime;
    }


    public void setHour(Integer hour)
    {
        this.hour = hour;
    }


    public Integer getHour()
    {
        return this.hour;
    }


    public void setMinute(Integer minute)
    {
        this.minute = minute;
    }


    public Integer getMinute()
    {
        return this.minute;
    }
}
