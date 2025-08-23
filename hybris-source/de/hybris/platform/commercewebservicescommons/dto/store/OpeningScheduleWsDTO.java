package de.hybris.platform.commercewebservicescommons.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "OpeningSchedule", description = "Representation of an Opening schedule")
public class OpeningScheduleWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "name", value = "Name of the opening schedule")
    private String name;
    @ApiModelProperty(name = "code", value = "Code of the opening schedule")
    private String code;
    @ApiModelProperty(name = "weekDayOpeningList", value = "List of weekday opening days")
    private List<WeekdayOpeningDayWsDTO> weekDayOpeningList;
    @ApiModelProperty(name = "specialDayOpeningList", value = "List of special opening days")
    private List<SpecialOpeningDayWsDTO> specialDayOpeningList;


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


    public void setWeekDayOpeningList(List<WeekdayOpeningDayWsDTO> weekDayOpeningList)
    {
        this.weekDayOpeningList = weekDayOpeningList;
    }


    public List<WeekdayOpeningDayWsDTO> getWeekDayOpeningList()
    {
        return this.weekDayOpeningList;
    }


    public void setSpecialDayOpeningList(List<SpecialOpeningDayWsDTO> specialDayOpeningList)
    {
        this.specialDayOpeningList = specialDayOpeningList;
    }


    public List<SpecialOpeningDayWsDTO> getSpecialDayOpeningList()
    {
        return this.specialDayOpeningList;
    }
}
