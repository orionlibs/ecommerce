/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.model;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.core.config.impl.jaxb.Context;
import com.hybris.cockpitng.core.util.impl.IdentityUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Represents a single search needle with all its results in the tree of context searches.
 * <p>
 * Searching of contexts is performed by traversing through context tree in regards to <code>merge-by</code> attributes,
 * obligatory merge attributes and/or appropriate
 * {@link com.hybris.cockpitng.core.config.CockpitConfigurationContextStrategy}. Each step down the tree means that any
 * contexts found are a little bit less relevant to actual request. It is represented by search level and results in
 * lower priority during merge process - a context with lower priority (found on lower level) is merged into a context
 * with higher priority (found on higher level).
 */
public class DefaultContextSearchNode implements ContextSearchNode
{
    private static final String EXCEPTION_MESSAGE = "Unable to change committed node";
    private final ContextSearchNeedle searchNeedle;
    private final NodeRelevance relevance;
    private final Set<Context> result = new LinkedHashSet<>();
    private final Set<ContextSearchNode> children = new LinkedHashSet<>();
    private ContextSearchNode parent;
    private boolean egalitarian = false;
    private NodeRelevance notEmptyRelevance;
    private boolean committed;


    /**
     * @param searchNeedle
     *           search request bound to this node
     * @param relevance
     *           relevance of this request
     */
    public DefaultContextSearchNode(final ContextSearchNeedle searchNeedle, final NodeRelevance relevance)
    {
        this.searchNeedle = searchNeedle;
        this.relevance = relevance;
    }


    @Override
    public NodeRelevance getRelevance()
    {
        return relevance;
    }


    @Override
    public ContextSearchNeedle getSearchNeedle()
    {
        return searchNeedle;
    }


    @Override
    public boolean isEgalitarian()
    {
        return egalitarian;
    }


    @Override
    public void setEgalitarian(final boolean egalitarian)
    {
        if(isCommitted())
        {
            throw new IllegalStateException(EXCEPTION_MESSAGE);
        }
        this.egalitarian = egalitarian;
    }


    @Override
    public boolean addNodeResult(final Context result)
    {
        if(isCommitted())
        {
            throw new IllegalStateException(EXCEPTION_MESSAGE);
        }
        return this.result.add(result);
    }


    @Override
    public List<Context> getNodeResult()
    {
        return Collections.unmodifiableList(Lists.newArrayList(result));
    }


    protected Collection<ContextSearchNode> getResultChildren()
    {
        return getResultChildren(this);
    }


    public static Collection<ContextSearchNode> getResultChildren(final ContextSearchNode node)
    {
        if(node.isEgalitarian())
        {
            final List<ContextSearchNode> nodes = new ArrayList<>();
            node.getChildren().forEach(child -> {
                nodes.add(child);
                nodes.addAll(getResultChildren(child));
            });
            return nodes;
        }
        else
        {
            final List<ContextSearchNode> nodes = node.getChildren().stream().filter(child -> child.getNotEmptyRelevance() != null)
                            .collect(Collectors.toList());
            Collections.sort(nodes, Comparator.comparing(ContextSearchNode::getNotEmptyRelevance, Comparator.reverseOrder()));
            if(CollectionUtils.isNotEmpty(nodes))
            {
                final ContextSearchNode max = nodes.get(0);
                final List<ContextSearchNode> result = new ArrayList<>();
                result.add(max);
                result.addAll(getResultChildren(max));
                return result;
            }
            else
            {
                return Collections.emptyList();
            }
        }
    }


    @Override
    public List<Context> getResult()
    {
        final List<Context> contexts = new ArrayList<>(result);
        contexts.addAll(getResultChildren().stream().filter(child -> CollectionUtils.isNotEmpty(child.getNodeResult()))
                        .sorted(ContextSearchNode.DEC_RELEVANCE_COMPARATOR).map(ContextSearchNode::getNodeResult).flatMap(Collection::stream)
                        .filter(new IdentityUtils.DistinctFilter<>()).collect(Collectors.toList()));
        return contexts;
    }


    @Override
    public NodeRelevance getNotEmptyRelevance()
    {
        if(!isCommitted())
        {
            throw new IllegalStateException("Commit node before counting its not empty relevance");
        }
        return notEmptyRelevance;
    }


    protected NodeRelevance countNotEmptyRelevance()
    {
        if(CollectionUtils.isNotEmpty(result))
        {
            return getRelevance();
        }
        else
        {
            final List<NodeRelevance> relevances = children.stream().map(ContextSearchNode::getNotEmptyRelevance)
                            .filter(Objects::nonNull).collect(Collectors.toList());
            Collections.sort(relevances, Comparator.reverseOrder());
            if(CollectionUtils.isNotEmpty(relevances))
            {
                return relevances.get(0);
            }
            else
            {
                return null;
            }
        }
    }


    @Override
    public int getLevel()
    {
        if(parent == null)
        {
            return 0;
        }
        else
        {
            return parent.getLevel() + 1;
        }
    }


    @Override
    public ContextSearchNode getParent()
    {
        return parent;
    }


    @Override
    public void setParent(final ContextSearchNode parent)
    {
        if(parent == this)
        {
            throw new IllegalArgumentException("Cannot set a search node as a parent of itself");
        }
        if(this.getParent() != parent)
        {
            if(this.parent != null)
            {
                this.parent.removeChild(this);
            }
            setParentImmediately(parent);
            if(parent != null && !parent.getChildren().contains(this))
            {
                parent.addChild(this);
            }
        }
    }


    protected void setParentImmediately(final ContextSearchNode parent)
    {
        if(isCommitted() && this.parent != parent)
        {
            throw new IllegalStateException(EXCEPTION_MESSAGE);
        }
        this.parent = parent;
    }


    @Override
    public void addChild(final ContextSearchNode child)
    {
        if(child == null)
        {
            throw new IllegalArgumentException("Cannot add null search node");
        }
        if(child == this)
        {
            throw new IllegalArgumentException("Cannot add a search node to itself");
        }
        addChildImmediately(child);
        if(child.getParent() != this)
        {
            child.setParent(this);
        }
    }


    protected boolean addChildImmediately(final ContextSearchNode child)
    {
        if(isCommitted() && !this.children.contains(child))
        {
            throw new IllegalStateException(EXCEPTION_MESSAGE);
        }
        return this.children.add(child);
    }


    @Override
    public void removeChild(final ContextSearchNode child)
    {
        if(removeChildImmediately(child))
        {
            child.setParent(null);
        }
    }


    protected boolean removeChildImmediately(final ContextSearchNode child)
    {
        if(isCommitted() && this.children.contains(child))
        {
            throw new IllegalStateException(EXCEPTION_MESSAGE);
        }
        return this.children.remove(child);
    }


    @Override
    public boolean isCommitted()
    {
        return committed;
    }


    @Override
    public void commit()
    {
        if(!committed)
        {
            notEmptyRelevance = countNotEmptyRelevance();
            committed = true;
        }
    }


    @Override
    public Collection<ContextSearchNode> getChildren()
    {
        return Collections.unmodifiableCollection(children);
    }


    /**
     * Writes single search node attribute to provided output stream.
     *
     * @param os
     *           destination stream
     * @param name
     *           name of attribute to be written
     * @param value
     *           value of attribute to be written
     * @throws IOException
     *            thrown if some problems occurred while writing to stream
     */
    public static void toTreeElementAttribute(final OutputStream os, final String name, final String value) throws IOException
    {
        os.write(String.format(" %s=\"%s\"", name, value).getBytes());
    }


    /**
     * Writes all attributes of single search node to provided output stream.
     *
     * @param os
     *           destination stream
     * @param node
     *           search node which attributes are to be written
     * @throws IOException
     *            thrown if some problems occurred while writing to stream
     */
    public static void toTreeElementAttributes(final OutputStream os, final ContextSearchNode node) throws IOException
    {
        toTreeElementAttribute(os, "relevance", node.getRelevance().toString());
        if(node.isCommitted())
        {
            toTreeElementAttribute(os, "not-empty", String.valueOf(node.getNotEmptyRelevance()));
        }
        else
        {
            toTreeElementAttribute(os, "not-empty", "not committed");
        }
        toTreeElementAttribute(os, "result", Integer.toString(node.getNodeResult().size()));
        if(node.getSearchNeedle() != null)
        {
            for(final Map.Entry<String, String> entry : node.getSearchNeedle().getAttributes().entrySet())
            {
                toTreeElementAttribute(os, entry.getKey(), entry.getValue());
            }
        }
        if(node.isEgalitarian())
        {
            toTreeElementAttribute(os, "egalitarian", "true");
        }
    }


    /**
     * Writes XML representation of single search node to provided output stream. XML element written is not closed and
     * {@link #toTreeElementEnd(OutputStream, String)} should be also called.
     *
     * @param os
     *           destination stream
     * @param node
     *           search node which is to be written
     * @param elementName
     *           name of element that should represent provided node
     * @throws IOException
     *            thrown if some problems occurred while writing to stream
     */
    public static void toTreeElement(final OutputStream os, final ContextSearchNode node, final String elementName)
                    throws IOException
    {
        os.write(String.format("<%s", elementName).getBytes());
        toTreeElementAttributes(os, node);
        os.write(">".getBytes());
    }


    /**
     * Writes a closure of XML element to provided output stream.
     *
     * @param os
     *           destination stream
     * @param elementName
     *           name of element that should be closed
     * @throws IOException
     *            thrown if writing to stream fails
     */
    public static void toTreeElementEnd(final OutputStream os, final String elementName) throws IOException
    {
        os.write(String.format("</%s>", elementName).getBytes());
    }


    @Override
    public void toXML(final OutputStream os) throws IOException
    {
        if(getSearchNeedle() == null)
        {
            os.write("<root>".getBytes());
            os.write(System.lineSeparator().getBytes());
            for(final ContextSearchNode child : getChildren())
            {
                child.toXML(os);
            }
            os.write("</root>".getBytes());
            os.write(System.lineSeparator().getBytes());
        }
        else if(!isCommitted() || getNotEmptyRelevance() != null)
        {
            final int level = getLevel();
            os.write(StringUtils.repeat('\t', level).getBytes());
            toTreeElement(os, this, "search");
            os.write(System.lineSeparator().getBytes());
            for(final ContextSearchNode child : getChildren())
            {
                child.toXML(os);
            }
            os.write(StringUtils.repeat('\t', level).getBytes());
            toTreeElementEnd(os, "search");
            os.write(System.lineSeparator().getBytes());
        }
    }


    @Override
    public String toString()
    {
        return String.format("(%s) %s: %s", getRelevance(), getSearchNeedle().getAttributes().toString(), getNodeResult().size());
    }
}
