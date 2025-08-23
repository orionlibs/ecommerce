package de.hybris.platform.workflow.jalo;

import de.hybris.platform.commons.jalo.CommonsManager;
import de.hybris.platform.commons.jalo.renderer.RendererTemplate;
import de.hybris.platform.core.Registry;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.util.mail.MailUtils;
import de.hybris.platform.workflow.constants.GeneratedWorkflowConstants;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class WorkflowAction extends GeneratedWorkflowAction
{
    private static final Logger LOG = Logger.getLogger(WorkflowAction.class.getName());


    @Deprecated(since = "ages", forRemoval = false)
    public static EnumerationValue getStartActionType()
    {
        return EnumerationManager.getInstance().getEnumerationValue(GeneratedWorkflowConstants.TC.WORKFLOWACTIONTYPE, GeneratedWorkflowConstants.Enumerations.WorkflowActionType.START);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static EnumerationValue getNormalActionType()
    {
        return EnumerationManager.getInstance().getEnumerationValue(GeneratedWorkflowConstants.TC.WORKFLOWACTIONTYPE, GeneratedWorkflowConstants.Enumerations.WorkflowActionType.NORMAL);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static EnumerationValue getEndActionType()
    {
        return EnumerationManager.getInstance().getEnumerationValue(GeneratedWorkflowConstants.TC.WORKFLOWACTIONTYPE, GeneratedWorkflowConstants.Enumerations.WorkflowActionType.END);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static EnumerationValue getActiveStatus()
    {
        return EnumerationManager.getInstance().getEnumerationValue(GeneratedWorkflowConstants.TC.WORKFLOWACTIONSTATUS, GeneratedWorkflowConstants.Enumerations.WorkflowActionStatus.IN_PROGRESS);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static EnumerationValue getIdleStatus()
    {
        return EnumerationManager.getInstance().getEnumerationValue(GeneratedWorkflowConstants.TC.WORKFLOWACTIONSTATUS, GeneratedWorkflowConstants.Enumerations.WorkflowActionStatus.PENDING);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static EnumerationValue getCompletedStatus()
    {
        return EnumerationManager.getInstance().getEnumerationValue(GeneratedWorkflowConstants.TC.WORKFLOWACTIONSTATUS, GeneratedWorkflowConstants.Enumerations.WorkflowActionStatus.COMPLETED);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static EnumerationValue getEndedByWorkflowStatus()
    {
        return EnumerationManager.getInstance().getEnumerationValue(GeneratedWorkflowConstants.TC.WORKFLOWACTIONSTATUS, GeneratedWorkflowConstants.Enumerations.WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static EnumerationValue getDisabledStatus()
    {
        return EnumerationManager.getInstance().getEnumerationValue(GeneratedWorkflowConstants.TC.WORKFLOWACTIONSTATUS, GeneratedWorkflowConstants.Enumerations.WorkflowActionStatus.DISABLED);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static EnumerationValue getTerminatedStatus()
    {
        return EnumerationManager.getInstance().getEnumerationValue(GeneratedWorkflowConstants.TC.WORKFLOWACTIONSTATUS, GeneratedWorkflowConstants.Enumerations.WorkflowActionStatus.TERMINATED);
    }


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Workflow workflow = (Workflow)allAttributes.get("workflow");
        if(workflow != null)
        {
            WorkflowTemplate template = (WorkflowTemplate)workflow.getJob();
            if(template == null)
            {
                throw new JaloBusinessException("Template of workflow " + workflow + " is null!!");
            }
            WorkflowActionTemplate actionTemplate = (WorkflowActionTemplate)allAttributes.get("template");
            if(actionTemplate != null && !template.getActions().contains(actionTemplate))
            {
                throw new JaloBusinessException("Defined action template " + actionTemplate + " is no allowed template for workflow " + workflow);
            }
        }
        return super.createItem(ctx, type, allAttributes);
    }


    protected void activate()
    {
        if(getActionType() == getEndActionType())
        {
            getWorkflow().endWorkflow();
        }
        else if(!isDisabled() && !isEndedByWorkflow())
        {
            setStatus(getActiveStatus());
            setActivated(new Date());
            if(getFirstActivated() == null)
            {
                setFirstActivated(new Date());
            }
            sendActivationEmail();
            getTemplate().perform(this);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void sendActivationEmail()
    {
        if(isSendEmailAsPrimitive())
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("trying to send activation mail...");
            }
            try
            {
                String toEmailAddress = getEmailAddress();
                if(toEmailAddress == null || toEmailAddress.length() == 0)
                {
                    LOG.warn("No email address set, can not send activation mail.");
                    return;
                }
                HtmlEmail htmlEmail = (HtmlEmail)MailUtils.getPreConfiguredEmail();
                htmlEmail.setCharset("utf-8");
                String[] emailAddresses = toEmailAddress.split(";");
                for(String toAddress : emailAddresses)
                {
                    MailUtils.validateEmailAddress(toAddress, "TO");
                    htmlEmail.addTo(toAddress);
                }
                htmlEmail.setSubject(Localization.getLocalizedString("message.workflowaction.activated.subject", (Object[])new String[] {getWorkflow().getCode() + "/" + getWorkflow().getCode(), getCode() + "/" + getCode()}));
                if(getRendererTemplate() == null)
                {
                    htmlEmail.setMsg(Localization.getLocalizedString("message.workflowaction.activated.mail", (Object[])new String[] {getWorkflow().getCode() + "/" + getWorkflow().getCode(), getCode() + "/" + getCode()}));
                }
                else
                {
                    WorkflowMailContext ctx = (WorkflowMailContext)Registry.getApplicationContext().getBean("workflow.mail.context");
                    ctx.setToEmailAddress(toEmailAddress);
                    ctx.setAssigneeName(getPrincipalAssigned().getDisplayName());
                    if(getAttachmentsCount() > 0L)
                    {
                        Item item = ((WorkflowItemAttachment)getAttachments().iterator().next()).getItem();
                        if(TypeManager.getInstance().getComposedType(Product.class).isAssignableFrom((Type)item.getComposedType()))
                        {
                            Product product = (Product)item;
                            String pName = product.getName();
                            ctx.setAttachmentName((pName == null) ? product.getPK().toString() : pName);
                            ctx.setAttachmentPK(product.getPK().toString());
                        }
                        else
                        {
                            ctx.setAttachmentName(item.getPK().toString());
                            ctx.setAttachmentPK(item.getPK().toString());
                        }
                    }
                    else
                    {
                        ctx.setAttachmentName("empty");
                        ctx.setAttachmentPK("empty");
                    }
                    StringWriter mailMessage = new StringWriter();
                    RendererTemplate template = getRendererTemplate();
                    if(template == null)
                    {
                        LOG.warn("Template 'checkProduct' not found, can not send activation mail.");
                        return;
                    }
                    if(getPrincipalAssigned() instanceof User && ((User)getPrincipalAssigned()).getSessionLanguage() != null)
                    {
                        JaloSession.getCurrentSession().createLocalSessionContext().setLanguage(((User)
                                        getPrincipalAssigned()).getSessionLanguage());
                        try
                        {
                            CommonsManager.getInstance().render(template, ctx, mailMessage);
                        }
                        finally
                        {
                            JaloSession.getCurrentSession().removeLocalSessionContext();
                        }
                    }
                    else
                    {
                        CommonsManager.getInstance().render(template, ctx, mailMessage);
                    }
                    htmlEmail.setHtmlMsg(mailMessage.toString());
                }
                htmlEmail.send();
            }
            catch(EmailException e)
            {
                LOG.warn("Problem while sending activation mail: " + e.getMessage(), (Throwable)e);
            }
        }
        else if(LOG.isDebugEnabled())
        {
            LOG.debug("email sending is disabled.");
        }
    }


    protected void idle()
    {
        setStatus(getIdleStatus());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isDisabled()
    {
        return getDisabledStatus().equals(getStatus());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isEndedByWorkflow()
    {
        return getEndedByWorkflowStatus().equals(getStatus());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isActive()
    {
        return getActiveStatus().equals(getStatus());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isCompleted()
    {
        return (getDisabledStatus().equals(getStatus()) || getCompletedStatus().equals(getStatus()));
    }


    public void disable()
    {
        setStatus(getDisabledStatus());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void tryActivate()
    {
        if(isCompleted())
        {
            if(!predecessorsCompleted())
            {
                LOG.warn("Invalid state: Action " + getCode() + " is completed, but not all predecessors");
            }
        }
        else if(!isActive())
        {
            if(predecessorsCompleted())
            {
                activate();
            }
            else
            {
                tryActivateSuccessors();
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected boolean predecessorsCompleted()
    {
        for(AbstractWorkflowAction pred : getPredecessors())
        {
            if(!((WorkflowAction)pred).isCompleted())
            {
                return false;
            }
        }
        return true;
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void tryActivateSuccessors()
    {
        for(AbstractWorkflowAction suc : getSuccessors())
        {
            ((WorkflowAction)suc).tryActivate();
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void tryActivatePredecessors()
    {
        for(AbstractWorkflowAction pred : getPredecessors())
        {
            ((WorkflowAction)pred).activate();
        }
    }


    public void complete()
    {
        setStatus(getCompletedStatus());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void reject()
    {
        idle();
        tryActivatePredecessors();
    }


    private void dirtyHack(Link link, String qualifier)
    {
        try
        {
            link.setAttribute(qualifier, Boolean.FALSE);
        }
        catch(JaloInvalidParameterException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e);
            }
        }
        catch(JaloSecurityException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e);
            }
        }
        catch(JaloBusinessException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e);
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void checkIncomingLinks() throws JaloInvalidParameterException, JaloBusinessException
    {
        boolean foundAnAndLink = false;
        boolean foundInactiveAndLink = false;
        boolean foundActiveOrLink = false;
        ArrayList<Link> andLinks = new ArrayList<>();
        ArrayList<Link> orLinks = new ArrayList<>();
        WorkflowManager woman = WorkflowManager.getInstance();
        Collection<Link> links = woman.getLinks(null, (AbstractWorkflowAction)this);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("links.size():" + links.size());
        }
        for(Link link : links)
        {
            if(link.getAttribute("active") == null)
            {
                dirtyHack(link, "active");
            }
            if(link.getAttribute("andconnection") == null)
            {
                dirtyHack(link, "andconnection");
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("linksource: " + ((WorkflowDecision)link.getSource()).getName() + "  linktarget: " + ((WorkflowAction)link
                                .getTarget()).getName());
                LOG.debug("andconnection: " + link.getAttribute("andconnection"));
                LOG.debug("active: " + link.getAttribute("active"));
            }
            if(((Boolean)link.getAttribute("andconnection")).booleanValue())
            {
                foundAnAndLink = true;
                andLinks.add(link);
                if(!((Boolean)link.getAttribute("active")).booleanValue() && (
                                !(link.getSource() instanceof WorkflowDecision) ||
                                                !((WorkflowDecision)link.getSource()).getAction().isCompleted()))
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Active ist null?: " + ((link.getAttribute("active") == null) ? 1 : 0));
                    }
                    Boolean active = (Boolean)link.getAttribute("active");
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Active?: " + active);
                    }
                    foundInactiveAndLink = true;
                }
                continue;
            }
            if(((Boolean)link.getAttribute("active")).booleanValue())
            {
                orLinks.add(link);
                foundActiveOrLink = true;
            }
        }
        if(!foundInactiveAndLink && foundAnAndLink)
        {
            for(Link link : andLinks)
            {
                link.setAttribute("active", Boolean.FALSE);
            }
            writeAutomatedComment(this, "text.automatedcomments.activatedthroughandlink");
            activate();
        }
        else if(foundActiveOrLink)
        {
            for(Link link : orLinks)
            {
                link.setAttribute("active", Boolean.FALSE);
            }
            writeAutomatedComment(this, "text.automatedcomments.activatedthroughorlink");
            activate();
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void decide()
    {
        if(isCompleted())
        {
            throw new WorkflowActionDecideException("[02] Workflow action '" + getCode() + "' already completed");
        }
        if(isUserAssignedPrincipal() || getTemplate() instanceof AutomatedWorkflowActionTemplate)
        {
            try
            {
                complete();
                WorkflowDecision selDec = getSelectedDecision();
                writeAutomatedComment(this, "text.automatedcomments.completed", new String[] {selDec
                                .getName()});
                selDec.chosen();
            }
            catch(JaloBusinessException e)
            {
                throw new JaloSystemException(e);
            }
        }
        else
        {
            throw new WorkflowActionDecideException("[01] User is not an assigned principal for this workflow action '" +
                            getCode() + "'");
        }
    }


    public WorkflowDecision getDecisionByName(String name)
    {
        Collection<WorkflowDecision> decisions = getDecisions();
        for(WorkflowDecision decision : decisions)
        {
            if(decision.getName().equals(name))
            {
                return decision;
            }
        }
        return null;
    }


    @Deprecated(since = "ages", forRemoval = false)
    @ForceJALO(reason = "abstract method implementation")
    public List<Link> getIncomingLinks(SessionContext ctx)
    {
        return (List<Link>)WorkflowManager.getInstance().getLinks(null, (AbstractWorkflowAction)this);
    }


    @ForceJALO(reason = "abstract method implementation")
    public String getIncomingLinksStr(SessionContext ctx)
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        List<Link> incomingLinkList = getIncomingLinks(ctx);
        for(Link link : incomingLinkList)
        {
            if(first)
            {
                first = false;
            }
            else
            {
                result.append(", ");
            }
            if(((WorkflowDecision)link.getSource()).getAction().getName() == null)
            {
                result.append(((WorkflowDecision)link.getSource()).getAction().getCode());
                continue;
            }
            result.append(((WorkflowDecision)link.getSource()).getAction().getName());
        }
        return result.toString();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public long getDecisionsCount()
    {
        String query = "SELECT COUNT({" + WorkflowDecision.PK + "}) FROM {" + GeneratedWorkflowConstants.TC.WORKFLOWDECISION + "} WHERE {action}=?action";
        try
        {
            List<Long> result = FlexibleSearch.getInstance().search(query, Collections.singletonMap("action", this), Long.class).getResult();
            return ((Long)result.iterator().next()).longValue();
        }
        catch(FlexibleSearchException e)
        {
            throw new JaloInternalException(e, "flexible search error for search query '" + query + "'", 0);
        }
    }


    @ForceJALO(reason = "abstract method implementation")
    public List<Item> getAttachmentItems(SessionContext ctx)
    {
        return FlexibleSearch.getInstance().search(ctx,
                                        "SELECT {a.item} FROM {" + GeneratedWorkflowConstants.Relations.WORKFLOWACTIONITEMATTACHMENTRELATION + " AS rel JOIN " + GeneratedWorkflowConstants.TC.WORKFLOWITEMATTACHMENT + " AS a ON {rel.target} = {a.pk} } WHERE {rel.source}=?me ORDER BY {rel.sequenceNumber} ASC ",
                                        Collections.singletonMap("me", this), Item.class)
                        .getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isUserAssignedPrincipal()
    {
        User currentUser = JaloSession.getCurrentSession().getUser();
        return (currentUser.isAdmin() || currentUser == getPrincipalAssigned() || currentUser.getAllGroups().contains(
                        getPrincipalAssigned()));
    }
}
