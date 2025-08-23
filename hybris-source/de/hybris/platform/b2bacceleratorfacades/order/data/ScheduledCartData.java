package de.hybris.platform.b2bacceleratorfacades.order.data;

import de.hybris.platform.commercefacades.order.data.CartData;
import java.util.Date;

public class ScheduledCartData extends CartData
{
    private boolean active;
    private TriggerData triggerData;
    private Date firstOrderDate;
    private String jobCode;


    public void setActive(boolean active)
    {
        this.active = active;
    }


    public boolean isActive()
    {
        return this.active;
    }


    public void setTriggerData(TriggerData triggerData)
    {
        this.triggerData = triggerData;
    }


    public TriggerData getTriggerData()
    {
        return this.triggerData;
    }


    public void setFirstOrderDate(Date firstOrderDate)
    {
        this.firstOrderDate = firstOrderDate;
    }


    public Date getFirstOrderDate()
    {
        return this.firstOrderDate;
    }


    public void setJobCode(String jobCode)
    {
        this.jobCode = jobCode;
    }


    public String getJobCode()
    {
        return this.jobCode;
    }
}
