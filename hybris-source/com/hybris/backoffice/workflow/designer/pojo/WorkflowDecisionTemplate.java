/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.pojo;

import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import java.util.Objects;

/**
 * Represents Workflow Decision of Workflow Template
 */
public class WorkflowDecisionTemplate implements WorkflowDecision
{
    private final WorkflowDecisionTemplateModel workflowDecisionTemplateModel;


    public WorkflowDecisionTemplate(final WorkflowDecisionTemplateModel workflowDecisionTemplateModel)
    {
        this.workflowDecisionTemplateModel = workflowDecisionTemplateModel;
    }


    @Override
    public WorkflowDecisionTemplateModel getModel()
    {
        return workflowDecisionTemplateModel;
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
        final WorkflowDecisionTemplate that = (WorkflowDecisionTemplate)o;
        return Objects.equals(workflowDecisionTemplateModel, that.workflowDecisionTemplateModel);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(workflowDecisionTemplateModel);
    }
}
