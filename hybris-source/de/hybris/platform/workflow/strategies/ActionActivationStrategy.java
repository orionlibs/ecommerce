package de.hybris.platform.workflow.strategies;

import de.hybris.platform.workflow.exceptions.ActivationWorkflowActionException;
import de.hybris.platform.workflow.model.WorkflowActionModel;

public interface ActionActivationStrategy
{
    void doAfterActivation(WorkflowActionModel paramWorkflowActionModel) throws ActivationWorkflowActionException;
}
