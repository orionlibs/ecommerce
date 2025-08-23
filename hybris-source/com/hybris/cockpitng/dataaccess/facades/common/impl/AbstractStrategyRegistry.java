/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.common.impl;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.core.OrderComparator;

/**
 * Utility class for registering strategies in spring.
 */
public abstract class AbstractStrategyRegistry<S, T>
{
    private S defaultStrategy;
    private List<S> strategies;


    public Optional<List<S>> getStrategies()
    {
        return Optional.ofNullable(strategies);
    }


    public void setStrategies(final List<S> strategies)
    {
        this.strategies = strategies;
        if(strategies != null)
        {
            OrderComparator.sort(strategies);
        }
    }


    public S getDefaultStrategy()
    {
        return defaultStrategy;
    }


    public void setDefaultStrategy(final S defaultStrategy)
    {
        this.defaultStrategy = defaultStrategy;
    }


    public abstract boolean canHandle(S strategy, T context);


    public boolean canHandle(final S strategy, final T context, final Context additionalContext)
    {
        return canHandle(strategy, context);
    }


    public S getStrategy(final T context, final Context additionalContext)
    {
        final Predicate<S> canHandle = strategy -> canHandle(strategy, context, additionalContext);
        final Optional<S> pref = findPreferredStrategies(context, additionalContext).stream().filter(canHandle).findFirst();
        if(pref.isPresent())
        {
            return pref.get();
        }
        final Optional<S> orderedStrategies = getOrderedStrategies(context, additionalContext).stream().filter(canHandle)
                        .findFirst();
        return orderedStrategies.orElseGet(() -> findDefaultStrategy(context, additionalContext).filter(canHandle).orElse(null));
    }


    protected Optional<S> findDefaultStrategy(final T context, final Context additionalContext)
    {
        return Optional.ofNullable(defaultStrategy);
    }


    /**
     * @deprecated since 6.6, use #getOrderedStrategies(T, Context) instead
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected Stream<Optional<S>> findOrderedStrategies(final T context, final Context additionalContext)
    {
        final Optional<List<S>> strategyList = getStrategies();
        return strategyList.map(s -> s.stream().map(Optional::of)).orElseGet(() -> Stream.of(Optional.empty()));
    }


    protected List<S> getOrderedStrategies(final T context, final Context additionalContext)
    {
        return findOrderedStrategies(context, additionalContext).filter(Optional::isPresent).map(Optional::get)
                        .collect(Collectors.toList());
    }


    /**
     * @deprecated since 6.6, use #findPreferredStrategies(T, Context) instead
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected Optional<S> findPreferredStrategy(final T context, final Context additionalContext)
    {
        return Optional.empty();
    }


    protected List<S> findPreferredStrategies(final T context, final Context additionalContext)
    {
        final Optional<S> preferredStrategy = findPreferredStrategy(context, additionalContext);
        return preferredStrategy.isPresent() ? Lists.newArrayList(preferredStrategy.get()) : Collections.emptyList();
    }


    public S getStrategy(final T context)
    {
        return getStrategy(context, new DefaultContext());
    }
}
