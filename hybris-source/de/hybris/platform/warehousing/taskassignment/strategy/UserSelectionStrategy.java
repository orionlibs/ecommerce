package de.hybris.platform.warehousing.taskassignment.strategy;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.workflow.model.WorkflowModel;

public interface UserSelectionStrategy
{
    UserModel getUserForConsignmentAssignment(WorkflowModel paramWorkflowModel);
}
