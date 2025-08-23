package de.hybris.platform.ticket.service;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.ticket.enums.CsEventReason;
import de.hybris.platform.ticket.enums.CsInterventionType;
import de.hybris.platform.ticket.enums.CsResolutionType;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.events.model.CsCustomerEventModel;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.events.model.CsTicketResolutionEventModel;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticketsystem.data.CsTicketParameter;
import java.util.Collection;
import java.util.List;

public interface TicketBusinessService
{
    CsTicketModel createTicket(CsTicketParameter paramCsTicketParameter);


    @Deprecated(since = "6.0", forRemoval = true)
    CsTicketModel createTicket(CsTicketModel paramCsTicketModel, CsCustomerEventModel paramCsCustomerEventModel);


    CsTicketModel updateTicket(CsTicketModel paramCsTicketModel) throws TicketException;


    CsTicketModel updateTicket(CsTicketModel paramCsTicketModel, String paramString) throws TicketException;


    CsTicketModel setTicketState(CsTicketModel paramCsTicketModel, CsTicketState paramCsTicketState) throws TicketException;


    CsTicketModel setTicketState(CsTicketModel paramCsTicketModel, CsTicketState paramCsTicketState, String paramString) throws TicketException;


    CsTicketModel assignTicketToAgent(CsTicketModel paramCsTicketModel, EmployeeModel paramEmployeeModel) throws TicketException;


    CsTicketModel assignTicketToGroup(CsTicketModel paramCsTicketModel, CsAgentGroupModel paramCsAgentGroupModel) throws TicketException;


    CsCustomerEventModel addNoteToTicket(CsTicketModel paramCsTicketModel, CsInterventionType paramCsInterventionType, CsEventReason paramCsEventReason, String paramString, Collection<MediaModel> paramCollection);


    CsCustomerEventModel addCustomerEmailToTicket(CsTicketModel paramCsTicketModel, CsEventReason paramCsEventReason, String paramString1, String paramString2, Collection<MediaModel> paramCollection);


    CsTicketResolutionEventModel resolveTicket(CsTicketModel paramCsTicketModel, CsInterventionType paramCsInterventionType, CsResolutionType paramCsResolutionType, String paramString, Collection<MediaModel> paramCollection) throws TicketException;


    CsTicketResolutionEventModel resolveTicket(CsTicketModel paramCsTicketModel, CsInterventionType paramCsInterventionType, CsResolutionType paramCsResolutionType, String paramString) throws TicketException;


    CsCustomerEventModel unResolveTicket(CsTicketModel paramCsTicketModel, CsInterventionType paramCsInterventionType, CsEventReason paramCsEventReason, String paramString, Collection<MediaModel> paramCollection) throws TicketException;


    CsCustomerEventModel unResolveTicket(CsTicketModel paramCsTicketModel, CsInterventionType paramCsInterventionType, CsEventReason paramCsEventReason, String paramString) throws TicketException;


    boolean isTicketClosed(CsTicketModel paramCsTicketModel);


    boolean isTicketResolvable(CsTicketModel paramCsTicketModel);


    List<CsTicketState> getTicketNextStates(CsTicketModel paramCsTicketModel);


    List<CsTicketState> getTicketNextStates(CsTicketState paramCsTicketState);


    CsTicketEventModel getLastEvent(CsTicketModel paramCsTicketModel);


    String renderTicketEventText(CsTicketEventModel paramCsTicketEventModel);
}
