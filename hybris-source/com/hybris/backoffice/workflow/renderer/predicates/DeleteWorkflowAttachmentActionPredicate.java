/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.renderer.predicates;

import com.hybris.backoffice.workflow.WorkflowFacade;
import com.hybris.cockpitng.core.util.Validate;
import de.hybris.platform.workflow.WorkflowStatus;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Required;

public class DeleteWorkflowAttachmentActionPredicate implements Predicate<WorkflowItemAttachmentModel>
{
    private WorkflowFacade workflowFacade;


    @Override
    public boolean test(final WorkflowItemAttachmentModel workflowItemAttachmentModelModel)
    {
        Validate.notNull("Workflow item attachment model must not be null", workflowItemAttachmentModelModel);
        return WorkflowStatus.PLANNED.equals(workflowFacade.getWorkflowStatus(workflowItemAttachmentModelModel.getWorkflow()));
    }


    @Required
    public void setWorkflowFacade(final WorkflowFacade workflowFacade)
    {
        this.workflowFacade = workflowFacade;
    }
}
