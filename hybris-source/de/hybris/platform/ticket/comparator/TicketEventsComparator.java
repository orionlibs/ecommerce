package de.hybris.platform.ticket.comparator;

import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import java.util.Comparator;

public class TicketEventsComparator implements Comparator<CsTicketEventModel>
{
    public int compare(CsTicketEventModel csTicketEventModel1, CsTicketEventModel csTicketEventModel2)
    {
        return csTicketEventModel2.getModifiedtime().compareTo(csTicketEventModel1.getModifiedtime());
    }
}
