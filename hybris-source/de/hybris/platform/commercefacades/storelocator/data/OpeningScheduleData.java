package de.hybris.platform.commercefacades.storelocator.data;

import java.io.Serializable;
import java.util.List;

public class OpeningScheduleData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String name;
    private String code;
    private List<WeekdayOpeningDayData> weekDayOpeningList;
    private List<SpecialOpeningDayData> specialDayOpeningList;


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setWeekDayOpeningList(List<WeekdayOpeningDayData> weekDayOpeningList)
    {
        this.weekDayOpeningList = weekDayOpeningList;
    }


    public List<WeekdayOpeningDayData> getWeekDayOpeningList()
    {
        return this.weekDayOpeningList;
    }


    public void setSpecialDayOpeningList(List<SpecialOpeningDayData> specialDayOpeningList)
    {
        this.specialDayOpeningList = specialDayOpeningList;
    }


    public List<SpecialOpeningDayData> getSpecialDayOpeningList()
    {
        return this.specialDayOpeningList;
    }
}
