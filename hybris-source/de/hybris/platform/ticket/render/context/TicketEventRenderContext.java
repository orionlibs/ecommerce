package de.hybris.platform.ticket.render.context;

import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import org.apache.velocity.VelocityContext;

public class TicketEventRenderContext extends VelocityContext
{
    private final CsTicketEventModel event;


    public TicketEventRenderContext(CsTicketEventModel event)
    {
        this.event = event;
    }


    public CsTicketEventModel getEvent()
    {
        return this.event;
    }


    public String getText()
    {
        return this.event.getText();
    }
}
