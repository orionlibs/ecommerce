package de.hybris.platform.ticket.events.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.ticket.jalo.CsTicket;
import java.util.Collection;
import java.util.Collections;
import org.apache.log4j.Logger;

public class CsTicketEvent extends GeneratedCsTicketEvent
{
    private static final Logger LOG = Logger.getLogger(CsTicketEvent.class.getName());


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item item = super.createItem(ctx, type, allAttributes);
        return item;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public CsTicket getTicket(SessionContext ctx)
    {
        Collection<Item> relatedItems = getRelatedItems(ctx);
        if(relatedItems != null)
        {
            if(relatedItems.size() > 1)
            {
                throw new IllegalStateException("A ticket event should only associated with a single ticket. Error occurred on event [" +
                                getPK() + "]");
            }
            if(relatedItems.size() == 1)
            {
                Item item = relatedItems.iterator().next();
                if(item instanceof CsTicket)
                {
                    return (CsTicket)item;
                }
                throw new IllegalStateException("A ticket event must be associated with a ticket. Error occurred on event [" +
                                getPK() + "] found related item [" + item + "]");
            }
        }
        return null;
    }


    public void setTicket(SessionContext ctx, CsTicket ticket)
    {
        if(ticket != null)
        {
            setRelatedItems(ctx, Collections.singleton(ticket));
        }
    }
}
