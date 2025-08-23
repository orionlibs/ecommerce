package de.hybris.platform.warehousing.taskassignment.services.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.process.WarehousingBusinessProcessService;
import de.hybris.platform.warehousing.taskassignment.services.WarehousingConsignmentWorkflowService;
import de.hybris.platform.warehousing.taskassignment.strategy.UserSelectionStrategy;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collections;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultWarehousingConsignmentWorkflowService implements WarehousingConsignmentWorkflowService
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultWarehousingConsignmentWorkflowService.class);
    protected static final String CONSIGNMENT_TEMPLATE_NAME = "warehousing.consignment.workflow.template";
    protected static final String WORKFLOW_OF_CONSIGNMENT = "consignmentworkflow_";
    protected static final String CONSIGNMENT_ACTION_EVENT_NAME = "ConsignmentActionEvent";
    private ModelService modelService;
    private UserSelectionStrategy userSelectionStrategy;
    private WorkflowService workflowService;
    private WorkflowTemplateService workflowTemplateService;
    private WorkflowProcessingService workflowProcessingService;
    private UserService userService;
    private ConfigurationService configurationService;
    private WarehousingBusinessProcessService<ConsignmentModel> consignmentBusinessProcessService;


    public void startConsignmentWorkflow(ConsignmentModel consignment)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("consignment", consignment);
        String consignmentWorkflowName = getConfigurationService().getConfiguration().getString("warehousing.consignment.workflow.template");
        try
        {
            WorkflowTemplateModel workflowTemplate = getWorkflowTemplateService().getWorkflowTemplateForCode(consignmentWorkflowName);
            if(workflowTemplate != null)
            {
                WorkflowModel workflow = getWorkflowService().createWorkflow("consignmentworkflow_" + consignment.getCode(), workflowTemplate,
                                Collections.singletonList(consignment), (UserModel)getUserService().getAdminUser());
                getModelService().save(workflow);
                consignment.setTaskAssignmentWorkflow(workflow.getCode());
                getModelService().save(consignment);
                workflow.getActions().forEach(action -> getModelService().save(action));
                getWorkflowProcessingService().startWorkflow(workflow);
                UserModel finalSelectedUser = getUserSelectionStrategy().getUserForConsignmentAssignment(workflow);
                workflow.setOwner((ItemModel)finalSelectedUser);
                workflow.getActions().forEach(action -> {
                    action.setPrincipalAssigned((PrincipalModel)finalSelectedUser);
                    getModelService().save(action);
                });
                getModelService().save(workflow);
                LOGGER.info("Employee: {} is assigned to consignment: {}.", finalSelectedUser.getDisplayName(), consignment
                                .getCode());
            }
            else
            {
                LOGGER.debug("No workflow template found, task assignment workflow will not be created but order will still be sourced.");
            }
        }
        catch(UnknownIdentifierException | IllegalArgumentException e)
        {
            LOGGER.debug("No WorkflowTemplate found for code: {} There will be no workflow assigned for this consignment", consignmentWorkflowName);
        }
        catch(ModelSavingException e)
        {
            LOGGER.debug("No PrincipalAssigned available for this order. A consignment will still be created.");
        }
    }


    public void terminateConsignmentWorkflow(ConsignmentModel consignment)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("consignment", consignment);
        if(consignment.getTaskAssignmentWorkflow() != null)
        {
            try
            {
                terminateWorkflow(getWorkflowService().getWorkflowForCode(consignment.getTaskAssignmentWorkflow()));
            }
            catch(UnknownIdentifierException e)
            {
                LOGGER.debug("No synchronization with a task assignment workflow because consignment {} has no workflow assigned to it.", consignment
                                .getCode());
            }
        }
    }


    public void decideWorkflowAction(ConsignmentModel consignment, String templateCode, String choice)
    {
        WorkflowActionModel taskAction = getWorkflowActionForTemplateCode(templateCode, consignment);
        if(taskAction != null)
        {
            taskAction.setPrincipalAssigned((PrincipalModel)getUserService().getCurrentUser());
            getModelService().save(taskAction);
            getWorkflowProcessingService().decideAction(taskAction, taskAction.getDecisions().iterator().next());
        }
        else if(!StringUtils.isEmpty(choice))
        {
            getConsignmentBusinessProcessService().triggerChoiceEvent((ItemModel)consignment, "ConsignmentActionEvent", choice);
        }
    }


    public WorkflowActionModel getWorkflowActionForTemplateCode(String templateCode, ConsignmentModel consignment)
    {
        WorkflowActionModel result = null;
        if(consignment.getTaskAssignmentWorkflow() != null)
        {
            WorkflowModel consignmentWorkflow = getWorkflowService().getWorkflowForCode(consignment.getTaskAssignmentWorkflow());
            Optional<WorkflowActionModel> workflowAction = consignmentWorkflow.getActions().stream().filter(action -> action.getTemplate().getCode().equals(templateCode)).findFirst();
            if(workflowAction.isPresent())
            {
                result = workflowAction.get();
            }
        }
        return result;
    }


    protected void terminateWorkflow(WorkflowModel taskAssignmentWorkflow)
    {
        if(taskAssignmentWorkflow != null)
        {
            getWorkflowProcessingService().terminateWorkflow(taskAssignmentWorkflow);
            taskAssignmentWorkflow.getActions().stream()
                            .filter(action -> !WorkflowActionStatus.TERMINATED.equals(action.getStatus())).forEach(action -> {
                                action.setStatus(WorkflowActionStatus.TERMINATED);
                                getModelService().save(action);
                            });
        }
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


    protected WorkflowTemplateService getWorkflowTemplateService()
    {
        return this.workflowTemplateService;
    }


    @Required
    public void setWorkflowTemplateService(WorkflowTemplateService workflowTemplateService)
    {
        this.workflowTemplateService = workflowTemplateService;
    }


    protected WorkflowProcessingService getWorkflowProcessingService()
    {
        return this.workflowProcessingService;
    }


    @Required
    public void setWorkflowProcessingService(WorkflowProcessingService workflowProcessingService)
    {
        this.workflowProcessingService = workflowProcessingService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected UserSelectionStrategy getUserSelectionStrategy()
    {
        return this.userSelectionStrategy;
    }


    @Required
    public void setUserSelectionStrategy(UserSelectionStrategy userSelectionStrategy)
    {
        this.userSelectionStrategy = userSelectionStrategy;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
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


    protected WarehousingBusinessProcessService<ConsignmentModel> getConsignmentBusinessProcessService()
    {
        return this.consignmentBusinessProcessService;
    }


    @Required
    public void setConsignmentBusinessProcessService(WarehousingBusinessProcessService<ConsignmentModel> consignmentBusinessProcessService)
    {
        this.consignmentBusinessProcessService = consignmentBusinessProcessService;
    }
}
