package de.hybris.platform.ticket.model;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.ticket.service.TicketService;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;

public class TicketsAttributeHandler implements DynamicAttributeHandler<List<CsTicketModel>, CustomerModel>
{
    @Resource(name = "defaultTicketService")
    private TicketService ticketService;
    @Resource(name = "defaultModelService")
    private ModelService modelService;


    public List<CsTicketModel> get(CustomerModel model)
    {
        if(this.modelService.isNew(model))
        {
            return Collections.emptyList();
        }
        return this.ticketService.getTicketsForCustomer((UserModel)model);
    }


    public void set(CustomerModel model, List<CsTicketModel> csTicketModel)
    {
        for(CsTicketModel ticket : csTicketModel)
        {
            ticket.setCustomer((UserModel)model);
            this.modelService.save(ticket);
        }
    }
}
