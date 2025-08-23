/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import java.util.Collection;

class WorkflowItemFromWorkflowDecisionTemplateModel extends WorkflowItem
{
    private final WorkflowDecisionTemplateModel decision;
    private final CockpitLocaleService localeService;


    public WorkflowItemFromWorkflowDecisionTemplateModel(final WorkflowDecisionTemplateModel decision,
                    final CockpitLocaleService localeService)
    {
        super(String.valueOf(decision.getPk()), Type.DECISION, false);
        this.decision = decision;
        this.localeService = localeService;
    }


    @Override
    public WorkflowDecisionTemplateModel getModel()
    {
        return decision;
    }


    @Override
    public Node createNode()
    {
        return new Node.Builder() //
                        .withId(getId()) //
                        .withLabel(decision.getName(localeService.getCurrentLocale())) //
                        .withLevel(getLevel()) //
                        .withGroup(Type.DECISION.toString().toLowerCase()) //
                        .build();
    }


    @Override
    public Collection<String> getNeighborsIds()
    {
        return Lists.newArrayList(String.valueOf(decision.getActionTemplate().getPk()));
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
