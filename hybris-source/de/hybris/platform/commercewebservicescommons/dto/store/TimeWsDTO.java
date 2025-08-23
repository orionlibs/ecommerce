package de.hybris.platform.commercewebservicescommons.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "Time", description = "Representation of a Time")
public class TimeWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "hour", value = "Hour part of the time data")
    private Byte hour;
    @ApiModelProperty(name = "minute", value = "Minute part of the time data")
    private Byte minute;
    @ApiModelProperty(name = "formattedHour", value = "Formatted hour")
    private String formattedHour;


    public void setHour(Byte hour)
    {
        this.hour = hour;
    }


    public Byte getHour()
    {
        return this.hour;
    }


    public void setMinute(Byte minute)
    {
        this.minute = minute;
    }


    public Byte getMinute()
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
