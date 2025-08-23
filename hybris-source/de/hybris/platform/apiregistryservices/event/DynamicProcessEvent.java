package de.hybris.platform.apiregistryservices.event;

import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import java.io.Serializable;

public class DynamicProcessEvent extends AbstractEvent
{
    private String businessEvent;
    private BusinessProcessModel businessProcess;


    public DynamicProcessEvent()
    {
    }


    public DynamicProcessEvent(Serializable source)
    {
        super(source);
    }


    public void setBusinessEvent(String businessEvent)
    {
        this.businessEvent = businessEvent;
    }


    public String getBusinessEvent()
    {
        return this.businessEvent;
    }


    public void setBusinessProcess(BusinessProcessModel businessProcess)
    {
        this.businessProcess = businessProcess;
    }


    public BusinessProcessModel getBusinessProcess()
    {
        return this.businessProcess;
    }
}
