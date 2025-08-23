package de.hybris.platform.ticket.strategies;

import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketException;
import java.util.List;

public interface TicketUpdateStrategy
{
    CsTicketModel updateTicket(CsTicketModel paramCsTicketModel) throws TicketException;


    CsTicketModel updateTicket(CsTicketModel paramCsTicketModel, String paramString) throws TicketException;


    void setTicketState(CsTicketModel paramCsTicketModel, CsTicketState paramCsTicketState) throws TicketException;


    void setTicketState(CsTicketModel paramCsTicketModel, CsTicketState paramCsTicketState, String paramString) throws TicketException;


    CsTicketEventModel assignTicketToAgent(CsTicketModel paramCsTicketModel, EmployeeModel paramEmployeeModel) throws TicketException;


    CsTicketEventModel assignTicketToGroup(CsTicketModel paramCsTicketModel, CsAgentGroupModel paramCsAgentGroupModel) throws TicketException;


    List<CsTicketState> getTicketNextStates(CsTicketState paramCsTicketState);
}
