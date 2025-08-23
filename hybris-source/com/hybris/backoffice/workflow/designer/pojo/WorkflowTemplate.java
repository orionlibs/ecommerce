/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.pojo;

import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents Workflow Template
 */
public class WorkflowTemplate implements Workflow
{
    private final WorkflowTemplateModel workflowTemplateModel;


    public WorkflowTemplate(final WorkflowTemplateModel workflowTemplateModel)
    {
        this.workflowTemplateModel = workflowTemplateModel;
    }


    @Override
    public WorkflowTemplateModel getModel()
    {
        return workflowTemplateModel;
    }


    @Override
    public Collection<WorkflowAction> getActions()
    {
        return workflowTemplateModel.getActions().stream().map(WorkflowActionTemplate::new).collect(Collectors.toList());
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
        final WorkflowTemplate that = (WorkflowTemplate)o;
        return Objects.equals(workflowTemplateModel, that.workflowTemplateModel);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(workflowTemplateModel);
    }
}
