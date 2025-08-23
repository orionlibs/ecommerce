package de.hybris.platform.commercewebservicescommons.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "OpeningDay", description = "List of OpeningDay")
public class OpeningDayWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "openingTime", value = "Starting time of opening day")
    private TimeWsDTO openingTime;
    @ApiModelProperty(name = "closingTime", value = "Closing time of opening day")
    private TimeWsDTO closingTime;


    public void setOpeningTime(TimeWsDTO openingTime)
    {
        this.openingTime = openingTime;
    }


    public TimeWsDTO getOpeningTime()
    {
        return this.openingTime;
    }


    public void setClosingTime(TimeWsDTO closingTime)
    {
        this.closingTime = closingTime;
    }


    public TimeWsDTO getClosingTime()
    {
        return this.closingTime;
    }
}
