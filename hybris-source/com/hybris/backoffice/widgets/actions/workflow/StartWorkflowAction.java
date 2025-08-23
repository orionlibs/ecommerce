/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.actions.workflow;

import com.hybris.backoffice.workflow.renderer.actionexecutors.WorkflowStartActionExecutor;
import com.hybris.backoffice.workflow.renderer.predicates.StartWorkflowActionPredicate;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import de.hybris.platform.workflow.model.WorkflowModel;
import javax.annotation.Resource;

public class StartWorkflowAction implements CockpitAction<WorkflowModel, WorkflowModel>
{
    @Resource
    private WorkflowStartActionExecutor workflowStartActionExecutor;
    @Resource
    private StartWorkflowActionPredicate startWorkflowActionPredicate;


    @Override
    public boolean canPerform(final ActionContext<WorkflowModel> ctx)
    {
        final WorkflowModel workflowModel = ctx.getData();
        if(workflowModel != null)
        {
            return startWorkflowActionPredicate.test(workflowModel);
        }
        return false;
    }


    @Override
    public ActionResult<WorkflowModel> perform(final ActionContext<WorkflowModel> context)
    {
        final WorkflowModel workflowModel = context.getData();
        if(workflowModel != null)
        {
            final boolean started = workflowStartActionExecutor.apply(workflowModel);
            if(started)
            {
                return new ActionResult<>(ActionResult.SUCCESS);
            }
        }
        return new ActionResult<>(ActionResult.ERROR);
    }
}
