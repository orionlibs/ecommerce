package de.hybris.platform.workflow.services.internal.impl;

import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.services.internal.WorkflowFactory;
import java.util.ArrayList;
import java.util.List;

public class DecisionFromDecisionTemplateFactory extends AbstractWorkflowFactory implements WorkflowFactory<WorkflowModel, WorkflowDecisionTemplateModel, WorkflowDecisionModel>
{
    public WorkflowDecisionModel create(WorkflowModel root, WorkflowDecisionTemplateModel workflowDecisionTemplate)
    {
        WorkflowDecisionModel newDecision = (WorkflowDecisionModel)this.modelService.create(WorkflowDecisionModel.class);
        List<WorkflowActionModel> toActions = new ArrayList<>();
        for(WorkflowActionTemplateModel actionTemplate : workflowDecisionTemplate.getToTemplateActions())
        {
            toActions.add(getWorkAction(actionTemplate, root.getActions()));
        }
        newDecision.setToActions(toActions);
        return newDecision;
    }
}
