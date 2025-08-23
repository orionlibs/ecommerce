package de.hybris.platform.workflow;

import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.List;

public interface WorkflowTemplateService
{
    List<WorkflowTemplateModel> getAllWorkflowTemplates();


    List<WorkflowTemplateModel> getAllVisibleWorkflowTemplatesForUser(UserModel paramUserModel);


    WorkflowTemplateModel getAdhocWorkflowTemplate();


    EmployeeModel getAdhocWorkflowTemplateDummyOwner();


    WorkflowActionTemplateModel getWorkflowActionTemplateForCode(String paramString);


    WorkflowTemplateModel getWorkflowTemplateForCode(String paramString);


    void setAndConnectionBetweenActionAndDecision(WorkflowDecisionTemplateModel paramWorkflowDecisionTemplateModel, WorkflowActionTemplateModel paramWorkflowActionTemplateModel);


    void setOrConnectionBetweenActionAndDecision(WorkflowDecisionTemplateModel paramWorkflowDecisionTemplateModel, WorkflowActionTemplateModel paramWorkflowActionTemplateModel);
}
