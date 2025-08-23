package de.hybris.platform.workflow.jalo;

import de.hybris.platform.cronjob.jalo.AbortCronJobException;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.security.AccessManager;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.localization.Localization;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class WorkflowTemplate extends GeneratedWorkflowTemplate
{
    private static final Logger LOG = Logger.getLogger(WorkflowTemplate.class.getName());


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(allAttributes.get("code") == null)
        {
            allAttributes.put("code", WorkflowManager.getInstance().getNextWorkflowNumber());
        }
        return super.createItem(ctx, type, allAttributes);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Workflow createWorkflow()
    {
        Map<String, Object> params = new HashMap<>();
        params.put(Workflow.OWNER, JaloSession.getCurrentSession().getUser());
        params.put("job", this);
        Workflow workflow = WorkflowManager.getInstance().createWorkflow(params);
        workflow.setAllName(getAllName());
        workflow.setAllDescription(getAllDescription());
        createActions(workflow);
        return workflow;
    }


    protected List<WorkflowAction> createActions(Workflow workflow)
    {
        List<WorkflowAction> workflowActions = new ArrayList<>();
        for(WorkflowActionTemplate action : getActions())
        {
            workflowActions.add(action.createWorkflowAction(workflow));
        }
        for(WorkflowActionTemplate templateAction : getActions())
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Name:" + templateAction.getName());
            }
            WorkflowAction action = getWorkAction(templateAction, workflowActions);
            for(AbstractWorkflowAction predTemplateAction : templateAction.getPredecessors())
            {
                WorkflowAction workAction = getWorkAction((WorkflowActionTemplate)predTemplateAction, workflowActions);
                action.addToPredecessors((AbstractWorkflowAction)workAction);
            }
            for(WorkflowDecisionTemplate workflowDecisionTemplate : templateAction.getDecisionTemplates())
            {
                Map<Object, Object> map = new HashMap<>();
                map.put("code", workflowDecisionTemplate.getCode());
                map.put("action", getWorkAction(workflowDecisionTemplate.getActionTemplate(), workflowActions));
                WorkflowDecision newDecision = WorkflowManager.getInstance().createWorkflowDecision(map);
                newDecision.setAllName(workflowDecisionTemplate.getAllName());
                newDecision.setAllDescription(workflowDecisionTemplate.getAllDescription());
                action.addToDecisions(newDecision);
                for(WorkflowActionTemplate actionTemplate : workflowDecisionTemplate.getToTemplateActions())
                {
                    newDecision.addToToActions(getWorkAction(actionTemplate, workflowActions));
                }
                copyAndConnectionAttribute(workflowDecisionTemplate, workflowActions, newDecision);
            }
        }
        return workflowActions;
    }


    public void copyAndConnectionAttribute(WorkflowDecisionTemplate workflowDecisionTemplate, List<WorkflowAction> workflowActions, WorkflowDecision newDecision)
    {
        WorkflowManager woman = WorkflowManager.getInstance();
        Collection<Link> linkTemplates = WorkflowManager.getInstance().getLinkTemplates((AbstractWorkflowDecision)workflowDecisionTemplate, null);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("linkTemplates.size()" + linkTemplates.size());
        }
        for(Link linkTemplate : linkTemplates)
        {
            WorkflowActionTemplate acttionTemplate = (WorkflowActionTemplate)linkTemplate.getTarget();
            WorkflowAction action = getWorkAction(acttionTemplate, workflowActions);
            Collection<Link> links = woman.getLinks(newDecision, (AbstractWorkflowAction)action);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("links.size():" + links.size());
            }
            try
            {
                ((Link)links.iterator().next()).setAttribute("andconnection", linkTemplate.getAttribute("andConnectionTemplate"));
            }
            catch(Exception e)
            {
                LOG.error(e.toString());
            }
        }
    }


    protected WorkflowAction getWorkAction(WorkflowActionTemplate templateAction, List<WorkflowAction> workflowActions)
    {
        for(WorkflowAction act : workflowActions)
        {
            if(act.getTemplate().equals(templateAction))
            {
                return act;
            }
        }
        return null;
    }


    protected CronJob.CronJobResult performCronJob(CronJob cronJob) throws AbortCronJobException
    {
        Workflow workflow = (Workflow)cronJob;
        workflow.startWorkflow();
        if(workflow.isCompleted())
        {
            return workflow.getFinishedResult(true);
        }
        return workflow.getPausedResult();
    }


    public void addToActions(SessionContext ctx, WorkflowActionTemplate value)
    {
        super.addToActions(ctx, value);
    }


    @ForceJALO(reason = "something else")
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        Map<String, Item> params = new HashMap<>();
        params.put("job", this);
        List<Workflow> result = FlexibleSearch.getInstance().search("SELECT {PK} FROM {Workflow} WHERE {job}=?job", params, Workflow.class).getResult();
        if(!result.isEmpty())
        {
            throw new ConsistencyCheckException(Localization.getLocalizedString("error.workflowtemplate.remove"), 0);
        }
        super.remove(ctx);
    }


    public void setOwner(Item item) throws ConsistencyCheckException
    {
        User user = (User)item;
        if(!TypeManager.getInstance().getComposedType(Workflow.class).checkPermission((Principal)user,
                        AccessManager.getInstance()
                                        .getOrCreateUserRightByCode("read")))
        {
            throw new JaloSystemException(Localization.getLocalizedString("error.workflow.owner.readaccess", new Object[] {user
                            .getUID()}));
        }
        if(user.isLoginDisabledAsPrimitive() || (user
                        .getDeactivationDate() != null && user.getDeactivationDate().toInstant().isBefore(Instant.now())))
        {
            throw new JaloSystemException(Localization.getLocalizedString("error.workflow.owner.hmcaccess", new Object[] {user
                            .getUID()}));
        }
        super.setOwner(item);
    }


    protected boolean isValidWorkflowTemplateActions()
    {
        for(WorkflowActionTemplate action : getActions())
        {
            if(Long.valueOf(action.getDecisionsCount()).equals(Long.valueOf(0L)) &&
                            !action.getActionType().equals(WorkflowAction.getEndActionType()))
            {
                return false;
            }
        }
        return true;
    }
}
