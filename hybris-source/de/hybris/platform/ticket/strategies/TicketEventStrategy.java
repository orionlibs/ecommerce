package de.hybris.platform.ticket.strategies;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.ticket.enums.CsEventReason;
import de.hybris.platform.ticket.enums.CsInterventionType;
import de.hybris.platform.ticket.events.model.CsCustomerEventModel;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import java.util.Collection;

public interface TicketEventStrategy
{
    CsCustomerEventModel createNoteForTicket(CsTicketModel paramCsTicketModel, CsInterventionType paramCsInterventionType, CsEventReason paramCsEventReason, String paramString, Collection<MediaModel> paramCollection);


    CsCustomerEventModel createCustomerEmailForTicket(CsTicketModel paramCsTicketModel, CsEventReason paramCsEventReason, String paramString1, String paramString2, Collection<MediaModel> paramCollection);


    CsCustomerEventModel createCreationEventForTicket(CsTicketModel paramCsTicketModel, CsEventReason paramCsEventReason, CsInterventionType paramCsInterventionType, String paramString);


    CsCustomerEventModel ensureTicketEventSetupForCreationEvent(CsTicketModel paramCsTicketModel, CsCustomerEventModel paramCsCustomerEventModel);


    CsTicketEventModel createAssignAgentToTicket(CsTicketModel paramCsTicketModel);
}
