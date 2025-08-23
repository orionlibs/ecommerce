package de.hybris.platform.workflow.daos;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.Collection;
import java.util.List;

public interface WorkflowActionDao
{
    List<WorkflowActionModel> findStartWorkflowActions(WorkflowModel paramWorkflowModel);


    List<WorkflowActionModel> findNormalWorkflowActions(WorkflowModel paramWorkflowModel);


    List<WorkflowActionModel> findEndWorkflowActions(WorkflowModel paramWorkflowModel);


    List<WorkflowActionModel> findWorkflowActionsByType(WorkflowActionType paramWorkflowActionType, WorkflowModel paramWorkflowModel);


    List<WorkflowActionModel> findWorkflowActionsByStatusAndAttachmentType(List<ComposedTypeModel> paramList, Collection<WorkflowActionStatus> paramCollection);
}
