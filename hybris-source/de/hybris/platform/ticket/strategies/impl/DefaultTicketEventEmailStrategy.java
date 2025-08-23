package de.hybris.platform.ticket.strategies.impl;

import com.google.common.collect.Maps;
import de.hybris.platform.comments.model.CommentAttachmentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.commons.renderer.RendererService;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.ticket.email.context.AbstractTicketContext;
import de.hybris.platform.ticket.enums.CsEmailRecipients;
import de.hybris.platform.ticket.enums.CsInterventionType;
import de.hybris.platform.ticket.events.model.CsTicketChangeEventEntryModel;
import de.hybris.platform.ticket.events.model.CsTicketEmailModel;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.events.model.CsTicketResolutionEventModel;
import de.hybris.platform.ticket.model.CsTicketEventEmailConfigurationModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.strategies.TicketEventEmailStrategy;
import de.hybris.platform.util.mail.MailUtils;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTicketEventEmailStrategy implements TicketEventEmailStrategy
{
    private static final String NO_EMAIL_EVENTS_FOUND = "No email events found for type [%s]";
    private static final Logger LOG = Logger.getLogger(DefaultTicketEventEmailStrategy.class);
    private FlexibleSearchService flexibleSearch;
    private MediaService mediaService;
    private RendererService rendererService;
    private ModelService modelService;
    private Map<CsEmailRecipients, String> recipientTypeToContextClassMap = new EnumMap<>(CsEmailRecipients.class);


    public void sendEmailsForEvent(CsTicketModel ticket, CsTicketEventModel event)
    {
        CsEmailRecipients recepientType = null;
        if(event instanceof CsTicketResolutionEventModel && CsInterventionType.PRIVATE
                        .equals(((CsTicketResolutionEventModel)event).getInterventionType()))
        {
            recepientType = CsEmailRecipients.ASSIGNEDAGENT;
        }
        List<CsTicketEventEmailConfigurationModel> filteredConfigurations = getApplicableConfigs(event, recepientType);
        if(filteredConfigurations.isEmpty())
        {
            LOG.info(String.format("No email events found for type [%s]", new Object[] {getTicketEventCommentTypeString(event)}));
            return;
        }
        String originalText = event.getText();
        for(CsTicketEventEmailConfigurationModel config : filteredConfigurations)
        {
            AbstractTicketContext ticketContext = createContextForEvent(config, ticket, event);
            if(ticketContext != null)
            {
                CsTicketEmailModel email = constructAndSendEmail(ticketContext, config);
                if(email != null)
                {
                    List<CsTicketEmailModel> emails = new ArrayList<>();
                    emails.addAll(event.getEmails());
                    emails.add(email);
                    event.setEmails(emails);
                }
            }
            event.setText(originalText);
        }
        getModelService().save(event);
    }


    public void sendEmailsForAssignAgentTicketEvent(CsTicketModel ticket, CsTicketEventModel event, CsEmailRecipients recepientType)
    {
        List<CsTicketEventEmailConfigurationModel> filteredConfigurations = getApplicableConfigs(event, recepientType);
        if(filteredConfigurations.isEmpty())
        {
            LOG.info(String.format("No email events found for type [%s]", new Object[] {getTicketEventCommentTypeString(event)}));
            return;
        }
        for(CsTicketEventEmailConfigurationModel config : filteredConfigurations)
        {
            AbstractTicketContext ticketContext = createContextForEvent(config, ticket, event);
            if(ticketContext != null)
            {
                CsTicketEmailModel email = constructAndSendEmail(ticketContext, config);
                if(email != null)
                {
                    List<CsTicketEmailModel> emails = new ArrayList<>();
                    emails.addAll(event.getEmails());
                    emails.add(email);
                    event.setEmails(emails);
                }
            }
        }
        getModelService().save(event);
    }


    @Required
    public void setFlexibleSearch(FlexibleSearchService flexibleSearch)
    {
        this.flexibleSearch = flexibleSearch;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    @Required
    public void setRecipientTypeToContextClassMap(Map<CsEmailRecipients, String> recipientTypeToContextClassMap)
    {
        this.recipientTypeToContextClassMap = recipientTypeToContextClassMap;
    }


    @Required
    public void setRendererService(RendererService rendererService)
    {
        this.rendererService = rendererService;
    }


    protected CsTicketEmailModel constructAndSendEmail(AbstractTicketContext ticketContext, CsTicketEventEmailConfigurationModel config)
    {
        try
        {
            if(ticketContext.getTo() == null)
            {
                LOG.warn("Could not send email for event [" + ticketContext.getEvent() + "]. With config [" + config + "] No recipient could be found.");
                return null;
            }
            HtmlEmail email = (HtmlEmail)getPreConfiguredEmail();
            setMailEncoding(email, "UTF-8");
            VelocityContext ctx = new VelocityContext();
            ctx.put("ctx", ticketContext);
            StringWriter subj = new StringWriter();
            Velocity.evaluate((Context)ctx, subj, "logtag", new StringReader(config.getSubject()));
            email.setSubject(subj.toString());
            email.addTo(ticketContext.getTo());
            StringWriter htmlVersion = new StringWriter();
            this.rendererService.render(config.getHtmlTemplate(), ticketContext, htmlVersion);
            email.setHtmlMsg(htmlVersion.toString());
            StringWriter textVersion = new StringWriter();
            if(config.getPlainTextTemplate() != null)
            {
                this.rendererService.render(config.getPlainTextTemplate(), ticketContext, textVersion);
                email.setTextMsg(textVersion.toString());
            }
            Collection<CommentAttachmentModel> attachments = ticketContext.getAttachments();
            attachMediaToMail(email, attachments);
            CsTicketEmailModel storedEmail = (CsTicketEmailModel)getModelService().create(CsTicketEmailModel.class);
            storedEmail.setTo(ticketContext.getTo());
            storedEmail.setFrom(email.getFromAddress().toString());
            storedEmail.setSubject(email.getSubject());
            storedEmail.setBody(textVersion.toString() + textVersion.toString() + System.getProperty("line.separator"));
            String messageID = email.send();
            storedEmail.setMessageId(messageID);
            return storedEmail;
        }
        catch(EmailException e)
        {
            LOG.error("Error sending email to [" + config.getRecipientType() + "]. Context was [" + ticketContext + "]", (Throwable)e);
            return null;
        }
    }


    private void setMailEncoding(HtmlEmail email, String encoding)
    {
        try
        {
            email.setCharset(encoding);
        }
        catch(IllegalArgumentException iae)
        {
            LOG.error(String.format("Setting charset to '%s' failed.", new Object[] {encoding}), iae);
        }
    }


    private void attachMediaToMail(HtmlEmail email, Collection<CommentAttachmentModel> attachments) throws EmailException
    {
        if(attachments == null || attachments.isEmpty())
        {
            return;
        }
        for(CommentAttachmentModel attachment : attachments)
        {
            if(!(attachment.getItem() instanceof MediaModel))
            {
                continue;
            }
            try
            {
                MediaModel mediaAttachment = (MediaModel)attachment.getItem();
                ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(this.mediaService.getStreamFromMedia(mediaAttachment), mediaAttachment.getMime());
                email.attach((DataSource)byteArrayDataSource, mediaAttachment.getRealFileName(), mediaAttachment.getDescription());
            }
            catch(IOException ex)
            {
                LOG.error("Failed to load attachment data into data source [" + attachment + "]", ex);
            }
        }
    }


    protected AbstractTicketContext createContextForEvent(CsTicketEventEmailConfigurationModel config, CsTicketModel ticket, CsTicketEventModel event)
    {
        String contextClassName = this.recipientTypeToContextClassMap.get(config.getRecipientType());
        try
        {
            Class<?> contextClass = Class.forName(contextClassName);
            Constructor<?> constructor = contextClass.getConstructor(new Class[] {CsTicketModel.class, CsTicketEventModel.class});
            AbstractTicketContext ticketContext = (AbstractTicketContext)constructor.newInstance(new Object[] {ticket, event});
            StringBuilder text = new StringBuilder();
            for(CsTicketChangeEventEntryModel e : event.getEntries())
            {
                text.append(e.getAlteredAttribute().getName() + ": " + e.getAlteredAttribute().getName() + " -> " + e.getOldStringValue() + "\n");
            }
            text.append("\n").append(event.getText());
            event.setText(text.toString());
            return ticketContext;
        }
        catch(ClassNotFoundException | java.lang.reflect.InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e)
        {
            LOG.error("Error finding context class for email target [" + config.getRecipientType() + "]. Context class was [" + contextClassName + "]", e);
            return null;
        }
    }


    protected List<CsTicketEventEmailConfigurationModel> getApplicableConfigs(CsTicketEventModel event, CsEmailRecipients recepientType)
    {
        SearchResult<CsTicketEventEmailConfigurationModel> result = null;
        List<CsTicketEventEmailConfigurationModel> configurations = null;
        if(recepientType != null)
        {
            Map<String, Object> parameters = Maps.newHashMap();
            parameters.put("eventType", event.getCommentType());
            parameters.put("recipientType", recepientType);
            result = this.flexibleSearch.search("SELECT {pk} FROM {CsTicketEventEmailConfiguration} WHERE {eventType} = ?eventType AND {recipientType} = ?recipientType", parameters);
        }
        else
        {
            result = this.flexibleSearch.search("SELECT {pk} FROM {CsTicketEventEmailConfiguration} WHERE {eventType} = ?eventType",
                            Collections.singletonMap("eventType", event.getCommentType()));
        }
        configurations = result.getResult();
        if(configurations.isEmpty())
        {
            LOG.info(String.format("No email events found for type [%s]", new Object[] {getTicketEventCommentTypeString(event)}));
            return Collections.emptyList();
        }
        List<CsTicketEventEmailConfigurationModel> filteredConfigurations = null;
        filteredConfigurations = new ArrayList<>();
        List<AttributeDescriptorModel> attributes = new ArrayList<>();
        for(CsTicketChangeEventEntryModel entry : event.getEntries())
        {
            attributes.add(entry.getAlteredAttribute());
        }
        for(CsTicketEventEmailConfigurationModel config : configurations)
        {
            if(!config.getAlteredAttributes().isEmpty())
            {
                if(!CollectionUtils.intersection(attributes, config.getAlteredAttributes()).isEmpty())
                {
                    filteredConfigurations.add(config);
                    continue;
                }
                LOG.debug("configuration [" + config + "] was filtered out as none of the changed attributes met its required attributes");
                continue;
            }
            filteredConfigurations.add(config);
        }
        return filteredConfigurations;
    }


    protected Email getPreConfiguredEmail() throws EmailException
    {
        return MailUtils.getPreConfiguredEmail();
    }


    protected String getTicketEventCommentTypeString(CsTicketEventModel event)
    {
        if(event == null)
        {
            return "CsTicketEvent is NULL";
        }
        CommentTypeModel commentType = event.getCommentType();
        if(commentType == null)
        {
            return "CommentType is NULL for [" + event + "]";
        }
        return commentType.getCode();
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
