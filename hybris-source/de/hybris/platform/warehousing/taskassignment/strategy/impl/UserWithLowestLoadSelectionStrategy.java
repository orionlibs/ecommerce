package de.hybris.platform.warehousing.taskassignment.strategy.impl;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.taskassignment.strategy.UserSelectionStrategy;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class UserWithLowestLoadSelectionStrategy implements UserSelectionStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(UserWithLowestLoadSelectionStrategy.class);
    private WorkflowService workflowService;


    public UserModel getUserForConsignmentAssignment(WorkflowModel workflow)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("workflow", workflow);
        UserModel result = null;
        Optional<WorkflowActionModel> inProgressAction = workflow.getActions().stream().filter(action -> action.getStatus().equals(WorkflowActionStatus.IN_PROGRESS)).findFirst();
        if(inProgressAction.isPresent())
        {
            WorkflowActionModel workflowAction = inProgressAction.get();
            if(workflowAction.getPrincipalAssigned() instanceof PrincipalGroupModel)
            {
                result = userWithLowestLoad(workflow, workflowAction);
            }
        }
        else
        {
            LOG.debug("Couldn't find a user to assign for workflow: {}", workflow.getCode());
        }
        return result;
    }


    protected UserModel userWithLowestLoad(WorkflowModel workflow, WorkflowActionModel workflowAction)
    {
        PrincipalGroupModel principalGroup = (PrincipalGroupModel)workflowAction.getPrincipalAssigned();
        Integer lowestLoad = null;
        UserModel result = null;
        for(PrincipalModel principal : getPosEmployees(workflow, principalGroup))
        {
            if(principal instanceof UserModel)
            {
                Integer assignedWorkflowActions = Integer.valueOf(
                                (int)getWorkflowService().getWorkflowsForTemplateAndUser(workflow.getJob(), (UserModel)principal).stream().filter(workflowModel -> (!CronJobStatus.ABORTED.equals(workflowModel.getStatus()) && !CronJobStatus.FINISHED.equals(workflowModel.getStatus()))).count());
                if(lowestLoad == null || lowestLoad.intValue() > assignedWorkflowActions.intValue())
                {
                    lowestLoad = assignedWorkflowActions;
                    result = (UserModel)principal;
                }
            }
        }
        return result;
    }


    protected Set<PrincipalModel> getPosEmployees(WorkflowModel workflow, PrincipalGroupModel principalGroup)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("workflow", workflow);
        ServicesUtil.validateParameterNotNullStandardMessage("principalGroup", principalGroup);
        Set<PrincipalModel> posEmployees = new HashSet<>();
        if(!workflow.getAttachments().isEmpty())
        {
            WarehouseModel warehouse = ((ConsignmentModel)((WorkflowItemAttachmentModel)workflow.getAttachments().iterator().next()).getItem()).getWarehouse();
            if(warehouse.getPointsOfService() != null)
            {
                warehouse.getPointsOfService()
                                .forEach(pos -> pos.getStoreEmployeeGroups().forEach(()));
                posEmployees.retainAll(principalGroup.getMembers());
            }
        }
        return posEmployees;
    }


    protected WorkflowService getWorkflowService()
    {
        return this.workflowService;
    }


    @Required
    public void setWorkflowService(WorkflowService workflowService)
    {
        this.workflowService = workflowService;
    }
}
