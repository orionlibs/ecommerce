/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.cache;

import com.hybris.cockpitng.core.config.impl.jaxb.Config;
import com.hybris.cockpitng.core.config.impl.jaxb.Context;
import com.hybris.cockpitng.core.util.impl.IdentityUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

public class DefaultContextQuery implements ContextQuery
{
    private final DefaultConfigurationCache cache;
    private final Config config;
    private final ContextAttributeComparator comparator;
    private final Map<String, Collection<String>> restrictions;
    // Most commonly restrictions are added in the way, that those at the end are narrowing results the most.
    // To possibly finish the loop in #execute() the fastest it is possible, it is better to narrow results
    // starting from last added restriction, so that we may possible exclude all results at the beginning.
    private final List<String> restrictedAttributes;
    private boolean ignoreEmpty;


    public DefaultContextQuery(final DefaultConfigurationCache cache, final Config config,
                    final ContextAttributeComparator comparator)
    {
        this.cache = cache;
        this.config = config;
        this.comparator = comparator;
        this.restrictions = new HashMap<>();
        this.restrictedAttributes = new ArrayList<>();
    }


    @Override
    public ContextQuery notEmpty()
    {
        ignoreEmpty = true;
        return this;
    }


    @Override
    public ContextQuery matchesAny(final String name, final Collection<String> values)
    {
        final Collection<String> restriction;
        if(!restrictions.containsKey(name))
        {
            restriction = new LinkedHashSet<>();
            restrictedAttributes.add(0, name);
            restrictions.put(name, restriction);
        }
        else
        {
            restriction = restrictions.get(name);
        }
        restriction.addAll(values);
        return this;
    }


    @Override
    public Collection<Context> execute()
    {
        try
        {
            cache.requestReadLock();
            final Collection<Context> allContexts = cache.getContexts(config);
            if(allContexts == null)
            {
                return null;
            }
            Collection<Context> result = new IdentityUtils.DistinctCollection<>(allContexts);
            for(final Iterator<String> it = restrictedAttributes.iterator(); it.hasNext() && !result.isEmpty(); )
            {
                final String name = it.next();
                final Collection<String> restriction = restrictions.get(name);
                final Collection<Context> contexts = restriction.stream()
                                .map(value -> cache.getContextsForAttribute(allContexts, name, value, comparator))
                                .flatMap(Collection::stream)
                                .collect(IdentityUtils.distinctCollector(allContexts.size()));
                result.retainAll(contexts);
            }
            if(CollectionUtils.isNotEmpty(result) && ignoreEmpty)
            {
                result = result.stream().filter(ctx -> ctx.getAny() != null).collect(Collectors.toList());
            }
            return Collections.unmodifiableCollection(result);
        }
        finally
        {
            cache.releaseReadLock();
        }
    }
}
