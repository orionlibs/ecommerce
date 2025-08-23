package de.hybris.platform.ticket.email.context;

import de.hybris.platform.comments.model.CommentAttachmentModel;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import java.util.Collection;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.velocity.VelocityContext;

public abstract class AbstractTicketContext extends VelocityContext
{
    private final CsTicketModel ticket;
    private final CsTicketEventModel event;


    public AbstractTicketContext(CsTicketModel ticket, CsTicketEventModel event)
    {
        this.ticket = ticket;
        this.event = event;
    }


    public abstract String getName();


    public abstract String getTo();


    public String getTicketId()
    {
        return this.ticket.getTicketID();
    }


    public String getText()
    {
        return this.event.getText();
    }


    public String getHtmlText()
    {
        String escapedValue = escapeHtml(this.event.getText());
        String paragraphs = buildParagraphs(escapedValue);
        return paragraphs;
    }


    public CsTicketModel getTicket()
    {
        return this.ticket;
    }


    public CsTicketEventModel getEvent()
    {
        return this.event;
    }


    public String getSubject()
    {
        return this.event.getSubject();
    }


    public Collection<CommentAttachmentModel> getAttachments()
    {
        return this.event.getAttachments();
    }


    protected String escapeHtml(String text)
    {
        return StringEscapeUtils.escapeHtml(text);
    }


    protected String buildParagraphs(String text)
    {
        return "<p>" + text.replaceAll("(\\n\\r?)+", "</p><p>") + "</p>";
    }
}
