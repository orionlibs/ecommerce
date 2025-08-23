/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.cache;

import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.Config;
import com.hybris.cockpitng.core.config.impl.jaxb.Context;
import com.hybris.cockpitng.core.config.impl.model.ContextSearchRequest;
import com.hybris.cockpitng.core.util.impl.IdentityUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultConfigurationCache implements ConfigurationCache
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConfigurationCache.class);
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Map<Config, Collection<CachedContextImpl>> contexts = new HashMap<>();
    private final Map<String, Map<String, Collection<CachedContextImpl>>> contextsByAttributes = new HashMap<>();
    private final Map<Pair<String, String>, Collection<CachedContextImpl>> contextMatches = new HashMap<>();
    private final Map<String, Map<ConfigContext, Object>> configurations = new HashMap<>();
    private long timestamp = Long.MIN_VALUE;
    private Config rootConfig;


    protected Collection<CachedContextImpl> createContextsContainer(final Collection<? extends Context> elements)
    {
        return elements.stream().map(this::wrapContext).collect(Collectors.toCollection(LinkedHashSet::new));
    }


    @Override
    public boolean isValid(final long origin)
    {
        return timestamp >= origin;
    }


    @Override
    public void clear()
    {
        try
        {
            requestWriteLock();
            this.timestamp = Long.MIN_VALUE;
            this.rootConfig = null;
            contexts.clear();
            clearAttributes();
            clearConfigurations();
        }
        finally
        {
            releaseWriteLock();
        }
    }


    @Override
    public Config getRootConfiguration()
    {
        try
        {
            requestReadLock();
            return rootConfig;
        }
        finally
        {
            releaseReadLock();
        }
    }


    @Override
    public void cacheRootConfiguration(final Config config, final long origin)
    {
        try
        {
            requestWriteLock();
            this.rootConfig = config;
            this.timestamp = origin;
        }
        finally
        {
            releaseWriteLock();
        }
    }


    @Override
    public Collection<Context> getContexts(final Config config)
    {
        try
        {
            requestReadLock();
            final Collection<CachedContextImpl> ctx = this.contexts.get(config);
            return ctx != null ? Collections.unmodifiableCollection(ctx) : null;
        }
        finally
        {
            releaseReadLock();
        }
    }


    @Override
    public void cacheContexts(final Config config, final Collection<Context> contexts, final ConfigurationContextResolver resolver)
    {
        try
        {
            requestWriteLock();
            if(this.contexts.containsKey(config))
            {
                this.contexts.get(config).forEach(context -> context.attributes
                                .forEach((key, value) -> contextsByAttributes.get(key).get(value).remove(context)));
            }
            contextMatches.clear();
            final Collection<CachedContextImpl> contextsContainer = createContextsContainer(contexts);
            this.contexts.put(config, contextsContainer);
            contextsContainer.forEach(ctx -> cacheAttributes(ctx, resolver.getContextAttributes(ctx)));
        }
        finally
        {
            releaseWriteLock();
        }
    }


    @Override
    public void clearAttributes()
    {
        try
        {
            requestWriteLock();
            contexts.values().stream().flatMap(Collection::stream).filter(ctx -> ctx.attributes != null).forEach(ctx -> {
                ctx.attributes.clear();
                ctx.attributes = null;
            });
            contextsByAttributes.clear();
            contextMatches.clear();
        }
        finally
        {
            releaseWriteLock();
        }
    }


    @Override
    public void clearAttributes(final Context context)
    {
        if(context instanceof CachedContextImpl)
        {
            try
            {
                requestWriteLock();
                final Map<String, String> attributes = ((CachedContextImpl)context).attributes;
                if(MapUtils.isNotEmpty(attributes))
                {
                    ((CachedContextImpl)context).attributes.clear();
                    ((CachedContextImpl)context).attributes = null;
                    attributes.forEach((name, value) -> contextsByAttributes.get(name).get(value).remove(context));
                    contextMatches.entrySet().stream().filter(attribute -> {
                        final String attrValue = attributes.get(attribute.getKey().getLeft());
                        return attribute.getKey().getRight().equals(attrValue);
                    }).forEach(matches -> matches.getValue().remove(context));
                }
            }
            finally
            {
                releaseWriteLock();
            }
        }
    }


    @Override
    public Map<String, String> getAttributes(final Context context)
    {
        if(context instanceof CachedContextImpl)
        {
            try
            {
                requestReadLock();
                if(((CachedContextImpl)context).attributes != null)
                {
                    return Collections.unmodifiableMap(((CachedContextImpl)context).attributes);
                }
                else
                {
                    return null;
                }
            }
            finally
            {
                releaseReadLock();
            }
        }
        else
        {
            return null;
        }
    }


    @Override
    public ContextQuery createContextQuery(final Config config, final ContextAttributeComparator comparator)
    {
        return new DefaultContextQuery(this, config, comparator);
    }


    protected void cacheAttributes(final CachedContextImpl context, final Map<String, String> attributes)
    {
        try
        {
            requestWriteLock();
            context.attributes = new HashMap<>(attributes);
            attributes.forEach((key, value) -> {
                final Map<String, Collection<CachedContextImpl>> contextsByValues = contextsByAttributes.computeIfAbsent(key,
                                k -> new LinkedHashMap<>());
                Collection<CachedContextImpl> ctx = contextsByValues.get(value);
                if(ctx == null)
                {
                    ctx = createContextsContainer(Collections.emptySet());
                    contextsByValues.put(value, ctx);
                }
                ctx.add(context);
            });
        }
        finally
        {
            releaseWriteLock();
        }
    }


    protected void requestReadLock()
    {
        rwLock.readLock().lock();
    }


    protected void releaseReadLock()
    {
        rwLock.readLock().unlock();
    }


    protected void requestWriteLock()
    {
        rwLock.writeLock().lock();
    }


    protected void releaseWriteLock()
    {
        rwLock.writeLock().unlock();
    }


    protected Collection<CachedContextImpl> getCachedMatchingContexts(final String name, final String value)
    {
        final Pair<String, String> key = new ImmutablePair<>(name, value);
        return contextMatches.get(key);
    }


    protected void cacheMatchingContexts(final String name, final String value, final Collection<CachedContextImpl> contexts)
    {
        final Pair<String, String> key = new ImmutablePair<>(name, value);
        contextMatches.put(key, contexts);
    }


    protected Collection<CachedContextImpl> getContextsForAttribute(final Collection<? extends Context> rootContexts,
                    final String name, final String value, final ContextAttributeComparator comparator)
    {
        try
        {
            requestReadLock();
            Collection<CachedContextImpl> result = getCachedMatchingContexts(name, value);
            if(result != null)
            {
                result = createContextsContainer(result);
            }
            else
            {
                result = getContextsForAttributeIfExisted(rootContexts, name, value, comparator);
            }
            return Collections
                            .unmodifiableCollection(result.stream().filter(el -> rootContexts.contains(el)).collect(Collectors.toList()));
        }
        finally
        {
            releaseReadLock();
        }
    }


    private Collection<CachedContextImpl> getContextsForAttributeIfExisted(final Collection<? extends Context> rootContexts,
                    final String name, final String value, final ContextAttributeComparator comparator)
    {
        final Map<String, Collection<CachedContextImpl>> contextsWithAttribute = contextsByAttributes.get(name);
        if(contextsWithAttribute == null)
        {
            if(StringUtils.isNotEmpty(value))
            {
                return createContextsContainer(Collections.emptySet());
            }
            else
            {
                return createContextsContainer(rootContexts);
            }
        }
        else
        {
            if(StringUtils.isEmpty(value))
            {
                final Collection<CachedContextImpl> allContexts = contexts.values().stream().flatMap(Collection::stream)
                                .collect(IdentityUtils.distinctCollector(rootContexts.size()));
                final Collection<CachedContextImpl> allContextsWithAttribute = contextsWithAttribute.values().stream()
                                .flatMap(Collection::stream).collect(IdentityUtils.distinctCollector(rootContexts.size()));
                allContexts.removeAll(allContextsWithAttribute);
                cacheMatchingContexts(name, value, allContexts);
                return allContexts;
            }
            else
            {
                final Collection<CachedContextImpl> result = contextsWithAttribute.entrySet().stream()
                                .filter(entry -> comparator.matches(name, entry.getKey(), value)).map(Map.Entry::getValue)
                                .flatMap(Collection::stream).collect(IdentityUtils.distinctCollector(rootContexts.size()));
                cacheMatchingContexts(name, value, result);
                return result;
            }
        }
    }


    @Override
    public void clearConfigurations()
    {
        try
        {
            requestWriteLock();
            configurations.clear();
        }
        finally
        {
            releaseWriteLock();
        }
    }


    @Override
    public <C> C getConfiguration(final ConfigContext context, final Class<C> configType)
    {
        try
        {
            requestReadLock();
            final Map<ConfigContext, Object> results = getConfigurations(configType);
            final Object result = results.get(context);
            if(result != null && configType.isAssignableFrom(result.getClass()))
            {
                return (C)result;
            }
            else
            {
                return null;
            }
        }
        finally
        {
            releaseReadLock();
        }
    }


    protected Map<ConfigContext, Object> getConfigurations(final Class<?> configType)
    {
        final Map<ConfigContext, Object> results = configurations.get(configType.getName());
        if(results != null)
        {
            return Collections.unmodifiableMap(results);
        }
        else
        {
            return Collections.emptyMap();
        }
    }


    @Override
    public <C> void cacheConfiguration(final ConfigContext context, final Class<C> configType, final C config)
    {
        try
        {
            requestWriteLock();
            final Map<ConfigContext, Object> cache = configurations.computeIfAbsent(configType.getName(), k -> new HashMap<>());
            cache.put(context, config);
        }
        finally
        {
            releaseWriteLock();
        }
    }


    protected Stream<Pair<String, String>> extractConstantValues(final ContextSearchRequest request, final Set<String> constants)
    {
        return constants.stream().flatMap(attribute -> request.getAttributesNeedle().get(attribute).stream()
                        .map(value -> new ImmutablePair<>(attribute, value)));
    }


    protected Stream<Context> getMatchingContexts(final Collection<? extends Context> contexts,
                    final Stream<Pair<String, String>> attributes, final ContextAttributeComparator comparator)
    {
        return attributes.flatMap(
                        condition -> getContextsForAttribute(contexts, condition.getKey(), condition.getValue(), comparator).stream());
    }


    protected Stream<Map.Entry<String, String>> extractAttributes(final Stream<Context> contexts)
    {
        return contexts.map(context -> ObjectUtils.defaultIfNull(getAttributes(context), Collections.<String, String>emptyMap()))
                        .flatMap(attributes -> attributes.entrySet().stream());
    }


    @Override
    public ConfigurationSearchFilter prepareSearchFilter(final ContextSearchRequest request, final Set<String> constants,
                    final ContextAttributeComparator comparator)
    {
        try
        {
            requestReadLock();
            final Stream<Pair<String, String>> constantAttributes = extractConstantValues(request, constants);
            final Collection<CachedContextImpl> ctx = this.contexts.get(request.getContextStack());
            final Stream<Context> constantContexts;
            if(ctx == null)
            {
                LOGGER.warn("Search filter creation for unknown context stack", new IllegalStateException(
                                "Context search was triggered on invalidated cache! Please make sure that timezones on server and database are aligned!"));
                constantContexts = Stream.empty();
            }
            else
            {
                constantContexts = getMatchingContexts(ctx, constantAttributes, comparator).filter(ctx::contains);
            }
            final Stream<Map.Entry<String, String>> attributes = extractAttributes(constantContexts);
            final Map<String, Set<String>> possibilities = attributes
                            .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toSet())));
            final var filter = new DefaultConfigurationSearchFilter(comparator);
            filter.addPossibleValues(possibilities);
            return filter;
        }
        finally
        {
            releaseReadLock();
        }
    }


    protected CachedContextImpl wrapContext(final CachedContextImpl parent, final Context context)
    {
        if(context instanceof CachedContextImpl)
        {
            return (CachedContextImpl)context;
        }
        else if(context != null)
        {
            var wrapper = new CachedContextImpl(context);
            wrapper.setParentContext(parent);
            return wrapper;
        }
        else
        {
            return null;
        }
    }


    protected CachedContextImpl wrapContext(final Context context)
    {
        if(context == null)
        {
            return null;
        }
        else
        {
            return wrapContext(wrapContext(context.getParentContext()), context);
        }
    }


    protected class CachedContextImpl extends AbstractCachedContext
    {
        private Map<String, String> attributes;


        public CachedContextImpl(final Context original)
        {
            super(original);
        }


        @Override
        public List<Context> getContext()
        {
            return new CachedContextList(this, getOriginal());
        }


        @Override
        public boolean equals(final Object o)
        {
            return o != null && (o.getClass() == this.getClass()) && ((AbstractCachedContext)o).getOriginal() == this.getOriginal();
        }


        @Override
        public int hashCode()
        {
            return System.identityHashCode(this.getOriginal());
        }
    }


    protected class CachedContextList extends AbstractCachedContextList
    {
        public CachedContextList(final CachedContextImpl cachedContext, final Context original)
        {
            super(cachedContext, original);
        }


        protected AbstractCachedContext wrapCachedContext(final AbstractCachedContext parent, final Context context)
        {
            return wrapContext((CachedContextImpl)parent, context);
        }
    }
}
