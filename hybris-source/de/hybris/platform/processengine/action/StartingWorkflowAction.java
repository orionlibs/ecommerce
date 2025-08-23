package de.hybris.platform.processengine.action;

import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.processengine.helpers.WorkflowIntegrationService;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.workflow.WorkflowAttachmentService;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Required;

public class StartingWorkflowAction extends AbstractProceduralAction
{
    private UserService userService;
    private String templateId;
    private WorkflowIntegrationService workflowIntegrationService;
    private WorkflowAttachmentService workflowAttachmentService;


    public void executeAction(BusinessProcessModel process) throws RetryLaterException, Exception
    {
        EmployeeModel employeeModel = this.userService.getAdminUser();
        WorkflowTemplateModel template = this.workflowIntegrationService.getWorkflowTemplateModelById(this.templateId);
        WorkflowModel workflow = this.workflowIntegrationService.createWorkflow(template, employeeModel);
        this.workflowAttachmentService.addItems(workflow, Arrays.asList(new BusinessProcessModel[] {process}));
        this.modelService.save(workflow);
        beforeStart(workflow, process);
        this.workflowIntegrationService.startWorkflow(workflow);
    }


    protected void beforeStart(WorkflowModel workflow, BusinessProcessModel process)
    {
    }


    @Required
    public void setWorkflowAttachmentService(WorkflowAttachmentService workflowAttachmentService)
    {
        this.workflowAttachmentService = workflowAttachmentService;
    }


    @Required
    public void setTemplateId(String templateId)
    {
        this.templateId = templateId;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setWorkflowIntegrationService(WorkflowIntegrationService workflowIntegrationService)
    {
        this.workflowIntegrationService = workflowIntegrationService;
    }
}
