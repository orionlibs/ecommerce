package com.hybris.backoffice.workflow.impl;

import com.hybris.backoffice.workflow.CoreWorkflowFacade;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.workflow.WorkflowAttachmentService;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.WorkflowStatus;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCoreWorkflowFacade implements CoreWorkflowFacade
{
    private WorkflowAttachmentService workflowAttachmentService;
    private WorkflowTemplateService workflowTemplateService;
    private WorkflowService workflowService;
    private WorkflowProcessingService workflowProcessingService;


    public List<WorkflowItemAttachmentModel> addItems(WorkflowModel workflow, List<? extends ItemModel> itemsToAdd)
    {
        return getWorkflowAttachmentService().addItems(workflow, itemsToAdd);
    }


    public void removeItems(WorkflowModel workflow, List<WorkflowItemAttachmentModel> itemsToRemove)
    {
        this.workflowAttachmentService.removeItems(workflow, itemsToRemove);
    }


    public WorkflowTemplateModel getWorkflowTemplateForCode(String code)
    {
        return getWorkflowTemplateService().getWorkflowTemplateForCode(code);
    }


    public WorkflowTemplateModel getAdHocWorkflowTemplate()
    {
        return getWorkflowTemplateService().getAdhocWorkflowTemplate();
    }


    public WorkflowModel createWorkflow(String name, WorkflowTemplateModel template, List<ItemModel> itemsToAdd, UserModel owner)
    {
        return getWorkflowService().createWorkflow(name, template, itemsToAdd, owner);
    }


    public boolean startWorkflow(WorkflowModel workflow)
    {
        return getWorkflowProcessingService().startWorkflow(workflow);
    }


    public boolean canBeStarted(WorkflowModel workflow)
    {
        return (this.workflowService.isPlanned(workflow) && this.workflowService.canBeStarted(workflow) &&
                        CollectionUtils.isNotEmpty((Collection)workflow.getAttachments().stream().filter(e -> (e.getItem() != null)).collect(Collectors.toList())));
    }


    public boolean isAdHocTemplate(WorkflowTemplateModel template)
    {
        return Objects.equals(template, getWorkflowTemplateService().getAdhocWorkflowTemplate());
    }


    public boolean isCorrectAdHocAssignee(PrincipalModel adHocAssignedUser)
    {
        return (adHocAssignedUser != null &&
                        !Objects.equals(adHocAssignedUser, getWorkflowTemplateService().getAdhocWorkflowTemplateDummyOwner()));
    }


    public WorkflowStatus getWorkflowStatus(WorkflowModel workflowModel)
    {
        if(workflowModel.getActions().isEmpty())
        {
            return null;
        }
        if(getWorkflowService().isFinished(workflowModel))
        {
            return WorkflowStatus.FINISHED;
        }
        if(getWorkflowService().isTerminated(workflowModel))
        {
            return WorkflowStatus.TERMINATED;
        }
        if(getWorkflowService().isRunning(workflowModel) || getWorkflowService().isPaused(workflowModel))
        {
            return WorkflowStatus.RUNNING;
        }
        if(getWorkflowService().isPlanned(workflowModel))
        {
            return WorkflowStatus.PLANNED;
        }
        return null;
    }


    public boolean terminateWorkflow(WorkflowModel workflow)
    {
        return this.workflowProcessingService.terminateWorkflow(workflow);
    }


    public List<WorkflowActionModel> getCurrentTasks(WorkflowModel workflowModel)
    {
        return (List<WorkflowActionModel>)workflowModel.getActions().stream().filter(a -> WorkflowActionStatus.IN_PROGRESS.equals(a.getStatus()))
                        .collect(Collectors.toList());
    }


    public int countDecisions(WorkflowModel workflowModel)
    {
        return
                        (int)workflowModel.getActions().stream().filter(a -> (a.getDecisions() != null)).mapToLong(a -> a.getDecisions().size()).sum();
    }


    public Date getWorkflowStartTime(WorkflowModel workflow)
    {
        return this.workflowService.getStartTime(workflow);
    }


    public WorkflowAttachmentService getWorkflowAttachmentService()
    {
        return this.workflowAttachmentService;
    }


    @Required
    public void setWorkflowAttachmentService(WorkflowAttachmentService workflowAttachmentService)
    {
        this.workflowAttachmentService = workflowAttachmentService;
    }


    public WorkflowTemplateService getWorkflowTemplateService()
    {
        return this.workflowTemplateService;
    }


    @Required
    public void setWorkflowTemplateService(WorkflowTemplateService workflowTemplateService)
    {
        this.workflowTemplateService = workflowTemplateService;
    }


    public WorkflowService getWorkflowService()
    {
        return this.workflowService;
    }


    @Required
    public void setWorkflowService(WorkflowService workflowService)
    {
        this.workflowService = workflowService;
    }


    public WorkflowProcessingService getWorkflowProcessingService()
    {
        return this.workflowProcessingService;
    }


    @Required
    public void setWorkflowProcessingService(WorkflowProcessingService workflowProcessingService)
    {
        this.workflowProcessingService = workflowProcessingService;
    }
}
