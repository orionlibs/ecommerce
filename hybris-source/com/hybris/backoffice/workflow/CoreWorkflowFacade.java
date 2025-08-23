package com.hybris.backoffice.workflow;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.workflow.WorkflowStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.NotImplementedException;

public interface CoreWorkflowFacade
{
    List<WorkflowItemAttachmentModel> addItems(WorkflowModel paramWorkflowModel, List<? extends ItemModel> paramList);


    default void removeItems(WorkflowModel workflow, List<WorkflowItemAttachmentModel> itemsToRemove)
    {
        throw new NotImplementedException("This method was not implemented yet");
    }


    WorkflowTemplateModel getWorkflowTemplateForCode(String paramString);


    WorkflowTemplateModel getAdHocWorkflowTemplate();


    WorkflowModel createWorkflow(String paramString, WorkflowTemplateModel paramWorkflowTemplateModel, List<ItemModel> paramList, UserModel paramUserModel);


    boolean startWorkflow(WorkflowModel paramWorkflowModel);


    boolean canBeStarted(WorkflowModel paramWorkflowModel);


    boolean isAdHocTemplate(WorkflowTemplateModel paramWorkflowTemplateModel);


    boolean isCorrectAdHocAssignee(PrincipalModel paramPrincipalModel);


    WorkflowStatus getWorkflowStatus(WorkflowModel paramWorkflowModel);


    boolean terminateWorkflow(WorkflowModel paramWorkflowModel);


    List<WorkflowActionModel> getCurrentTasks(WorkflowModel paramWorkflowModel);


    int countDecisions(WorkflowModel paramWorkflowModel);


    Date getWorkflowStartTime(WorkflowModel paramWorkflowModel);
}
