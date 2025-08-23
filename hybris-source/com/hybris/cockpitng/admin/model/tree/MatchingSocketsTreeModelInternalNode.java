/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin.model.tree;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.core.util.Validate;
import java.util.List;

public class MatchingSocketsTreeModelInternalNode
{
    private final MatchingSocketsTreeModelInternalNode parent;
    private final Object element;
    private final List<MatchingSocketsTreeModelInternalNode> children;


    public MatchingSocketsTreeModelInternalNode(final MatchingSocketsTreeModelInternalNode parent, final Object element,
                    final List<MatchingSocketsTreeModelInternalNode> children)
    {
        Validate.notNull("Element must not be null", element);
        this.parent = parent;
        this.element = element;
        this.children = (children == null ? Lists.<MatchingSocketsTreeModelInternalNode>newArrayList() : children);
    }


    public Object getElement()
    {
        return element;
    }


    public MatchingSocketsTreeModelInternalNode getParent()
    {
        return parent;
    }


    public List<MatchingSocketsTreeModelInternalNode> getChildren()
    {
        return children;
    }


    public MatchingSocketsTreeModelInternalNode getOrInsertElement(final Object elem)
    {
        for(final MatchingSocketsTreeModelInternalNode child : children)
        {
            if(child.element.equals(elem))
            {
                return child;
            }
        }
        final MatchingSocketsTreeModelInternalNode result = new MatchingSocketsTreeModelInternalNode(this, elem,
                        Lists.<MatchingSocketsTreeModelInternalNode>newArrayList());
        children.add(result);
        return result;
    }


    public void remove(final MatchingSocketsTreeModelInternalNode node)
    {
        getChildren().remove(node);
        if(getChildren().isEmpty() && parent != null)
        {
            parent.remove(this);
        }
    }


    @Override
    public boolean equals(final Object object)
    {
        if(this == object)
        {
            return true;
        }
        if(object == null || getClass() != object.getClass())
        {
            return false;
        }
        final MatchingSocketsTreeModelInternalNode that = (MatchingSocketsTreeModelInternalNode)object;
        return !(element == null ? that.element != null : !element.equals(that.element));
    }


    @Override
    public int hashCode()
    {
        return element == null ? 0 : element.hashCode();
    }
}
