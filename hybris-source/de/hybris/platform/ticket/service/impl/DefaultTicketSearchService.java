package de.hybris.platform.ticket.service.impl;

import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.ticket.dao.TicketDao;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketSearchService;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTicketSearchService implements TicketSearchService
{
    private TicketDao ticketDao;


    public List<CsTicketModel> searchForTickets(String searchString)
    {
        if("".equals(searchString) || searchString == null)
        {
            return Collections.emptyList();
        }
        return this.ticketDao.findTicketsByStringInTicketOrEvent(searchString);
    }


    public List<CsTicketModel> searchForTickets(String searchString, Set<CsTicketState> states)
    {
        if("".equals(searchString) || searchString == null || states == null || states.isEmpty())
        {
            return Collections.emptyList();
        }
        return this.ticketDao.findTicketsByStringInTicketOrEventAndStates(searchString, states);
    }


    public List<CsTicketModel> searchForTickets(EmployeeModel agent, CsAgentGroupModel group, CsTicketState state)
    {
        if(agent == null && group == null && state == null)
        {
            return Collections.emptyList();
        }
        return this.ticketDao.findTicketsByAgentGroupState(agent, group, state);
    }


    @Required
    public void setTicketDao(TicketDao ticketDao)
    {
        this.ticketDao = ticketDao;
    }
}
