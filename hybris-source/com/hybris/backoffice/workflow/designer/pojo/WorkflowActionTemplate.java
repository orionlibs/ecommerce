/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.pojo;

import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Represents Workflow Action of Workflow Template
 */
public class WorkflowActionTemplate implements WorkflowAction
{
    private final WorkflowActionTemplateModel workflowActionTemplateModel;


    public WorkflowActionTemplate(final WorkflowActionTemplateModel workflowActionTemplateModel)
    {
        this.workflowActionTemplateModel = workflowActionTemplateModel;
    }


    @Override
    public WorkflowActionTemplateModel getModel()
    {
        return workflowActionTemplateModel;
    }


    @Override
    public Collection<WorkflowDecision> getDecisions()
    {
        return CollectionUtils.emptyIfNull(workflowActionTemplateModel.getDecisionTemplates()).stream()
                        .map(WorkflowDecisionTemplate::new).collect(Collectors.toList());
    }


    @Override
    public Collection<WorkflowDecision> getIncomingDecisions()
    {
        return CollectionUtils.emptyIfNull(workflowActionTemplateModel.getIncomingTemplateDecisions()).stream()
                        .map(WorkflowDecisionTemplate::new).collect(Collectors.toList());
    }


    @Override
    public Collection<WorkflowLink> getIncomingLinks()
    {
        return CollectionUtils.emptyIfNull(workflowActionTemplateModel.getIncomingLinkTemplates()).stream()
                        .map(WorkflowLink::ofSavedModel).collect(Collectors.toList());
    }


    @Override
    public WorkflowActionType getActionType()
    {
        return workflowActionTemplateModel.getActionType();
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
        final WorkflowActionTemplate that = (WorkflowActionTemplate)o;
        return Objects.equals(workflowActionTemplateModel, that.workflowActionTemplateModel);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(workflowActionTemplateModel);
    }
}
