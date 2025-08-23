package de.hybris.platform.ticket.service;

import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import java.util.List;
import java.util.Set;

public interface TicketSearchService
{
    List<CsTicketModel> searchForTickets(String paramString);


    List<CsTicketModel> searchForTickets(String paramString, Set<CsTicketState> paramSet);


    List<CsTicketModel> searchForTickets(EmployeeModel paramEmployeeModel, CsAgentGroupModel paramCsAgentGroupModel, CsTicketState paramCsTicketState);
}
