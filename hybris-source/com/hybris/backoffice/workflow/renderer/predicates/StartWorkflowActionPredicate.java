/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.renderer.predicates;

import com.hybris.backoffice.workflow.WorkflowFacade;
import com.hybris.cockpitng.core.util.Validate;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Required;

public class StartWorkflowActionPredicate implements Predicate<WorkflowModel>
{
    private WorkflowFacade workflowFacade;


    @Override
    public boolean test(final WorkflowModel workflowModel)
    {
        Validate.notNull("Workflow model must not be null", workflowModel);
        return workflowFacade.canBeStarted(workflowModel);
    }


    @Required
    public void setWorkflowFacade(final WorkflowFacade workflowFacade)
    {
        this.workflowFacade = workflowFacade;
    }
}
