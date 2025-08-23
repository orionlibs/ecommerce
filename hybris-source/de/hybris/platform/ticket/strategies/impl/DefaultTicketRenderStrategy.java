package de.hybris.platform.ticket.strategies.impl;

import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.commons.renderer.RendererService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.render.context.TicketEventRenderContext;
import de.hybris.platform.ticket.service.TicketException;
import de.hybris.platform.ticket.strategies.TicketRenderStrategy;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTicketRenderStrategy implements TicketRenderStrategy
{
    private static final Logger LOG = Logger.getLogger(DefaultTicketRenderStrategy.class);
    private String defaultTemplateCode = null;
    private RendererService rendererService;
    private FlexibleSearchService flexibleSearchService;
    private Map<String, String> eventType2TemplateCode = new HashMap<>();


    public String renderTicketEvent(CsTicketEventModel ticketEvent)
    {
        try
        {
            RendererTemplateModel renderTemplate = getTemplateForEvent(ticketEvent);
            StringWriter text = new StringWriter();
            this.rendererService.render(renderTemplate, new TicketEventRenderContext(ticketEvent), text);
            return text.toString();
        }
        catch(TicketException e)
        {
            LOG.error("could not find template for [" + ticketEvent + "]", (Throwable)e);
            return ticketEvent.getText();
        }
    }


    @Required
    public void setDefaultTemplateCode(String defaultTemplateCode)
    {
        this.defaultTemplateCode = defaultTemplateCode;
    }


    @Required
    public void setEventType2TemplateCode(Map<String, String> eventType2TemplateCode)
    {
        this.eventType2TemplateCode = eventType2TemplateCode;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Required
    public void setRendererService(RendererService rendererService)
    {
        this.rendererService = rendererService;
    }


    protected RendererTemplateModel getTemplateForEvent(CsTicketEventModel ticketEvent) throws TicketException
    {
        String template = this.eventType2TemplateCode.get(ticketEvent.getCommentType().getCode());
        if(template == null)
        {
            LOG.info("Did not find specific render template for type [" + ticketEvent.getCommentType().getCode() + "]");
            template = this.defaultTemplateCode;
        }
        SearchResult<RendererTemplateModel> result = this.flexibleSearchService.search("SELECT {pk} from {RendererTemplate} where {code} = ?code",
                        Collections.singletonMap("code", template));
        if(result.getCount() < 1)
        {
            throw new TicketException("Could not find render template [" + template + "]");
        }
        return result.getResult().get(0);
    }
}
