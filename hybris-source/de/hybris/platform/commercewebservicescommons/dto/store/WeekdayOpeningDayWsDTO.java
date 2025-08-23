package de.hybris.platform.commercewebservicescommons.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "WeekdayOpeningDay", description = "Representation of a Weekday Opening Day")
public class WeekdayOpeningDayWsDTO extends OpeningDayWsDTO
{
    @ApiModelProperty(name = "weekDay", value = "Text representation of week day opening day")
    private String weekDay;
    @ApiModelProperty(name = "closed", value = "Flag stating if weekday opening day is closed")
    private Boolean closed;


    public void setWeekDay(String weekDay)
    {
        this.weekDay = weekDay;
    }


    public String getWeekDay()
    {
        return this.weekDay;
    }


    public void setClosed(Boolean closed)
    {
        this.closed = closed;
    }


    public Boolean getClosed()
    {
        return this.closed;
    }
}
