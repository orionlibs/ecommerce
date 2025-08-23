package de.hybris.platform.orderprocessing.events;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public class ConsignmentProcessingEvent extends AbstractEvent
{
    private final ConsignmentProcessModel process;
    private final ConsignmentStatus consignmentStatus;


    public ConsignmentProcessingEvent(ConsignmentProcessModel process)
    {
        this.process = process;
        if(process != null)
        {
            ConsignmentModel consignment = process.getConsignment();
            this.consignmentStatus = (consignment == null) ? null : consignment.getStatus();
        }
        else
        {
            this.consignmentStatus = null;
        }
    }


    public ConsignmentProcessModel getProcess()
    {
        return this.process;
    }


    public ConsignmentStatus getConsignmentStatus()
    {
        return this.consignmentStatus;
    }
}
