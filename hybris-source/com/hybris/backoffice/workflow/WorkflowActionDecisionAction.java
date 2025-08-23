/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow;

import com.hybris.cockpitng.actions.AbstractStatefulAction;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import org.springframework.beans.factory.annotation.Autowired;

public class WorkflowActionDecisionAction extends AbstractStatefulAction<WorkflowActionModel, WorkflowDecisionModel>
{
    protected static final String PARAMETER_SELECTED_DECISION = "selectedDecision";
    protected WorkflowDecisionMaker workflowDecisionMaker;


    @Override
    public ActionResult perform(final ActionContext<WorkflowActionModel> ctx)
    {
        final WorkflowDecisionModel selectedDecision = getValue(ctx, PARAMETER_SELECTED_DECISION);
        final WidgetInstanceManager widgetInstanceManager = (WidgetInstanceManager)ctx.getParameter("wim");
        if(!shouldPerform(widgetInstanceManager, selectedDecision))
        {
            return new ActionResult(ActionResult.ERROR, selectedDecision);
        }
        getWorkflowDecisionMaker().makeDecision(selectedDecision.getAction(), selectedDecision, widgetInstanceManager);
        return new ActionResult(ActionResult.SUCCESS, selectedDecision);
    }


    protected boolean shouldPerform(final WidgetInstanceManager widgetInstanceManager,
                    final WorkflowDecisionModel selectedDecision)
    {
        return widgetInstanceManager != null && selectedDecision != null && selectedDecision.getAction() != null
                        && selectedDecision.getAction().getStatus() == WorkflowActionStatus.IN_PROGRESS;
    }


    protected WorkflowDecisionMaker getWorkflowDecisionMaker()
    {
        return workflowDecisionMaker;
    }


    @Autowired
    public void setWorkflowDecisionMaker(final WorkflowDecisionMaker workflowDecisionMaker)
    {
        this.workflowDecisionMaker = workflowDecisionMaker;
    }
}
