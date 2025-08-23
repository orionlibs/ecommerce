package de.hybris.platform.ticket.jalo;

import de.hybris.platform.comments.constants.GeneratedCommentsConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.ticket.constants.GeneratedTicketsystemConstants;
import de.hybris.platform.ticket.events.jalo.CsTicketEvent;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

public class CsTicket extends GeneratedCsTicket
{
    private static final Logger LOG = Logger.getLogger(CsTicket.class.getName());


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item item = super.createItem(ctx, type, allAttributes);
        return item;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public List<CsTicketEvent> getEvents(SessionContext ctx)
    {
        String query = "SELECT {e:" + Item.PK + "}, {c2i:reverseSequenceNumber} FROM {" + GeneratedTicketsystemConstants.TC.CSTICKETEVENT + " AS e JOIN " + GeneratedCommentsConstants.Relations.COMMENTITEMRELATION + " AS c2i ON {c2i:source}={e:" + Item.PK
                        + "} }WHERE {c2i:target}=?ticket ORDER BY {c2i:reverseSequenceNumber} ASC";
        return JaloSession.getCurrentSession().getFlexibleSearch()
                        .search(query, Collections.singletonMap("ticket", this), CsTicketEvent.class).getResult();
    }
}
