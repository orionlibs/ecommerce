/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.pojo;

import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import java.util.Objects;

/**
 * Represents Workflow Decision of Workflow Instance
 */
public class WorkflowDecisionInstance implements WorkflowDecision
{
    private final WorkflowDecisionModel workflowDecisionModel;


    public WorkflowDecisionInstance(final WorkflowDecisionModel workflowDecisionModel)
    {
        this.workflowDecisionModel = workflowDecisionModel;
    }


    @Override
    public WorkflowDecisionModel getModel()
    {
        return workflowDecisionModel;
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        final WorkflowDecisionInstance that = (WorkflowDecisionInstance)o;
        return Objects.equals(workflowDecisionModel, that.workflowDecisionModel);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(workflowDecisionModel);
    }
}
