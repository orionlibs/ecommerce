package de.hybris.platform.ticket.strategies;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.ticket.enums.CsEventReason;
import de.hybris.platform.ticket.enums.CsInterventionType;
import de.hybris.platform.ticket.enums.CsResolutionType;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.events.model.CsCustomerEventModel;
import de.hybris.platform.ticket.events.model.CsTicketResolutionEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketException;
import java.util.Collection;
import java.util.List;

public interface TicketResolutionStrategy
{
    CsTicketResolutionEventModel resolveTicket(CsTicketModel paramCsTicketModel, CsInterventionType paramCsInterventionType, CsResolutionType paramCsResolutionType, String paramString) throws TicketException;


    CsTicketResolutionEventModel resolveTicket(CsTicketModel paramCsTicketModel, CsInterventionType paramCsInterventionType, CsResolutionType paramCsResolutionType, String paramString, Collection<MediaModel> paramCollection) throws TicketException;


    CsCustomerEventModel unResolveTicket(CsTicketModel paramCsTicketModel, CsInterventionType paramCsInterventionType, CsEventReason paramCsEventReason, String paramString) throws TicketException;


    CsCustomerEventModel unResolveTicket(CsTicketModel paramCsTicketModel, CsInterventionType paramCsInterventionType, CsEventReason paramCsEventReason, String paramString, Collection<MediaModel> paramCollection) throws TicketException;


    boolean isTicketClosed(CsTicketModel paramCsTicketModel);


    boolean isTicketResolvable(CsTicketModel paramCsTicketModel);


    List<CsTicketState> filterTicketStatesToRemovedClosedStates(List<CsTicketState> paramList);
}
