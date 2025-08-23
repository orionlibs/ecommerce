package de.hybris.platform.customersupportbackoffice.accessors.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

public class TicketListPropertyAccessor implements PropertyAccessor
{
    private static final String TICKETS_ATTR = "tickets";


    public Class<?>[] getSpecificTargetClasses()
    {
        Class<?>[] classes = new Class[] {CsTicketModel.class};
        return classes;
    }


    public boolean canRead(EvaluationContext evaluationContext, Object currentObject, String attribute) throws AccessException
    {
        return (isTypeSupported(currentObject) && attribute.equalsIgnoreCase("tickets"));
    }


    protected boolean isTypeSupported(Object object)
    {
        return object instanceof CsTicketModel;
    }


    public TypedValue read(EvaluationContext evaluationContext, Object target, String attribute) throws AccessException
    {
        CsTicketModel currentTicket = (CsTicketModel)target;
        UserModel ticketCustomer = currentTicket.getCustomer();
        if(ticketCustomer instanceof CustomerModel)
        {
            List<CsTicketModel> ticketList = ((CustomerModel)ticketCustomer).getTickets();
            return new TypedValue(ticketList.stream().filter(i -> !i.getTicketID().equals(currentTicket.getTicketID()))
                            .collect(Collectors.toList()));
        }
        return new TypedValue(new ArrayList());
    }


    public boolean canWrite(EvaluationContext evaluationContext, Object currentObject, String attribute) throws AccessException
    {
        return false;
    }


    public void write(EvaluationContext evaluationContext, Object target, String attributeName, Object attributeValue) throws AccessException
    {
    }
}
