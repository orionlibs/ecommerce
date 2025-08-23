package de.hybris.platform.workflow;

import de.hybris.platform.workflow.exceptions.ActivationWorkflowActionException;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowModel;

public interface WorkflowProcessingService
{
    boolean activate(WorkflowActionModel paramWorkflowActionModel) throws ActivationWorkflowActionException;


    void decideAction(WorkflowActionModel paramWorkflowActionModel, WorkflowDecisionModel paramWorkflowDecisionModel);


    boolean terminateWorkflow(WorkflowModel paramWorkflowModel);


    boolean endWorkflow(WorkflowModel paramWorkflowModel);


    boolean startWorkflow(WorkflowModel paramWorkflowModel);


    void setAndConnectionBetweenActionAndDecision(WorkflowDecisionModel paramWorkflowDecisionModel, WorkflowActionModel paramWorkflowActionModel);


    void setOrConnectionBetweenActionAndDecision(WorkflowDecisionModel paramWorkflowDecisionModel, WorkflowActionModel paramWorkflowActionModel);


    boolean toggleActions(WorkflowModel paramWorkflowModel);
}
