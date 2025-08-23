package de.hybris.platform.platformbackoffice.widgets.workflow;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.ArrayList;
import java.util.List;

public class CreateWorkflowForm
{
    private String workflowName;
    private String description;
    private WorkflowTemplateModel workflowTemplate;
    private List<WorkflowItemAttachmentModel> itemsToAdd;
    private UserModel owner;
    private final List<WorkflowActionTemplateModel> workflowActionTemplates = new ArrayList<>();
    private WorkflowModel createdWorkflow;


    public WorkflowTemplateModel getWorkflowTemplate()
    {
        return this.workflowTemplate;
    }


    public void setWorkflowTemplate(WorkflowTemplateModel workflowTemplate)
    {
        this.workflowTemplate = workflowTemplate;
    }


    public String getWorkflowName()
    {
        return this.workflowName;
    }


    public void setWorkflowName(String workflowName)
    {
        this.workflowName = workflowName;
    }


    public List<WorkflowItemAttachmentModel> getItemsToAdd()
    {
        return this.itemsToAdd;
    }


    public void setItemsToAdd(List<WorkflowItemAttachmentModel> itemsToAdd)
    {
        this.itemsToAdd = itemsToAdd;
    }


    public UserModel getOwner()
    {
        return this.owner;
    }


    public void setOwner(UserModel owner)
    {
        this.owner = owner;
    }


    public WorkflowModel getCreatedWorkflow()
    {
        return this.createdWorkflow;
    }


    public void setCreatedWorkflow(WorkflowModel createdWorkflow)
    {
        this.createdWorkflow = createdWorkflow;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public List<WorkflowActionTemplateModel> getWorkflowActionTemplates()
    {
        return this.workflowActionTemplates;
    }
}
