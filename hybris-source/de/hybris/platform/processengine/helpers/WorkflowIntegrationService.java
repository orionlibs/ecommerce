package de.hybris.platform.processengine.helpers;

import de.hybris.platform.processengine.definition.xml.UserGroupType;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.List;

public interface WorkflowIntegrationService
{
    WorkflowModel createWorkflow(WorkflowTemplateModel paramWorkflowTemplateModel, Object paramObject);


    void startWorkflow(WorkflowModel paramWorkflowModel);


    WorkflowTemplateModel getWorkflowTemplateModelById(String paramString);


    WorkflowTemplateModel createOrReadWorkflowTemplate(List<UserGroupType> paramList);
}
