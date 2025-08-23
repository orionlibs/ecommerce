package de.hybris.platform.workflow.impl;

import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.workflow.WorkflowActionService;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.constants.GeneratedWorkflowConstants;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.exceptions.ActivationWorkflowActionException;
import de.hybris.platform.workflow.exceptions.AutomatedWorkflowActionException;
import de.hybris.platform.workflow.exceptions.AutomatedWorkflowTemplateException;
import de.hybris.platform.workflow.exceptions.WorkflowActionDecideException;
import de.hybris.platform.workflow.jalo.Workflow;
import de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import de.hybris.platform.workflow.model.AbstractWorkflowDecisionModel;
import de.hybris.platform.workflow.model.AutomatedWorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowActionCommentModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.services.internal.AutomatedWorkflowTemplateRegistry;
import de.hybris.platform.workflow.strategies.ActionActivationStrategy;
import de.hybris.platform.workflow.strategies.DecideActionStrategy;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;

public class DefaultWorkflowProcessingService implements WorkflowProcessingService
{
    private static final Logger LOG = Logger.getLogger(DefaultWorkflowProcessingService.class);
    private ModelService modelService;
    private WorkflowActionService workflowActionService;
    private WorkflowService workflowService;
    private FlexibleSearchService flexibleSearchService;
    private ActionActivationStrategy actionActivationStrategy;
    private DecideActionStrategy decideActionStrategy;
    private volatile AutomatedWorkflowTemplateRegistry automatedWorkflowRegistry;


    public boolean activate(WorkflowActionModel action) throws ActivationWorkflowActionException
    {
        if(action.getActionType() == WorkflowActionType.END)
        {
            endWorkflow(action.getWorkflow());
            return true;
        }
        if(WorkflowActionStatus.DISABLED != action.getStatus() && action
                        .getStatus() != WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW)
        {
            action.setStatus(WorkflowActionStatus.IN_PROGRESS);
            action.setActivated(new Date());
            if(action.getFirstActivated() == null)
            {
                action.setFirstActivated(new Date());
            }
            if(action.getTemplate() instanceof AutomatedWorkflowActionTemplateModel)
            {
                performAutomatedWorkflow(action);
            }
            this.actionActivationStrategy.doAfterActivation(action);
            this.modelService.save(action);
            return false;
        }
        return true;
    }


    private void performAutomatedWorkflow(WorkflowActionModel action)
    {
        try
        {
            AutomatedWorkflowTemplateJob automatedWorkflow = getAutomatedWorkflowRegistry().getAutomatedWorkflowTemplateJobForTemplate((AutomatedWorkflowActionTemplateModel)action
                            .getTemplate());
            if(LOG.isDebugEnabled())
            {
                LOG.debug("calling perform on action object: " + automatedWorkflow);
            }
            writeAutomatedComment(action, "text.automatedworkflowactionTemplate.perform.start", new String[] {automatedWorkflow.getClass()
                            .getName()});
            WorkflowDecisionModel decision = automatedWorkflow.perform(action);
            writeAutomatedComment(action, "text.automatedworkflowactionTemplate.perform.end", new String[] {automatedWorkflow
                            .getClass().getName()});
            if(LOG.isDebugEnabled())
            {
                LOG.debug("finish perform on action object");
            }
            if(decision != null)
            {
                action.setSelectedDecision(decision);
                decide(action);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("decide for next workflow action");
                }
            }
        }
        catch(AutomatedWorkflowTemplateException e)
        {
            writeAutomatedComment(action, "text.automatedworkflowactionTemplate.perform.error", new String[] {e.getMessage()});
            throw new AutomatedWorkflowActionException(e);
        }
    }


    protected void writeAutomatedComment(WorkflowActionModel action, String message, String... messageParams)
    {
        List<WorkflowActionCommentModel> comments = new ArrayList<>(action.getWorkflowActionComments());
        WorkflowActionCommentModel comment = (WorkflowActionCommentModel)this.modelService.create(WorkflowActionCommentModel.class);
        comment.setComment(MessageFormat.format(Localization.getLocalizedString(message, (Object[])messageParams), (Object[])null));
        comment.setUser(null);
        comment.setWorkflowAction((AbstractWorkflowActionModel)action);
        this.modelService.save(comment);
        comments.add(comment);
        action.setWorkflowActionComments(comments);
        this.modelService.save(action);
    }


    public void decideAction(WorkflowActionModel action, WorkflowDecisionModel decision)
    {
        action.setSelectedDecision(decision);
        this.modelService.save(action);
        try
        {
            decide(action);
        }
        catch(WorkflowActionDecideException e)
        {
            throw new WorkflowActionDecideException(e);
        }
    }


    public void setAndConnectionBetweenActionAndDecision(WorkflowDecisionModel decision, WorkflowActionModel workflowAction)
    {
        setConnectionBetweenActionAndDecision(decision, workflowAction, true);
    }


    public void setOrConnectionBetweenActionAndDecision(WorkflowDecisionModel decision, WorkflowActionModel workflowAction)
    {
        setConnectionBetweenActionAndDecision(decision, workflowAction, false);
    }


    private void setConnectionBetweenActionAndDecision(WorkflowDecisionModel decision, WorkflowActionModel workflowAction, boolean isAndConnection)
    {
        Collection<LinkModel> incomingLinkList = getLinks((AbstractWorkflowDecisionModel)decision, (AbstractWorkflowActionModel)workflowAction);
        for(LinkModel link : incomingLinkList)
        {
            setAttributeForLink(link, "andconnection", Boolean.valueOf(isAndConnection));
        }
    }


    public boolean startWorkflow(WorkflowModel workflowModel)
    {
        if(this.modelService.isNew(workflowModel))
        {
            throw new IllegalArgumentException("Workflow model " + workflowModel + " has to be saved before start");
        }
        boolean startFlagFound = false;
        Collection<WorkflowActionModel> actions = workflowModel.getActions();
        for(WorkflowActionModel action : actions)
        {
            if(action.getActionType() == WorkflowActionType.START)
            {
                setPausedStatus(workflowModel);
                this.modelService.refresh(workflowModel);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("After: " + workflowModel.getStatus());
                }
                activate(action);
                startFlagFound = true;
            }
        }
        if(!startFlagFound)
        {
            for(WorkflowActionModel action : actions)
            {
                if(action.getPredecessors().size() > 0)
                {
                    setPausedStatus(workflowModel);
                    activate(workflowModel);
                    this.modelService.refresh(workflowModel);
                }
            }
        }
        return startFlagFound;
    }


    private void activate(WorkflowModel workflowModel)
    {
        Collection<WorkflowActionModel> activeActionsBefore = null;
        Collection<WorkflowActionModel> activeActionsAfter = null;
        do
        {
            activeActionsBefore = getActiveActions(workflowModel);
            for(WorkflowActionModel action : workflowModel.getActions())
            {
                tryActivate(action);
            }
            activeActionsAfter = getActiveActions(workflowModel);
        }
        while(activeActionsBefore.size() != activeActionsAfter.size() && !activeActionsBefore.containsAll(activeActionsAfter));
    }


    private void tryActivate(WorkflowActionModel action)
    {
        if(this.workflowActionService.isCompleted(action))
        {
            if(!predecessorsCompleted(action))
            {
                LOG.warn("Invalid state: Action " + action.getCode() + " is completed, but not all predecessors");
            }
        }
        else if(!this.workflowActionService.isActive(action))
        {
            if(predecessorsCompleted(action))
            {
                activate(action);
            }
            else
            {
                tryActivateSuccessors(action);
            }
        }
    }


    private void tryActivateSuccessors(WorkflowActionModel action)
    {
        for(AbstractWorkflowActionModel suc : action.getSuccessors())
        {
            tryActivate((WorkflowActionModel)suc);
        }
    }


    protected boolean predecessorsCompleted(WorkflowActionModel action)
    {
        for(AbstractWorkflowActionModel pred : action.getPredecessors())
        {
            if(!this.workflowActionService.isCompleted((WorkflowActionModel)pred))
            {
                return false;
            }
        }
        return true;
    }


    private Collection<WorkflowActionModel> getActiveActions(WorkflowModel workflowModel)
    {
        Collection<WorkflowActionModel> ret = new ArrayList<>();
        for(WorkflowActionModel action : workflowModel.getActions())
        {
            if(this.workflowActionService.isActive(action))
            {
                ret.add(action);
            }
        }
        return ret;
    }


    public boolean endWorkflow(WorkflowModel workflow)
    {
        boolean ended = false;
        Collection<WorkflowActionModel> actions = workflow.getActions();
        for(WorkflowActionModel workflowAction : actions)
        {
            if(workflowAction.getStatus() == WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW)
            {
                LOG.error("Status '" + WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW + "' was set before action " + workflowAction
                                .getName() + " was actually ended.");
                continue;
            }
            if(workflowAction.getStatus() == WorkflowActionStatus.PENDING || workflowAction
                            .getStatus() == WorkflowActionStatus.IN_PROGRESS)
            {
                workflowAction.setStatus(WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW);
                setFinishedStatus(workflow);
                ended = true;
            }
        }
        setEndTime(workflow, new Date());
        return ended;
    }


    public boolean terminateWorkflow(WorkflowModel workflowModel)
    {
        boolean terminated = false;
        Collection<WorkflowActionModel> actions = workflowModel.getActions();
        for(WorkflowActionModel workflowAction : actions)
        {
            if(WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW == workflowAction.getStatus())
            {
                LOG.error("Status '" + WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW + "' was set before action " + workflowAction
                                .getName() + " was actually ended.");
                continue;
            }
            if(workflowAction.getStatus() == WorkflowActionStatus.PENDING || workflowAction
                            .getStatus() == WorkflowActionStatus.IN_PROGRESS)
            {
                workflowAction.setStatus(WorkflowActionStatus.TERMINATED);
                setAbortedStatus(workflowModel);
                this.modelService.save(workflowAction);
                terminated = true;
            }
        }
        setEndTime(workflowModel, new Date());
        return terminated;
    }


    private void setFinishedStatus(WorkflowModel workflow)
    {
        Workflow _workflow = (Workflow)this.modelService.getSource(workflow);
        _workflow.setFinishedStatus();
    }


    private void setAbortedStatus(WorkflowModel workflow)
    {
        Workflow _workflow = (Workflow)this.modelService.getSource(workflow);
        _workflow.setAbortedStatus();
    }


    private void setEndTime(WorkflowModel workflow, Date date)
    {
        Workflow _workflow = (Workflow)this.modelService.getSource(workflow);
        _workflow.setEndTime(date);
    }


    public Collection<LinkModel> getLinks(AbstractWorkflowDecisionModel decision, AbstractWorkflowActionModel action)
    {
        Collection<LinkModel> results;
        Map<Object, Object> params = new HashMap<>();
        if(decision == null && action == null)
        {
            throw new IllegalArgumentException("Decision and action cannot both be null");
        }
        if(action == null)
        {
            params.put("desc", decision);
            SearchResult<LinkModel> res = this.flexibleSearchService.search("SELECT {pk} from {" + GeneratedWorkflowConstants.Relations.WORKFLOWACTIONLINKRELATION + "} where {source}=?desc", params);
            results = res.getResult();
        }
        else if(decision == null)
        {
            params.put("act", action);
            SearchResult<LinkModel> res = this.flexibleSearchService.search("SELECT {pk} from {" + GeneratedWorkflowConstants.Relations.WORKFLOWACTIONLINKRELATION + "} where {target}=?act", params);
            results = res.getResult();
        }
        else
        {
            params.put("desc", decision);
            params.put("act", action);
            SearchResult<LinkModel> res = this.flexibleSearchService.search("SELECT {pk} from {" + GeneratedWorkflowConstants.Relations.WORKFLOWACTIONLINKRELATION + "} where {source}=?desc AND {target}=?act", params);
            results = res.getResult();
            if(results.isEmpty())
            {
                LOG.error("There is no WorkflowActionLinkRelation for source '" + decision.getCode() + "' and target '" + action
                                .getCode() + "'");
            }
            else if(results.size() > 1)
            {
                LOG.error("There is more than one WorkflowActionLinkRelation for source '" + decision.getCode() + "' and target '" + action
                                .getCode() + "'");
            }
        }
        return results;
    }


    protected void chosen(WorkflowDecisionModel selDec)
    {
        Collection<LinkModel> links = getLinks((AbstractWorkflowDecisionModel)selDec, null);
        for(LinkModel link : links)
        {
            setAttributeForLink(link, "active", Boolean.TRUE);
        }
        Collection<WorkflowActionModel> actions = selDec.getToActions();
        for(WorkflowActionModel action : actions)
        {
            checkIncomingLinks(action);
        }
    }


    protected void checkIncomingLinks(WorkflowActionModel action)
    {
        boolean foundAnAndLink = false;
        boolean foundInactiveAndLink = false;
        boolean foundActiveOrLink = false;
        ArrayList<LinkModel> andLinks = new ArrayList<>();
        ArrayList<LinkModel> orLinks = new ArrayList<>();
        Collection<LinkModel> links = getLinks(null, (AbstractWorkflowActionModel)action);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("links.size():" + links.size());
        }
        for(LinkModel link : links)
        {
            Boolean andconnection = (Boolean)getAttributeForLink(link, "andconnection");
            Boolean active = (Boolean)getAttributeForLink(link, "active");
            if(active == null)
            {
                setAttributeForLink(link, "active", Boolean.FALSE);
                active = Boolean.FALSE;
            }
            if(andconnection == null)
            {
                setAttributeForLink(link, "andconnection", Boolean.FALSE);
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("linksource: " + ((WorkflowDecisionModel)link.getSource()).getName() + "  linktarget: " + ((WorkflowActionModel)link
                                .getTarget()).getName());
                LOG.debug("andconnection: " + andconnection);
                LOG.debug("active: " + active);
            }
            if(BooleanUtils.isTrue(andconnection))
            {
                foundAnAndLink = true;
                andLinks.add(link);
                if(!active.booleanValue() && (
                                !(link.getSource() instanceof WorkflowDecisionModel) ||
                                                !this.workflowActionService.isCompleted(((WorkflowDecisionModel)link.getSource()).getAction())))
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Active?: " + active);
                    }
                    foundInactiveAndLink = true;
                }
                continue;
            }
            if(BooleanUtils.isTrue(active))
            {
                orLinks.add(link);
                foundActiveOrLink = true;
            }
        }
        if(!foundInactiveAndLink && foundAnAndLink)
        {
            for(LinkModel link : andLinks)
            {
                setAttributeForLink(link, "active", Boolean.FALSE);
            }
            activate(action);
            this.decideActionStrategy.doAfterActivationOfAndLink(action);
        }
        else if(foundActiveOrLink)
        {
            for(LinkModel link : orLinks)
            {
                setAttributeForLink(link, "active", Boolean.FALSE);
            }
            activate(action);
            this.decideActionStrategy.doAfterActivationOfOrLink(action);
        }
    }


    protected void decide(WorkflowActionModel action)
    {
        if(this.workflowActionService.isCompleted(action))
        {
            throw new WorkflowActionDecideException("[02] Workflow action '" + action.getCode() + "' already completed");
        }
        if(this.workflowActionService.isUserAssignedPrincipal(action) || action
                        .getTemplate() instanceof AutomatedWorkflowActionTemplateModel)
        {
            this.workflowActionService.complete(action);
            WorkflowDecisionModel selDec = action.getSelectedDecision();
            chosen(selDec);
            this.decideActionStrategy.doAfterDecisionMade(action, selDec);
        }
        else
        {
            throw new WorkflowActionDecideException("[01] User is not an assigned principal for this workflow action '" + action
                            .getCode() + "'");
        }
        this.modelService.saveAll();
    }


    public boolean toggleActions(WorkflowModel workflow)
    {
        if(CronJobStatus.FINISHED != workflow.getStatus() && CronJobStatus.RUNNING != workflow.getStatus())
        {
            startWorkflow(workflow);
            if(this.workflowService.isCompleted(workflow))
            {
                return true;
            }
        }
        return false;
    }


    private void setAttributeForLink(LinkModel link, String attribute, Boolean value)
    {
        Link linkSource = (Link)this.modelService.getSource(link);
        try
        {
            linkSource.setAttribute(attribute, value);
        }
        catch(JaloInvalidParameterException e)
        {
            LOG.error("Jalo invalid parameter exception", (Throwable)e);
        }
        catch(JaloSecurityException e)
        {
            LOG.error("Jalo security attribute exception", (Throwable)e);
        }
        catch(JaloBusinessException e)
        {
            LOG.error("Jalo business attribute exception", (Throwable)e);
        }
    }


    private Object getAttributeForLink(LinkModel link, String attribute)
    {
        Link linkSource = (Link)this.modelService.getSource(link);
        try
        {
            return linkSource.getAttribute(attribute);
        }
        catch(JaloInvalidParameterException e)
        {
            LOG.error("Jalo invalid parameter exception", (Throwable)e);
        }
        catch(JaloSecurityException e)
        {
            LOG.error("Jalo security attribute exception", (Throwable)e);
        }
        return null;
    }


    private void setPausedStatus(WorkflowModel workflow)
    {
        Workflow _workflow = (Workflow)this.modelService.getSource(workflow);
        _workflow.setPausedStatus();
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    public void setWorkflowActionService(WorkflowActionService workflowActionService)
    {
        this.workflowActionService = workflowActionService;
    }


    public void setActionActivationStrategy(ActionActivationStrategy actionActivationStrategy)
    {
        this.actionActivationStrategy = actionActivationStrategy;
    }


    public void setDecideActionStrategy(DecideActionStrategy decideActionStrategy)
    {
        this.decideActionStrategy = decideActionStrategy;
    }


    public void setWorkflowService(WorkflowService workflowService)
    {
        this.workflowService = workflowService;
    }


    public AutomatedWorkflowTemplateRegistry getAutomatedWorkflowRegistry()
    {
        if(this.automatedWorkflowRegistry == null)
        {
            synchronized(this)
            {
                if(this.automatedWorkflowRegistry == null)
                {
                    this.automatedWorkflowRegistry = lookupAutomatedWorkflowRegistry();
                }
            }
        }
        return this.automatedWorkflowRegistry;
    }


    public AutomatedWorkflowTemplateRegistry lookupAutomatedWorkflowRegistry()
    {
        throw new UnsupportedOperationException("please override #lookupAutomatedWorkflowRegistry() or use <lookup-method>");
    }
}
