package de.hybris.platform.commerceservices.event;

import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import java.io.Serializable;

public class CreateReturnEvent extends AbstractEvent
{
    private ReturnRequestModel returnRequest;


    public CreateReturnEvent()
    {
    }


    public CreateReturnEvent(Serializable source)
    {
        super(source);
    }


    public void setReturnRequest(ReturnRequestModel returnRequest)
    {
        this.returnRequest = returnRequest;
    }


    public ReturnRequestModel getReturnRequest()
    {
        return this.returnRequest;
    }
}
