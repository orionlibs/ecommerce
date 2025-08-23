package de.hybris.platform.customersupportbackoffice.widgets.sessioncontext.model;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import java.util.Map;

public class SessionContextModel
{
    private UserModel currentCustomer;
    private CsTicketModel currentTicket;
    private AbstractOrderModel currentOrder;
    private Map<String, ReturnRequestModel> currentOrderReturns;


    public UserModel getCurrentCustomer()
    {
        return this.currentCustomer;
    }


    public void setCurrentCustomer(UserModel currentCustomer)
    {
        this.currentCustomer = currentCustomer;
    }


    public CsTicketModel getCurrentTicket()
    {
        return this.currentTicket;
    }


    public void setCurrentTicket(CsTicketModel currentTicket)
    {
        this.currentTicket = currentTicket;
    }


    public AbstractOrderModel getCurrentOrder()
    {
        return this.currentOrder;
    }


    public void setCurrentOrder(AbstractOrderModel currentOrder)
    {
        this.currentOrder = currentOrder;
    }


    public Map<String, ReturnRequestModel> getCurrentOrderReturns()
    {
        return this.currentOrderReturns;
    }


    public void setCurrentOrderReturns(Map<String, ReturnRequestModel> currentOrderReturns)
    {
        this.currentOrderReturns = currentOrderReturns;
    }
}
