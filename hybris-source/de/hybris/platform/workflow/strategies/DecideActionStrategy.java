package de.hybris.platform.workflow.strategies;

import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;

public interface DecideActionStrategy
{
    void doAfterActivationOfAndLink(WorkflowActionModel paramWorkflowActionModel);


    void doAfterActivationOfOrLink(WorkflowActionModel paramWorkflowActionModel);


    void doAfterDecisionMade(WorkflowActionModel paramWorkflowActionModel, WorkflowDecisionModel paramWorkflowDecisionModel);
}
