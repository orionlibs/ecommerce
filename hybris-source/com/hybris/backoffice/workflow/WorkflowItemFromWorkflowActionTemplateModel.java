/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow;

import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class WorkflowItemFromWorkflowActionTemplateModel extends WorkflowItem
{
    private final WorkflowActionTemplateModel action;
    private final CockpitLocaleService localeService;


    public WorkflowItemFromWorkflowActionTemplateModel(final WorkflowActionTemplateModel action,
                    final CockpitLocaleService localeService)
    {
        super(String.valueOf(action.getPk()), Type.ACTION, WorkflowActionType.END == action.getActionType());
        this.action = action;
        this.localeService = localeService;
    }


    private static List<String> collectPKs(final Collection<? extends AbstractItemModel> items)
    {
        return items.stream() //
                        .map(AbstractItemModel::getPk) //
                        .map(String::valueOf) //
                        .collect(Collectors.toList());
    }


    @Override
    public WorkflowActionTemplateModel getModel()
    {
        return action;
    }


    @Override
    public Node createNode()
    {
        return new Node.Builder() //
                        .withId(getId()) //
                        .withLabel(action.getName(localeService.getCurrentLocale())) //
                        .withLevel(getLevel()) //
                        .withGroup(Type.ACTION.toString().toLowerCase()) //
                        .build();
    }


    @Override
    public Collection<String> getNeighborsIds()
    {
        final List<String> andConnections = collectPKs(action.getIncomingLinkTemplates().stream() //
                        .filter(WorkflowItemModelFactory::isAndConnectionTemplate) //
                        .collect(Collectors.toList()));
        if(andConnections.isEmpty())
        {
            return collectPKs(action.getIncomingTemplateDecisions());
        }
        return andConnections;
    }


    @Override
    public boolean equals(final Object o)
    {
        return super.equals(o);
    }


    @Override
    public int hashCode()
    {
        return super.hashCode();
    }
}
