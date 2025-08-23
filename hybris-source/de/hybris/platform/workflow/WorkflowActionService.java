package de.hybris.platform.workflow;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.Collection;
import java.util.List;

public interface WorkflowActionService
{
    List<WorkflowActionModel> getNormalWorkflowActions(WorkflowModel paramWorkflowModel);


    List<WorkflowActionModel> getStartWorkflowActions(WorkflowModel paramWorkflowModel);


    List<WorkflowActionModel> getEndWorkflowActions(WorkflowModel paramWorkflowModel);


    List<WorkflowActionModel> getWorkflowActionsByType(WorkflowActionType paramWorkflowActionType, WorkflowModel paramWorkflowModel);


    List<WorkflowActionModel> getAllUserWorkflowActionsWithAttachment(String paramString);


    List<WorkflowActionModel> getAllUserWorkflowActionsWithAttachment(ComposedTypeModel paramComposedTypeModel);


    List<WorkflowActionModel> getAllUserWorkflowActionsWithAttachments(List<String> paramList);


    List<WorkflowActionModel> getAllUserWorkflowActionsWithAttachments(List<String> paramList, Collection<WorkflowActionStatus> paramCollection);


    WorkflowActionModel createWorkflowAction(WorkflowActionTemplateModel paramWorkflowActionTemplateModel, WorkflowModel paramWorkflowModel);


    boolean isUserAssignedPrincipal(WorkflowActionModel paramWorkflowActionModel);


    boolean isCompleted(WorkflowActionModel paramWorkflowActionModel);


    boolean isActive(WorkflowActionModel paramWorkflowActionModel);


    boolean isEndedByWorkflow(WorkflowActionModel paramWorkflowActionModel);


    boolean isDisabled(WorkflowActionModel paramWorkflowActionModel);


    WorkflowActionModel disable(WorkflowActionModel paramWorkflowActionModel);


    WorkflowActionModel complete(WorkflowActionModel paramWorkflowActionModel);


    WorkflowActionModel idle(WorkflowActionModel paramWorkflowActionModel);


    WorkflowActionModel getActionForCode(WorkflowModel paramWorkflowModel, String paramString);
}
