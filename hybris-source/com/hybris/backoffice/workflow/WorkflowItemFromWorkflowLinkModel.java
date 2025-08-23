/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import de.hybris.platform.core.model.link.LinkModel;
import java.util.Collection;

class WorkflowItemFromWorkflowLinkModel extends WorkflowItem
{
    private static final String AND_LABEL = "AND";
    private final LinkModel link;


    public WorkflowItemFromWorkflowLinkModel(final LinkModel link)
    {
        super(String.valueOf(link.getPk()), Type.AND_LINK, false);
        this.link = link;
    }


    @Override
    public LinkModel getModel()
    {
        return link;
    }


    @Override
    public Node createNode()
    {
        return new Node.Builder() //
                        .withId(getAndId()) //
                        .withLabel(AND_LABEL) //
                        .withLevel(getLevel()) //
                        .withGroup(WorkflowItemModelFactory.PROPERTY_AND_CONNECTION) //
                        .build();
    }


    /**
     * @return the id which is used when node is created via {@link #createNode()}
     */
    public String getAndId()
    {
        return AND_LABEL + link.getTarget().getPk().toString();
    }


    @Override
    public Collection<String> getNeighborsIds()
    {
        return Lists.newArrayList(String.valueOf(link.getSource().getPk()));
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
