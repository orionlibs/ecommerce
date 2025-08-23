/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.adminmode;

import com.hybris.cockpitng.core.config.CockpitConfigurationContextStrategy;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.DefaultCockpitConfigurationService;
import com.hybris.cockpitng.core.config.impl.jaxb.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.namespace.QName;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides list of available attribute values on the basis of search conditions
 */
class FilterOptionsProvider
{
    protected static final String COMPONENT_ATTR = "component";
    protected static final String PRINCIPAL_ATTR = "principal";
    protected static final String TYPE_ATTR = "type";
    private static final Logger LOG = LoggerFactory.getLogger(FilterOptionsProvider.class);
    private final DefaultCockpitConfigurationService cockpitConfigurationService;
    //
    private List<Context> allContextList = new ArrayList<>();
    private Map<String, Set<String>> allAvailableOptions = new HashMap<>();
    private final FilteredContext fv;


    public FilterOptionsProvider(final DefaultCockpitConfigurationService cockpitConfigurationService, final FilteredContext fv)
    {
        this.cockpitConfigurationService = cockpitConfigurationService;
        this.fv = fv;
        FilterOptionsProvider.this.init();
    }


    protected void init()
    {
        try
        {
            allContextList = cockpitConfigurationService.loadRootConfiguration().getContext();
            allAvailableOptions = collectContextAttributesWithValues(allContextList);
        }
        catch(final CockpitConfigurationException ex)
        {
            LOG.error("Cannot load root configuration", ex);
        }
    }


    public Map<String, Set<String>> getAvailableAttributeValues(final Map<String, String> searchCondition)
    {
        if(MapUtils.isEmpty(searchCondition))
        {
            fv.setFilteredContextList(allContextList);
            return allAvailableOptions;
        }
        else
        {
            final List<Context> filteredContextList = filterConfig(allContextList, searchCondition,
                            cockpitConfigurationService.getContextStrategies());
            fv.setFilteredContextList(filteredContextList);
            return collectContextAttributesWithValues(fv.getFilteredContextList());
        }
    }


    private Map<String, Set<String>> collectContextAttributesWithValues(final List<Context> contextList)
    {
        final Map<String, Set<String>> contextAttribWithValues = new LinkedHashMap<>();
        contextAttribWithValues.put(COMPONENT_ATTR, new TreeSet<>());
        contextAttribWithValues.put(TYPE_ATTR, new TreeSet<>());
        contextAttribWithValues.put(PRINCIPAL_ATTR, new TreeSet<>());
        for(final Context ctx : contextList)
        {
            visitContext(ctx, contextAttribWithValues);
        }
        return contextAttribWithValues;
    }


    private void visitContext(final Context ctx, final Map<String, Set<String>> contextAttributes)
    {
        final String type = ctx.getType();
        final String component = ctx.getComponent();
        final String principal = ctx.getPrincipal();
        if(type != null && !contextAttributes.get(TYPE_ATTR).contains(type))
        {
            contextAttributes.get(TYPE_ATTR).add(type);
        }
        if(component != null && !contextAttributes.get(COMPONENT_ATTR).contains(component))
        {
            contextAttributes.get(COMPONENT_ATTR).add(component);
        }
        if(principal != null && !contextAttributes.get(PRINCIPAL_ATTR).contains(principal))
        {
            contextAttributes.get(PRINCIPAL_ATTR).add(principal);
        }
        for(final Map.Entry<QName, String> other : ctx.getOtherAttributes().entrySet())
        {
            final String key = other.getKey().toString();
            final String value = other.getValue();
            if(value != null)
            {
                if(!contextAttributes.containsKey(key))
                {
                    contextAttributes.put(key, new TreeSet<>());
                }
                if(!contextAttributes.get(key).contains(value))
                {
                    contextAttributes.get(key).add(value);
                }
            }
        }
        for(final Context childrenContext : ctx.getContext())
        {
            visitContext(childrenContext, contextAttributes);
        }
    }


    private List<Context> filterConfig(final List<Context> allContexts, final Map<String, String> searchCriteria,
                    final Map<String, CockpitConfigurationContextStrategy> contextStrategies)
    {
        return filterConfigRecursive(allContexts, new HashMap<>(), searchCriteria, contextStrategies);
    }


    private List<Context> filterConfigRecursive(final List<Context> allContexts, final Map<String, String> inherited,
                    final Map<String, String> searchCriteria, final Map<String, CockpitConfigurationContextStrategy> contextStrategies)
    {
        final List<Context> filteredContexts = new ArrayList<>(allContexts);
        if(filteredContexts.isEmpty())
        {
            return filteredContexts;
        }
        final Set<Integer> indexesToCopy = new HashSet<>();
        for(int i = 0; i < filteredContexts.size(); i++)
        {
            if(!filteredContexts.get(i).getContext().isEmpty())
            {
                indexesToCopy.add(i);
            }
        }
        for(final Integer idx : indexesToCopy)
        {
            filteredContexts.set(idx, copy(filteredContexts.get(idx)));
        }
        final Iterator<Context> iterator = filteredContexts.iterator();
        while(iterator.hasNext())
        {
            final Context ctx = iterator.next();
            final Map<String, String> actualAttributes = mergeAttributes(ctx, inherited);
            final List<Context> subContext = ctx.getContext();
            if(!subContext.isEmpty())
            {
                // ctx could be modified because it is copy
                final List<Context> filteredSubContexts = filterConfigRecursive(subContext, actualAttributes, searchCriteria,
                                contextStrategies);
                ctx.getContext().clear();
                ctx.getContext().addAll(filteredSubContexts);
            }
            if(ctx.getContext().isEmpty() && !contextMeetCriteria(actualAttributes, searchCriteria, contextStrategies))
            {
                iterator.remove();
            }
        }
        return filteredContexts;
    }


    private static Context copy(final Context in)
    {
        if(in == null)
        {
            return null;
        }
        final Context out = new Context();
        out.setAny(in.getAny());
        out.setComponent(in.getComponent());
        out.setMergeBy(in.getMergeBy());
        out.setParent(in.getParent());
        out.setParentContext(in.getParentContext());
        out.setPrincipal(in.getPrincipal());
        out.setType(in.getType());
        out.getOtherAttributes().putAll(in.getOtherAttributes());
        for(final Context sub : in.getContext())
        {
            out.getContext().add(copy(sub));
        }
        return out;
    }


    private Map<String, String> mergeAttributes(final Context context, final Map<String, String> otherAttributes)
    {
        final Map<String, String> result = new HashMap<>(otherAttributes);
        if(StringUtils.isNotEmpty(context.getComponent()))
        {
            result.put(COMPONENT_ATTR, context.getComponent());
        }
        if(StringUtils.isNotEmpty(context.getPrincipal()))
        {
            result.put(PRINCIPAL_ATTR, context.getPrincipal());
        }
        if(StringUtils.isNotEmpty(context.getType()))
        {
            result.put(TYPE_ATTR, context.getType());
        }
        for(final Map.Entry<QName, String> other : context.getOtherAttributes().entrySet())
        {
            result.put(other.getKey().toString(), other.getValue());
        }
        return result;
    }


    private boolean contextMeetCriteria(final Map<String, String> actualValues, final Map<String, String> searchCriteria,
                    final Map<String, CockpitConfigurationContextStrategy> strategyMap)
    {
        for(final Map.Entry<String, String> criteria : searchCriteria.entrySet())
        {
            final String key = criteria.getKey();
            final String val = criteria.getValue();
            if(strategyMap.containsKey(key))
            {
                if(!strategyMap.get(key).valueMatches(actualValues.get(key), val))
                {
                    return false;
                }
            }
            else
            {
                if(!StringUtils.equals(actualValues.get(key), val))
                {
                    return false;
                }
            }
        }
        return true;
    }
}
