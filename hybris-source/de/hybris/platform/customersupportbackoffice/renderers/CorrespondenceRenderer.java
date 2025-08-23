package de.hybris.platform.customersupportbackoffice.renderers;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractPanel;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.widgets.editorarea.renderer.EditorAreaRendererUtils;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.AbstractEditorAreaPanelRenderer;
import de.hybris.platform.comments.model.CommentAttachmentModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.ticket.comparator.TicketEventsComparator;
import de.hybris.platform.ticket.enums.CsEventReason;
import de.hybris.platform.ticket.enums.CsInterventionType;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.events.model.CsCustomerEventModel;
import de.hybris.platform.ticket.events.model.CsTicketChangeEventEntryModel;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketAttachmentsService;
import de.hybris.platform.ticket.service.TicketBusinessService;
import de.hybris.platform.ticket.service.TicketService;
import de.hybris.platform.ticket.utils.AttachmentMediaUrlHelper;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Script;
import org.zkoss.zul.Span;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;

public class CorrespondenceRenderer extends AbstractEditorAreaPanelRenderer<CsTicketModel>
{
    private static final Logger LOG = Logger.getLogger(CorrespondenceRenderer.class);
    protected static final String CUSTOMER_SERVICE_STYLE_BOX = "yw-editorarea-editor-correspondence-customer-service";
    protected static final String CUSTOMER_STYLE_BOX = "yw-editorarea-editor-correspondence-customer";
    protected static final String SEND_BUTTON_STYLE = "yw-editorarea-correspondence-button-send y-btn-primary";
    protected static final String SEND_TEXTBOX_STYLE = "yw-editorarea-correspondence-textbox-send";
    protected static final String PRIVATE_MSG_TEXTBOX_STYLE = "ye-customersupport-textbox-private";
    protected static final String PUBLIC_MSG_TEXTBOX_STYLE = "ye-customersupport-textbox-public";
    protected static final String STATUS_MSG_TEXTBOX_STYLE = "ye-customersupport-textbox-status";
    protected static final String CUSTOMER_MSG_TEXTBOX_STYLE = "ye-customersupport-textbox";
    protected static final String MSG_TEXTBOX_STYLE_CLOSE = "ye-status-open-closed";
    protected static final String MSG_TEXTBOX_STYLE_REOPEN = "ye-status-closed-open";
    private static final String REFRESH_BUTTON_LISTENER_ID = "REFRESH_BUTTON_LISTENER_ID";
    private TicketService ticketService;
    private TicketBusinessService ticketBusinessService;
    private ModelService modelService;
    private CsTicketModel csTicketModel;
    private final Textbox replyTextbox = new Textbox();
    private TicketAttachmentsService ticketAttachmentsService;
    private WidgetInstanceManager widgetInstanceManager;
    private NotificationService notificationService;
    private final Set<MediaModel> attachments = new HashSet<>();
    private String allowedUploadedFormats;


    public void render(Component component, AbstractPanel abstractPanel, CsTicketModel csTicketModel, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        this.csTicketModel = csTicketModel;
        this.widgetInstanceManager = widgetInstanceManager;
        EditorAreaRendererUtils.setAfterCancelListener(widgetInstanceManager.getModel(), "REFRESH_BUTTON_LISTENER_ID", event -> widgetInstanceManager.getWidgetslot().updateView(), false);
        removeSaveButton(component);
        List<CsTicketEventModel> events = new ArrayList<>(this.ticketService.getEventsForTicket(csTicketModel));
        Collections.sort(events, (Comparator<? super CsTicketEventModel>)new TicketEventsComparator());
        addSendArea(component);
        Vlayout allMessageBox = new Vlayout();
        for(CsTicketEventModel csTicketEventModel : events)
        {
            Set<CsTicketChangeEventEntryModel> entries = csTicketEventModel.getEntries();
            if(CollectionUtils.isNotEmpty(entries))
            {
                allMessageBox.appendChild(createEntryChangeBox(csTicketEventModel));
            }
            if(StringUtils.isNotEmpty(csTicketEventModel.getText()))
            {
                allMessageBox.appendChild(createCommentBox(csTicketEventModel));
            }
        }
        component.appendChild((Component)allMessageBox);
    }


    protected Component createFooter(CsTicketEventModel ticket)
    {
        Hlayout footer = new Hlayout();
        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", getCockpitLocaleService().getCurrentLocale());
        TimeZone timeZone = getFormatTimeZone();
        if(timeZone != null)
        {
            format.setTimeZone(timeZone);
        }
        Label timeLabel = new Label(format.format(ticket.getCreationtime()));
        footer.appendChild((Component)timeLabel);
        if(ticket.getAuthor() != null && ticket.getAuthor() instanceof de.hybris.platform.core.model.user.EmployeeModel)
        {
            UserModel author = ticket.getAuthor();
            Label authorLabel = new Label(author.getName() + " [" + author.getName() + "]");
            footer.appendChild((Component)authorLabel);
            footer.setSclass("yw-editorarea-editor-correspondence-customer-service");
        }
        else
        {
            Label authorLabel = new Label(this.csTicketModel.getCustomer().getName());
            footer.appendChild((Component)authorLabel);
            footer.setSclass("yw-editorarea-editor-correspondence-customer");
        }
        return (Component)footer;
    }


    protected TimeZone getFormatTimeZone()
    {
        Session session = Sessions.getCurrent();
        if(session != null)
        {
            Object timeZone = session.getAttribute("org.zkoss.web.preferred.timeZone");
            if(timeZone instanceof TimeZone)
            {
                return (TimeZone)timeZone;
            }
        }
        return null;
    }


    protected Component createCommentBox(CsTicketEventModel csTicketEventModel)
    {
        Vlayout oneMessageBox = new Vlayout();
        if(StringUtils.isNotBlank(csTicketEventModel.getText()))
        {
            Label messageLabel = new Label(csTicketEventModel.getText());
            Div messageDiv = new Div();
            messageDiv.setSclass("z-textbox-message");
            if(csTicketEventModel instanceof CsCustomerEventModel)
            {
                CsInterventionType interventionType = ((CsCustomerEventModel)csTicketEventModel).getInterventionType();
                if(interventionType != null && interventionType.equals(CsInterventionType.PRIVATE))
                {
                    UITools.modifySClass((HtmlBasedComponent)oneMessageBox, "ye-customersupport-textbox-private", true);
                    messageLabel.setValue("" + interventionType + "\n\n" + interventionType);
                }
                else if(csTicketEventModel.getAuthor() != null && csTicketEventModel.getAuthor() instanceof de.hybris.platform.core.model.user.EmployeeModel)
                {
                    UITools.modifySClass((HtmlBasedComponent)oneMessageBox, "ye-customersupport-textbox-public", true);
                }
                else
                {
                    UITools.modifySClass((HtmlBasedComponent)oneMessageBox, "ye-customersupport-textbox", true);
                }
            }
            messageLabel.setMultiline(true);
            if(!csTicketEventModel.getEntries().isEmpty())
            {
                if(hasMatchingStatusEvent(csTicketEventModel.getEntries(), CsTicketState.CLOSED, CsTicketState.OPEN))
                {
                    UITools.modifySClass((HtmlBasedComponent)oneMessageBox, "ye-status-closed-open", true);
                }
                else if(hasMatchingStatusEvent(csTicketEventModel.getEntries(), CsTicketState.OPEN, CsTicketState.CLOSED))
                {
                    UITools.modifySClass((HtmlBasedComponent)oneMessageBox, "ye-status-open-closed", true);
                }
            }
            messageDiv.appendChild((Component)messageLabel);
            oneMessageBox.appendChild((Component)messageDiv);
        }
        if(!CollectionUtils.isEmpty(csTicketEventModel.getAttachments()))
        {
            Hlayout attachmentsList = new Hlayout();
            attachmentsList.setSclass("yw-editorarea-editor-correspondence-attachments");
            for(CommentAttachmentModel attachmentModel : csTicketEventModel.getAttachments())
            {
                Span attachmentSpan = new Span();
                attachmentSpan.setSclass("yw-editorarea-editor-correspondence-attachment");
                attachmentSpan.appendChild((Component)createAttachmentLink((MediaModel)attachmentModel.getItem()));
                attachmentsList.appendChild((Component)attachmentSpan);
            }
            oneMessageBox.appendChild((Component)attachmentsList);
        }
        oneMessageBox.appendChild(createFooter(csTicketEventModel));
        return (Component)oneMessageBox;
    }


    protected Component createEntryChangeBox(CsTicketEventModel csTicketEventModel)
    {
        Vlayout oneMessageBox = new Vlayout();
        StringBuilder text = new StringBuilder();
        for(CsTicketChangeEventEntryModel e : csTicketEventModel.getEntries())
        {
            if(e instanceof de.hybris.platform.ticket.events.model.CsTicketChangeEventCsTicketStateEntryModel)
            {
                text.append(e.getAlteredAttribute().getName() + ": " + e.getAlteredAttribute().getName() + " → " +
                                Labels.getLabel("customersupport_backoffice_tickets_inline_state_" + e.getOldStringValue().toLowerCase()) + "\n");
                continue;
            }
            text.append(e
                            .getAlteredAttribute().getName() + ": " + e.getAlteredAttribute().getName() + " → " + e.getOldStringValue() + "\n");
        }
        Label changesLabel = new Label(text.toString());
        Div changesDiv = new Div();
        UITools.modifySClass((HtmlBasedComponent)oneMessageBox, "ye-customersupport-textbox-status", true);
        changesDiv.appendChild((Component)changesLabel);
        oneMessageBox.appendChild((Component)changesDiv);
        oneMessageBox.appendChild(createFooter(csTicketEventModel));
        return (Component)oneMessageBox;
    }


    protected boolean hasMatchingStatusEvent(Set<CsTicketChangeEventEntryModel> ticketEventEntries, CsTicketState oldState, CsTicketState newState)
    {
        for(CsTicketChangeEventEntryModel e : ticketEventEntries)
        {
            if(e instanceof de.hybris.platform.ticket.events.model.CsTicketChangeEventCsTicketStateEntryModel && e
                            .getOldStringValue().equalsIgnoreCase(oldState.getCode()) && e
                            .getNewStringValue().equalsIgnoreCase(newState.getCode()))
            {
                return true;
            }
        }
        return false;
    }


    protected void removeSaveButton(Component component)
    {
        Script jQscript = new Script();
        jQscript.setContent(
                        "function hideShowNav() { if ($('.yw-editorarea-correspondence-textbox-send').length) { $('div.yw-editorarea-navi-container').hide(); $('div.ye-save-container').hide(); } else { $('div.ye-save-container').show();} };hideShowNav(); $('.yw-editor-area-main-content').bind('DOMSubtreeModified',function(){ hideShowNav() });");
        jQscript.setDefer(true);
        jQscript.setParent(component);
    }


    protected void addSendArea(Component parent)
    {
        Vlayout vlayout = new Vlayout();
        Hlayout attachmentsList = new Hlayout();
        attachmentsList.setSclass("yw-editorarea-editor-correspondence-attachments");
        Button sendButton = new Button(Labels.getLabel("customersupport_backoffice_tickets_correspondence_send"));
        this.replyTextbox.setSclass("yw-editorarea-correspondence-textbox-send");
        this.replyTextbox.setMultiline(true);
        sendButton.setSclass("yw-editorarea-correspondence-button-send y-btn-primary");
        Button attachButton = new Button();
        attachButton.setUpload("true,maxsize=10240");
        attachButton.setLabel(Labels.getLabel("customersupport_backoffice_tickets_correspondence_attach"));
        attachButton.addEventListener("onUpload", event -> {
            MediaModel mediaModel;
            try
            {
                mediaModel = createMediaModel(((UploadEvent)event).getMedia(), this.csTicketModel.getCustomer());
            }
            catch(IOException | de.hybris.platform.ticket.service.UnsupportedAttachmentException e)
            {
                LOG.error(e.getMessage(), e);
                getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(this.widgetInstanceManager), "General", NotificationEvent.Level.FAILURE, new Object[] {e});
                return;
            }
            Span attachmentSpan = new Span();
            attachmentSpan.setSclass("yw-editorarea-editor-correspondence-attachment");
            attachmentSpan.appendChild((Component)createAttachmentLink(mediaModel));
            A rm = new A("x");
            rm.setSclass("yw-editorarea-editor-correspondence-attachment-remove");
            getAttachments().add(mediaModel);
            rm.addEventListener("onClick", ());
            attachmentSpan.appendChild((Component)rm);
            attachmentsList.appendChild((Component)attachmentSpan);
        });
        Hlayout replyToHLayout = new Hlayout();
        Label contactTypeLabel = new Label(Labels.getLabel("customersupport_backoffice_tickets_correspondence.contacttype"));
        Combobox contactTypeCombo = configureContactTypeCombo();
        contactTypeCombo.setReadonly(true);
        replyToHLayout.appendChild((Component)contactTypeLabel);
        replyToHLayout.appendChild((Component)contactTypeCombo);
        Div replyToDiv = new Div();
        Label replyToCustomerLabel = new Label(Labels.getLabel("customersupport_backoffice_tickets_correspondence.replyto"));
        replyToDiv.appendChild((Component)replyToCustomerLabel);
        Radiogroup replyToTypeRadioGroup = configureReplyToRadioGroup();
        replyToDiv.appendChild((Component)replyToTypeRadioGroup);
        replyToHLayout.appendChild((Component)replyToDiv);
        replyToDiv.setSclass("yw-editorarea-editor-correspondence-replyto");
        vlayout.appendChild((Component)replyToHLayout);
        replyToTypeRadioGroup.addEventListener("onCheck",
                        addReplyToRadioEventListener(replyToTypeRadioGroup, contactTypeCombo));
        contactTypeCombo.addEventListener("onSelect", addReplyToComboEventListener(replyToTypeRadioGroup, contactTypeCombo));
        vlayout.appendChild((Component)this.replyTextbox);
        Hlayout buttonsHlayout = new Hlayout();
        buttonsHlayout.appendChild((Component)attachmentsList);
        buttonsHlayout.appendChild((Component)attachButton);
        buttonsHlayout.appendChild((Component)sendButton);
        vlayout.appendChild((Component)buttonsHlayout);
        vlayout.setParent(parent);
        vlayout.setSclass("yw-reply-to-customer-container");
        sendButton.addEventListener("onClick", handleButtonClick(contactTypeCombo));
    }


    protected EventListener handleButtonClick(Combobox contactTypeCombo)
    {
        return event -> {
            LOG.debug(this.replyTextbox.getValue());
            if(StringUtils.isBlank(this.replyTextbox.getValue()))
            {
                getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(this.widgetInstanceManager), "General", NotificationEvent.Level.FAILURE, new Object[] {Labels.getLabel("customersupport_backoffice_tickets_correspondence_empty_message")});
                return;
            }
            CsInterventionType contactType = (CsInterventionType)contactTypeCombo.getSelectedItem().getValue();
            CsCustomerEventModel csCustomerEventModel = this.ticketBusinessService.addNoteToTicket(this.csTicketModel, contactType, CsEventReason.UPDATE, this.replyTextbox.getValue(), getAttachments());
            this.replyTextbox.setValue("");
            this.widgetInstanceManager.getWidgetslot().updateView();
            if(csCustomerEventModel != null)
            {
                if(CsInterventionType.PRIVATE.equals(contactType))
                {
                    getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(this.widgetInstanceManager), "JustMessage", NotificationEvent.Level.SUCCESS, new Object[] {Labels.getLabel("customersupport_backoffice_tickets_correspondence_private_messge_success")});
                }
                else
                {
                    getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(this.widgetInstanceManager), "JustMessage", NotificationEvent.Level.SUCCESS,
                                    new Object[] {Labels.getLabel("customersupport_backoffice_tickets_correspondence_customer_message_success") + " " + Labels.getLabel("customersupport_backoffice_tickets_correspondence_customer_message_success")});
                }
            }
            else
            {
                getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(this.widgetInstanceManager), "JustMessage", NotificationEvent.Level.FAILURE, new Object[] {Labels.getLabel("customersupport_backoffice_tickets_correspondence_fail")});
            }
        };
    }


    protected void highlightReplyTextBox(CsInterventionType selectedInterventionType)
    {
        UITools.modifySClass((HtmlBasedComponent)this.replyTextbox, "ye-customersupport-textbox-private", CsInterventionType.PRIVATE.equals(selectedInterventionType));
    }


    protected EventListener addReplyToComboEventListener(Radiogroup replyToTypeRadioGroup, Combobox contactTypeCombo)
    {
        return (EventListener)new Object(this, contactTypeCombo, replyToTypeRadioGroup);
    }


    protected EventListener addReplyToRadioEventListener(Radiogroup replyToTypeRadioGroup, Combobox contactTypeCombo)
    {
        return (EventListener)new Object(this, replyToTypeRadioGroup, contactTypeCombo);
    }


    protected MediaModel createMediaModel(Media media, UserModel customer) throws IOException
    {
        byte[] byteData;
        if(media.isBinary())
        {
            if(media.inMemory())
            {
                byteData = media.getByteData();
            }
            else
            {
                byteData = IOUtils.toByteArray(media.getStreamData());
            }
        }
        else
        {
            byteData = media.getStringData().getBytes("UTF-8");
        }
        return getTicketAttachmentsService().createAttachment(media.getName(), media.getContentType(), byteData, customer);
    }


    protected A createAttachmentLink(MediaModel mediaModel)
    {
        A attachmentLink = new A(mediaModel.getRealFileName());
        attachmentLink.setHref(AttachmentMediaUrlHelper.urlHelper(mediaModel.getURL()));
        attachmentLink.setTarget("_new");
        return attachmentLink;
    }


    protected Combobox configureContactTypeCombo()
    {
        Combobox contactTypeCombo = new Combobox();
        List<CsInterventionType> interventionTypes = this.ticketService.getInterventionTypes();
        for(CsInterventionType csInterventionType : interventionTypes)
        {
            Comboitem comboItem = new Comboitem();
            comboItem.setLabel(getLabelService().getObjectLabel(csInterventionType));
            comboItem.setValue(csInterventionType);
            contactTypeCombo.appendChild((Component)comboItem);
            if(CsInterventionType.TICKETMESSAGE.equals(csInterventionType))
            {
                contactTypeCombo.setSelectedItem(comboItem);
            }
        }
        return contactTypeCombo;
    }


    protected Radiogroup configureReplyToRadioGroup()
    {
        Radiogroup replyToTypeRadioGroup = new Radiogroup();
        replyToTypeRadioGroup.appendItem(Labels.getLabel("customersupport_backoffice_tickets_correspondence_replyto_customer"), CsInterventionType.TICKETMESSAGE
                        .getCode());
        replyToTypeRadioGroup.appendItem(
                        Labels.getLabel("customersupport_backoffice_tickets_correspondence_replyto_customersupport"), CsInterventionType.PRIVATE
                                        .getCode());
        replyToTypeRadioGroup.setSelectedIndex(0);
        return replyToTypeRadioGroup;
    }


    protected TicketBusinessService getTicketBusinessService()
    {
        return this.ticketBusinessService;
    }


    @Required
    public void setTicketBusinessService(TicketBusinessService ticketBusinessService)
    {
        this.ticketBusinessService = ticketBusinessService;
    }


    protected TicketService getTicketService()
    {
        return this.ticketService;
    }


    @Required
    public void setTicketService(TicketService ticketService)
    {
        this.ticketService = ticketService;
    }


    protected Set<MediaModel> getAttachments()
    {
        return this.attachments;
    }


    protected NotificationService getNotificationService()
    {
        return this.notificationService;
    }


    @Required
    public void setNotificationService(NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected TicketAttachmentsService getTicketAttachmentsService()
    {
        return this.ticketAttachmentsService;
    }


    @Required
    public void setTicketAttachmentsService(TicketAttachmentsService ticketAttachmentsService)
    {
        this.ticketAttachmentsService = ticketAttachmentsService;
    }


    protected String getAllowedUploadedFormats()
    {
        return this.allowedUploadedFormats;
    }


    @Required
    public void setAllowedUploadedFormats(String allowedUploadedFormats)
    {
        this.allowedUploadedFormats = allowedUploadedFormats;
    }
}
