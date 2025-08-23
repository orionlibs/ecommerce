/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.navigation.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hybris.backoffice.actionbar.ActionDefinition;
import com.hybris.backoffice.actionbar.DefaultActionDefinition;
import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.cockpitng.core.context.CockpitContext;
import com.hybris.cockpitng.core.context.impl.DefaultCockpitContext;
import com.hybris.cockpitng.widgets.common.explorertree.data.ActionAwareNode;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a navigation node with name, description and icon. It can hold a data object as well.
 */
public class SimpleNode extends DefaultActionDefinition implements NavigationNode, ActionAwareNode
{
    private transient Object data;
    private boolean directory;
    private boolean expandedByDefault;
    private int level;
    private NavigationNode parent;
    private boolean actionAware = true;
    private List<NavigationNode> children = new ArrayList<>();
    @JsonDeserialize(as = DefaultCockpitContext.class)
    private CockpitContext context;


    @JsonCreator
    public SimpleNode(@JsonProperty("id") final String id)
    {
        this(id, id);
    }


    public SimpleNode(final String id, final Object data)
    {
        super(id);
        this.data = data;
    }


    @Override
    public Object getData()
    {
        return data;
    }


    public void setData(final Object data)
    {
        this.data = data;
    }


    @Override
    public int getLevel()
    {
        return level;
    }


    public void setLevel(final int level)
    {
        this.level = level;
    }


    @Override
    public boolean isDirectory()
    {
        return this.directory;
    }


    public void setDirectory(final boolean directory)
    {
        this.directory = directory;
    }


    @Override
    public boolean isExpandedByDefault()
    {
        return expandedByDefault;
    }


    public void setExpandedByDefault(final boolean expandedByDefault)
    {
        this.expandedByDefault = expandedByDefault;
    }


    @Override
    public NavigationNode getParent()
    {
        return this.parent;
    }


    @Override
    public void setParent(final NavigationNode parent)
    {
        this.parent = parent;
    }


    @Override
    public List<NavigationNode> getChildren()
    {
        return this.children;
    }


    @Override
    public void setChildren(final List<NavigationNode> children)
    {
        final List<NavigationNode> internalChilds = new ArrayList<>(children);
        for(final NavigationNode child : internalChilds)
        {
            child.setParent(this);
        }
        this.children = internalChilds;
    }


    @Override
    public void addChild(final NavigationNode child)
    {
        child.setParent(this);
        this.children.add(child);
    }


    public void setActionAware(final boolean actionAware)
    {
        this.actionAware = actionAware;
    }


    @Override
    public boolean isActionAware()
    {
        return data != null && actionAware;
    }


    @Override
    public boolean equals(final Object obj)
    {
        if(!super.equals(obj))
        {
            return false;
        }
        final SimpleNode other = (SimpleNode)obj;
        if(this.data == null)
        {
            if(other.data != null)
            {
                return false;
            }
        }
        else if(!this.data.equals(other.data))
        {
            return false;
        }
        return true;
    }


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.data == null) ? 0 : this.data.hashCode());
        return result;
    }


    public static SimpleNode create(final ActionDefinition action)
    {
        final SimpleNode node = new SimpleNode(action.getId());
        node.setName(action.getName());
        node.setNameLocKey(action.getNameLocKey());
        node.setDescription(action.getDescription());
        node.setDescriptionLocKey(action.getDescriptionLocKey());
        node.setIconUri(action.getIconUri());
        node.setIconUriLocKey(action.getIconUriLocKey());
        node.setGroup(action.getGroup());
        return node;
    }


    @Override
    public CockpitContext getContext()
    {
        return context == null ? new DefaultCockpitContext() : context;
    }


    @Override
    public void setContext(final CockpitContext context)
    {
        this.context = context;
    }
}
