package de.hybris.platform.ticket.strategies;

import de.hybris.platform.ticket.events.model.CsTicketChangeEventEntryModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import java.util.Set;

public interface TicketAttributeChangeEventStrategy
{
    Set<CsTicketChangeEventEntryModel> getEntriesForChangedAttributes(CsTicketModel paramCsTicketModel);
}
