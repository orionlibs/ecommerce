/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.pojo;

import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Represents Workflow Action of Workflow Instance
 */
public class WorkflowActionInstance implements WorkflowAction
{
    private final WorkflowActionModel workflowActionModel;


    public WorkflowActionInstance(final WorkflowActionModel workflowActionModel)
    {
        this.workflowActionModel = workflowActionModel;
    }


    @Override
    public WorkflowActionModel getModel()
    {
        return workflowActionModel;
    }


    @Override
    public Collection<WorkflowDecision> getDecisions()
    {
        return CollectionUtils.emptyIfNull(workflowActionModel.getDecisions()).stream().map(WorkflowDecisionInstance::new)
                        .collect(Collectors.toList());
    }


    @Override
    public Collection<WorkflowDecision> getIncomingDecisions()
    {
        return CollectionUtils.emptyIfNull(workflowActionModel.getIncomingDecisions()).stream().map(WorkflowDecisionInstance::new)
                        .collect(Collectors.toList());
    }


    @Override
    public Collection<WorkflowLink> getIncomingLinks()
    {
        return CollectionUtils.emptyIfNull(workflowActionModel.getIncomingLinks()).stream().map(WorkflowLink::ofSavedModel)
                        .collect(Collectors.toList());
    }


    @Override
    public WorkflowActionType getActionType()
    {
        return workflowActionModel.getActionType();
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
        final WorkflowActionInstance that = (WorkflowActionInstance)o;
        return Objects.equals(workflowActionModel, that.workflowActionModel);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(workflowActionModel);
    }
}
