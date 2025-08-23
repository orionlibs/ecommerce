package de.hybris.platform.workflow.impl;

import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.commons.renderer.Renderer;
import de.hybris.platform.commons.renderer.RendererService;
import de.hybris.platform.commons.renderer.impl.VelocityTemplateRenderer;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.mail.MailUtils;
import de.hybris.platform.workflow.EmailService;
import de.hybris.platform.workflow.mail.WorkflowMailContext;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import java.io.StringWriter;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

public class DefaultEmailService implements EmailService
{
    private static final Logger LOG = Logger.getLogger(DefaultEmailService.class);
    private RendererService rendererService;
    private SessionService sessionService;
    private L10NService l10nService;
    private VelocityTemplateRenderer velocityTemplateRenderer;


    public HtmlEmail createActivationEmail(WorkflowActionModel action)
    {
        if(BooleanUtils.isFalse(action.getSendEmail()))
        {
            LOG.debug("email sending is disabled.");
            return null;
        }
        LOG.debug("trying to send activation mail...");
        try
        {
            String toEmailAddress = action.getEmailAddress();
            if(StringUtils.isBlank(toEmailAddress))
            {
                LOG.warn("No email address set, can not send activation mail.");
                return null;
            }
            HtmlEmail htmlEmail = (HtmlEmail)getPreconfiguredEmail();
            htmlEmail.setCharset("utf-8");
            String[] emailAddresses = toEmailAddress.split(";");
            for(String toAddress : emailAddresses)
            {
                validateEmailAddress(toAddress);
                htmlEmail.addTo(toAddress);
            }
            htmlEmail.setSubject(this.l10nService.getLocalizedString("message.workflowaction.activated.subject", (Object[])new String[] {action
                            .getWorkflow().getCode() + "/" + action.getWorkflow().getCode(),
                            action.getCode() + "/" + action.getCode()}));
            if(action.getRendererTemplate() == null)
            {
                htmlEmail
                                .setMsg(this.l10nService.getLocalizedString("message.workflowaction.activated.mail", (Object[])new String[] {action.getWorkflow().getCode() + "/" + action.getWorkflow().getCode(), action
                                                .getCode() + "/" + action.getCode()}));
            }
            else
            {
                WorkflowMailContext myWorkflowMailContext = createWorkflowMailContext();
                myWorkflowMailContext.setToEmailAddress(toEmailAddress);
                myWorkflowMailContext.setAssigneeName(action.getPrincipalAssigned().getDisplayName());
                if(action.getAttachmentItems().size() > 0)
                {
                    ItemModel item = ((WorkflowItemAttachmentModel)action.getAttachments().iterator().next()).getItem();
                    if(item instanceof ProductModel)
                    {
                        ProductModel product = (ProductModel)item;
                        String pName = product.getName();
                        myWorkflowMailContext.setAttachmentName((pName == null) ? product.getPk().toString() : pName);
                        myWorkflowMailContext.setAttachmentPK(product.getPk().toString());
                    }
                    else
                    {
                        myWorkflowMailContext.setAttachmentName(item.getPk().toString());
                        myWorkflowMailContext.setAttachmentPK(item.getPk().toString());
                    }
                }
                else
                {
                    myWorkflowMailContext.setAttachmentName("empty");
                    myWorkflowMailContext.setAttachmentPK("empty");
                }
                StringWriter mailMessage = new StringWriter();
                RendererTemplateModel template = action.getRendererTemplate();
                if(template == null)
                {
                    LOG.warn("Template 'checkProduct' not found, can not send activation mail.");
                    return null;
                }
                if(action.getPrincipalAssigned() instanceof UserModel && ((UserModel)action
                                .getPrincipalAssigned()).getSessionLanguage() != null)
                {
                    this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, template, myWorkflowMailContext, mailMessage), (UserModel)action
                                    .getPrincipalAssigned());
                }
                else
                {
                    this.rendererService.render((Renderer)getVelocityTemplateRenderer(), template, myWorkflowMailContext, mailMessage);
                }
                htmlEmail.setHtmlMsg(mailMessage.toString());
            }
            return htmlEmail;
        }
        catch(EmailException e)
        {
            LOG.warn("Problem while sending activation mail: " + e.getMessage(), (Throwable)e);
            return null;
        }
    }


    void validateEmailAddress(String toAddress) throws EmailException
    {
        MailUtils.validateEmailAddress(toAddress, "TO");
    }


    Email getPreconfiguredEmail() throws EmailException
    {
        return MailUtils.getPreConfiguredEmail();
    }


    public void setRendererService(RendererService rendererService)
    {
        this.rendererService = rendererService;
    }


    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public WorkflowMailContext createWorkflowMailContext()
    {
        throw new UnsupportedOperationException("Please override this method by using <lookup-method/> configuration in spring.");
    }


    public void setL10nService(L10NService l10nService)
    {
        this.l10nService = l10nService;
    }


    public VelocityTemplateRenderer getVelocityTemplateRenderer()
    {
        return this.velocityTemplateRenderer;
    }


    public void setVelocityTemplateRenderer(VelocityTemplateRenderer velocityTemplateRenderer)
    {
        this.velocityTemplateRenderer = velocityTemplateRenderer;
    }
}
