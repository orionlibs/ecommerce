package de.hybris.platform.workflow.jalo;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.security.AccessManager;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.workflow.constants.GeneratedWorkflowConstants;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class Workflow extends GeneratedWorkflow
{
    private static final Logger LOG = Logger.getLogger(Workflow.class.getName());


    @Deprecated(since = "ages", forRemoval = false)
    public void toggleActions()
    {
        if(!isFinished() && !isRunning())
        {
            getJob().perform((CronJob)this, true);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void startWorkflow()
    {
        boolean startFlagFound = false;
        Collection<WorkflowAction> actions = getActions();
        for(WorkflowAction action : actions)
        {
            if(action.getActionType().equals(WorkflowAction.getStartActionType()))
            {
                setStatus(getPausedStatus());
                action.activate();
                startFlagFound = true;
            }
        }
        if(!startFlagFound)
        {
            for(WorkflowAction action : actions)
            {
                if(action.getPredecessors().size() > 0)
                {
                    setStatus(getPausedStatus());
                    activate();
                }
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void terminateWorkflow()
    {
        Collection<WorkflowAction> actions = getActions();
        for(WorkflowAction workflowAction : actions)
        {
            if(workflowAction.getStatus().equals(WorkflowAction.getEndedByWorkflowStatus()))
            {
                LOG.error("Status '" + WorkflowAction.getEndedByWorkflowStatus() + "' was set before action " + workflowAction
                                .getName() + " was actually ended.");
                continue;
            }
            if(workflowAction.getStatus().equals(WorkflowAction.getIdleStatus()) || workflowAction
                            .getStatus().equals(WorkflowAction.getActiveStatus()))
            {
                workflowAction.setStatus(WorkflowAction.getTerminatedStatus());
                setStatus(getAbortedStatus());
            }
        }
        setEndTime(new Date());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void endWorkflow()
    {
        Collection<WorkflowAction> actions = getActions();
        for(WorkflowAction workflowAction : actions)
        {
            if(workflowAction.getStatus().equals(WorkflowAction.getEndedByWorkflowStatus()))
            {
                LOG.error("Status '" + WorkflowAction.getEndedByWorkflowStatus() + "' was set before action " + workflowAction
                                .getName() + " was actually ended.");
                continue;
            }
            if(workflowAction.getStatus().equals(WorkflowAction.getIdleStatus()) || workflowAction
                            .getStatus().equals(WorkflowAction.getActiveStatus()))
            {
                workflowAction.setStatus(WorkflowAction.getEndedByWorkflowStatus());
                setStatus(getFinishedStatus());
            }
        }
        setEndTime(new Date());
    }


    @ForceJALO(reason = "something else")
    protected void setAborted()
    {
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void activate()
    {
        Collection<WorkflowAction> activeActionsBefore = null;
        Collection<WorkflowAction> activeActionsAfter = null;
        do
        {
            activeActionsBefore = getActiveActions();
            for(WorkflowAction action : getActions())
            {
                action.tryActivate();
            }
            activeActionsAfter = getActiveActions();
        }
        while(activeActionsBefore.size() != activeActionsAfter.size() && !activeActionsBefore.containsAll(activeActionsAfter));
    }


    private Collection<WorkflowAction> getActiveActions()
    {
        Collection<WorkflowAction> ret = new ArrayList<>();
        for(WorkflowAction action : getActions())
        {
            if(action.isActive())
            {
                ret.add(action);
            }
        }
        return ret;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isCompleted()
    {
        for(WorkflowAction action : getActions())
        {
            if(!action.isCompleted())
            {
                return false;
            }
        }
        return true;
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


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isPlanned()
    {
        String pending = GeneratedWorkflowConstants.Enumerations.WorkflowActionStatus.PENDING;
        for(WorkflowAction action : getActions())
        {
            if(!pending.equals(action.getStatus().getCode()))
            {
                return false;
            }
        }
        return true;
    }


    public void setPausedStatus()
    {
        setStatus(getPausedStatus());
    }


    public void setFinishedStatus()
    {
        setStatus(getFinishedStatus());
    }


    public void setAbortedStatus()
    {
        setStatus(getAbortedStatus());
    }
}
