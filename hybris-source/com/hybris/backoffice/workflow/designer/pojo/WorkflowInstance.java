/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.pojo;

import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents Workflow Instance
 */
public class WorkflowInstance implements Workflow
{
    private final WorkflowModel workflowModel;


    public WorkflowInstance(final WorkflowModel workflowModel)
    {
        this.workflowModel = workflowModel;
    }


    @Override
    public WorkflowModel getModel()
    {
        return workflowModel;
    }


    @Override
    public Collection<WorkflowAction> getActions()
    {
        return workflowModel.getActions().stream().map(WorkflowActionInstance::new).collect(Collectors.toList());
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
        final WorkflowInstance that = (WorkflowInstance)o;
        return Objects.equals(workflowModel, that.workflowModel);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(workflowModel);
    }
}
