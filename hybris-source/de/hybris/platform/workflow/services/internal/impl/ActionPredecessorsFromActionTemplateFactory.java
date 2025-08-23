package de.hybris.platform.workflow.services.internal.impl;

import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.services.internal.WorkflowFactory;
import java.util.ArrayList;
import java.util.List;

public class ActionPredecessorsFromActionTemplateFactory extends AbstractWorkflowFactory implements WorkflowFactory<WorkflowModel, WorkflowActionTemplateModel, List<AbstractWorkflowActionModel>>
{
    public List<AbstractWorkflowActionModel> create(WorkflowModel root, WorkflowActionTemplateModel templateAction)
    {
        WorkflowActionModel action = getWorkAction(templateAction, root.getActions());
        List<AbstractWorkflowActionModel> predecessors = new ArrayList<>(action.getPredecessors());
        for(AbstractWorkflowActionModel predTemplateAction : templateAction.getPredecessors())
        {
            WorkflowActionModel workAction = getWorkAction((WorkflowActionTemplateModel)predTemplateAction, root
                            .getActions());
            predecessors.add(workAction);
        }
        action.setPredecessors(predecessors);
        this.modelService.save(action);
        return predecessors;
    }
}
