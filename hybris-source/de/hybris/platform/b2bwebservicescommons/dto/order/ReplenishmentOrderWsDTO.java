package de.hybris.platform.b2bwebservicescommons.dto.order;

import de.hybris.platform.commercewebservicescommons.dto.order.CartWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

@ApiModel(value = "ReplenishmentOrder", description = "Request body fields required and optional to operate on Replenishment Order data.")
public class ReplenishmentOrderWsDTO extends CartWsDTO
{
    @ApiModelProperty(name = "active", value = "Is the Replenishment Order active", example = "Boolean.FALSE")
    private Boolean active;
    @ApiModelProperty(name = "trigger", value = "Trigger for the replenishment order")
    private TriggerWsDTO trigger;
    @ApiModelProperty(name = "firstDate", value = "First date of the replenishment order", example = "2020-12-31T09:00:00+0000")
    private Date firstDate;
    @ApiModelProperty(name = "replenishmentOrderCode", value = "Unique code for the replenishment order", required = true, example = "502BJ")
    private String replenishmentOrderCode;


    public void setActive(Boolean active)
    {
        this.active = active;
    }


    public Boolean getActive()
    {
        return this.active;
    }


    public void setTrigger(TriggerWsDTO trigger)
    {
        this.trigger = trigger;
    }


    public TriggerWsDTO getTrigger()
    {
        return this.trigger;
    }


    public void setFirstDate(Date firstDate)
    {
        this.firstDate = firstDate;
    }


    public Date getFirstDate()
    {
        return this.firstDate;
    }


    public void setReplenishmentOrderCode(String replenishmentOrderCode)
    {
        this.replenishmentOrderCode = replenishmentOrderCode;
    }


    public String getReplenishmentOrderCode()
    {
        return this.replenishmentOrderCode;
    }
}
