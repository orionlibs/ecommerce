package de.hybris.platform.b2bwebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "Trigger", description = "Representation of a Trigger")
public class TriggerWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "activationTime", value = "The time the trigger will be activated", example = "2020-12-31T09:00:00+0000")
    private Date activationTime;
    @ApiModelProperty(name = "displayTimeTable", value = "Description of when the trigger is being activated", example = "Every Monday at 00:00:00")
    private String displayTimeTable;


    public void setActivationTime(Date activationTime)
    {
        this.activationTime = activationTime;
    }


    public Date getActivationTime()
    {
        return this.activationTime;
    }


    public void setDisplayTimeTable(String displayTimeTable)
    {
        this.displayTimeTable = displayTimeTable;
    }


    public String getDisplayTimeTable()
    {
        return this.displayTimeTable;
    }
}
