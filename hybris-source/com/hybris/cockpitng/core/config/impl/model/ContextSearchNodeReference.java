/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.model;

import com.hybris.cockpitng.core.config.impl.jaxb.Context;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * Represents a reference to other search node.
 * <p>
 * Searching for contexts is performed by traversing through context tree in regards to <code>merge-by</code>
 * attributes, obligatory merge attributes and/or appropriate
 * {@link com.hybris.cockpitng.core.config.CockpitConfigurationContextStrategy}. Each step down the tree means that any
 * contexts found are a little bit less relevant to actual request.
 * <P>
 * During this traverse it is possible that some searches are required more then once. To avoid performing them once
 * again, the reference may be put into tree instead.
 */
public class ContextSearchNodeReference implements ContextSearchNode
{
    private final ContextSearchNode reference;
    private ContextSearchNode parent;
    private List<ContextSearchNodeReference> children;


    /**
     * @param reference
     *           search node to be referenced
     */
    public ContextSearchNodeReference(final ContextSearchNode reference)
    {
        this.reference = reference;
    }


    @Override
    public NodeRelevance getRelevance()
    {
        return reference.getRelevance();
    }


    @Override
    public NodeRelevance getNotEmptyRelevance()
    {
        return reference.getNotEmptyRelevance();
    }


    @Override
    public int getLevel()
    {
        return parent != null ? parent.getLevel() + 1 : 0;
    }


    @Override
    public ContextSearchNeedle getSearchNeedle()
    {
        return reference.getSearchNeedle();
    }


    @Override
    public boolean isEgalitarian()
    {
        return reference.isEgalitarian();
    }


    @Override
    public void setEgalitarian(final boolean egalitarian)
    {
        reference.setEgalitarian(egalitarian);
    }


    @Override
    public boolean addNodeResult(final Context result)
    {
        return reference.addNodeResult(result);
    }


    @Override
    public List<Context> getNodeResult()
    {
        return reference.getNodeResult();
    }


    @Override
    public List<Context> getResult()
    {
        return reference.getResult();
    }


    @Override
    public ContextSearchNode getParent()
    {
        return parent;
    }


    @Override
    public void setParent(final ContextSearchNode parent)
    {
        if(this.parent != parent)
        {
            if(this.parent != null)
            {
                this.parent.removeChild(this);
            }
            this.parent = parent;
            if(parent != null && !(parent instanceof ContextSearchNodeReference))
            {
                parent.addChild(this);
            }
        }
    }


    @Override
    public void addChild(final ContextSearchNode child)
    {
        reference.addChild(child);
    }


    @Override
    public void removeChild(final ContextSearchNode child)
    {
        reference.removeChild(child);
    }


    @Override
    public Collection<ContextSearchNode> getChildren()
    {
        if(children == null)
        {
            children = reference.getChildren().stream().map(child -> {
                ContextSearchNodeReference contextSearchNodeReference = new ContextSearchNodeReference(child);
                contextSearchNodeReference.setParent(this);
                return contextSearchNodeReference;
            }).collect(Collectors.toList());
        }
        return Collections.<ContextSearchNode>unmodifiableCollection(children);
    }


    @Override
    public void toXML(final OutputStream os) throws IOException
    {
        if(!isCommitted() || getNotEmptyRelevance() != null)
        {
            final int level = getLevel();
            os.write(StringUtils.repeat('\t', level).getBytes());
            DefaultContextSearchNode.toTreeElement(os, this, "reference");
            DefaultContextSearchNode.toTreeElementEnd(os, "reference");
            os.write(System.lineSeparator().getBytes());
        }
    }


    @Override
    public void commit()
    {
        reference.commit();
    }


    @Override
    public boolean isCommitted()
    {
        return reference.isCommitted();
    }


    @Override
    public String toString()
    {
        return String.format("[REF] %s", reference.toString());
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null)
        {
            return false;
        }
        if(o.getClass() != this.getClass())
        {
            return false;
        }
        final ContextSearchNodeReference that = (ContextSearchNodeReference)o;
        return reference.equals(that.reference);
    }


    @Override
    public int hashCode()
    {
        return reference.hashCode();
    }
}
