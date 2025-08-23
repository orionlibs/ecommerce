package de.hybris.platform.platformbackoffice.widgets.workflow;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class CreateWorkflowWizardHandler implements FlowActionHandler
{
    private WorkflowService workflowService;
    private WorkflowProcessingService workflowProcessingService;
    private ModelService modelService;


    public void perform(CustomType customType, FlowActionHandlerAdapter adapter, Map<String, String> parameters)
    {
        Map<String, Object> currentContext = (Map<String, Object>)adapter.getWidgetInstanceManager().getModel().getValue("currentContext", Map.class);
        WorkflowTemplateModel workflowTemplate = (WorkflowTemplateModel)currentContext.get("workflowTemplate");
        UserModel owner = (UserModel)currentContext.get("owner");
        String workflowName = (String)currentContext.get("workflowName");
        String description = (String)currentContext.get("workflowDescription");
        List<WorkflowItemAttachmentModel> itemsToAdd = (List<WorkflowItemAttachmentModel>)currentContext.get("itemsToAdd");
        List<WorkflowActionTemplateModel> actions = (List<WorkflowActionTemplateModel>)currentContext.get("workflowActionTemplates");
        workflowTemplate.setActions(actions);
        WorkflowModel createdWorkflow = this.workflowService.createWorkflow(workflowTemplate, owner);
        createdWorkflow.setName(workflowName);
        createdWorkflow.setDescription(description);
        if(itemsToAdd != null)
        {
            itemsToAdd.forEach(i -> i.setWorkflow(createdWorkflow));
            this.modelService.saveAll(itemsToAdd);
        }
        this.modelService.save(createdWorkflow);
        this.workflowProcessingService.startWorkflow(createdWorkflow);
        currentContext.put("createdWorkflow", createdWorkflow);
        adapter.custom();
    }


    @Required
    public void setWorkflowService(WorkflowService workflowService)
    {
        this.workflowService = workflowService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setWorkflowProcessingService(WorkflowProcessingService workflowProcessingService)
    {
        this.workflowProcessingService = workflowProcessingService;
    }
}
