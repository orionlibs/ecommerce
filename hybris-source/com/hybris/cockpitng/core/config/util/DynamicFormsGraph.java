/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.DynamicAttribute;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * Graph implementation to detect cyclic references in dynamicForms.
 */
public class DynamicFormsGraph
{
    private final Map<String, Map<String, Edge>> edges = Maps.newHashMap();


    public DynamicFormsGraph(final List<DynamicAttribute> attributes)
    {
        final List<DynamicAttribute> attributesWithComputedValue = attributes.stream()
                        .filter(da -> StringUtils.isNotBlank(da.getComputedValue())).collect(Collectors.toList());
        attributesWithComputedValue.forEach(da -> edges.put(da.getQualifier(), Maps.newHashMap()));
        attributesWithComputedValue.forEach(da -> addAttribute(da));
    }


    public Set<String> getVertices()
    {
        return edges.keySet();
    }


    public Set<String> getVerticesFrom(final String vertex)
    {
        return edges.containsKey(vertex) ? edges.get(vertex).keySet() : Collections.emptySet();
    }


    public List<DynamicAttribute> getAttributesFromCycle(final List<String> cycle)
    {
        final List<DynamicAttribute> attributes = Lists.newArrayList();
        final Iterator<String> cycleIterator = cycle.iterator();
        if(cycleIterator.hasNext())
        {
            findAttributes(cycleIterator.next(), cycleIterator, attributes);
        }
        return attributes;
    }


    private void findAttributes(final String from, final Iterator<String> cycleIterator, final List<DynamicAttribute> attributes)
    {
        if(cycleIterator.hasNext())
        {
            final String to = cycleIterator.next();
            final Edge edge = getEdgeFromTo(from, to);
            if(edge != null)
            {
                if(edge.getAttributes() != null)
                {
                    attributes.addAll(edge.getAttributes());
                }
                findAttributes(to, cycleIterator, attributes);
            }
        }
    }


    private void addAttribute(final DynamicAttribute attribute)
    {
        for(final String trigger : attribute.getTriggeredOn().split(","))
        {
            final String trimmedTrigger = trigger.trim();
            if(edges.containsKey(trimmedTrigger))
            {
                addEdge(trimmedTrigger, attribute.getQualifier(), attribute);
            }
        }
    }


    private void addEdge(final String from, final String to, final DynamicAttribute attribute)
    {
        final Map<String, Edge> edgeToVertex = edges.get(from);
        if(edgeToVertex != null)
        {
            Edge edge = edgeToVertex.get(to);
            if(edge == null)
            {
                edge = new Edge(to);
                edgeToVertex.put(to, edge);
            }
            edge.addAttribute(attribute);
        }
    }


    private Edge getEdgeFromTo(final String from, final String to)
    {
        return edges.containsKey(from) ? edges.get(from).get(to) : null;
    }


    private static class Edge
    {
        final String to;
        final Set<DynamicAttribute> attributes;


        Edge(final String to)
        {
            this.to = to;
            attributes = Sets.newHashSet();
        }


        void addAttribute(final DynamicAttribute attribute)
        {
            if(attribute.getQualifier().equals(to))
            {
                attributes.add(attribute);
            }
        }


        Set<DynamicAttribute> getAttributes()
        {
            return attributes;
        }
    }
}
