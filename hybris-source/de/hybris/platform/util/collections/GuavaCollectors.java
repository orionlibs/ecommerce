package de.hybris.platform.util.collections;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.stream.Collector;

public final class GuavaCollectors
{
    public static <T> Collector<T, ImmutableList.Builder<T>, ImmutableList<T>> immutableList()
    {
        return Collector.of(com.google.common.collect.ImmutableList.Builder::new, ImmutableList.Builder::add, (l, r) -> l.addAll((Iterable)r.build()), ImmutableList.Builder::build, new Collector.Characteristics[0]);
    }


    public static <T> Collector<T, ImmutableSet.Builder<T>, ImmutableSet<T>> immutableSet()
    {
        return Collector.of(com.google.common.collect.ImmutableSet.Builder::new, ImmutableSet.Builder::add, (l, r) -> l.addAll((Iterable)r.build()), ImmutableSet.Builder::build, new Collector.Characteristics[0]);
    }
}
