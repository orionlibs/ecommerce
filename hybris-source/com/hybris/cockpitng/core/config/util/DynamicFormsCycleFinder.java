/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.DynamicAttribute;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.DynamicForms;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility class which let's check if dynamicForms has dynamicAttributes which have cyclic dependencies.
 */
public class DynamicFormsCycleFinder
{
    private final Set<String> visited;
    private final Set<String> onStack;
    private final Map<String, String> edgeTo;
    private final DynamicFormsGraph graph;
    private LinkedList<String> cycle;


    /**
     * Creates an instance of {@link DynamicFormsCycleFinder} and runs the search. It stops when when the first cycle is
     * found or all attributes have been checked.
     *
     * @param dynamicForms
     *           dynamicForms configuration.
     * @return Creates an instance of {@link DynamicFormsCycleFinder} which has has results of cycle search.
     */
    public static DynamicFormsCycleFinder findCycles(final DynamicForms dynamicForms)
    {
        final DynamicFormsGraph graph = new DynamicFormsGraph(dynamicForms.getAttribute());
        final DynamicFormsCycleFinder finder = new DynamicFormsCycleFinder(graph);
        return finder;
    }


    private DynamicFormsCycleFinder(final DynamicFormsGraph graph)
    {
        this.graph = graph;
        this.visited = Sets.newHashSet();
        this.onStack = Sets.newHashSet();
        this.edgeTo = Maps.newHashMap();
        graph.getVertices().forEach(vertex -> {
            if(!visited.contains(vertex))
            {
                deepFirstSearch(graph, vertex);
            }
        });
    }


    /**
     * Tells whether dynamicForms configuration has at least one cycle.
     */
    public boolean hasCycle()
    {
        return CollectionUtils.isNotEmpty(cycle);
    }


    /**
     * @return list of dynamicAttributes in found cycle.
     */
    public List<DynamicAttribute> getCycle()
    {
        return graph.getAttributesFromCycle(cycle);
    }


    private void deepFirstSearch(final DynamicFormsGraph graph, final String vertex)
    {
        onStack.add(vertex);
        visited.add(vertex);
        for(final String w : graph.getVerticesFrom(vertex))
        {
            if(cycle != null)
            {
                return;
            }
            else if(!visited.contains(w))
            {
                edgeTo.put(w, vertex);
                deepFirstSearch(graph, w);
            }
            else if(onStack.contains(w))
            {
                cycle = Lists.newLinkedList();
                for(String x = vertex; !StringUtils.equals(x, w); x = edgeTo.get(x))
                {
                    cycle.push(x);
                }
                cycle.push(w);
                cycle.push(vertex);
            }
        }
        onStack.remove(vertex);
    }
}
