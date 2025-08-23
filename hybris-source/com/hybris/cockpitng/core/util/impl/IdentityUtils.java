/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.impl;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Set of tools that allows to identify instance duplicates in different collection-related objects.
 */
public final class IdentityUtils
{
    private IdentityUtils()
    {
        //Utility class
    }


    /**
     * Iterates over provided collection and removes all duplicated instances.
     *
     * @param collection
     *           collection to be cleared from duplicates
     * @param <E>
     *           element type
     */
    public static <E> void removeDuplicates(final Collection<E> collection)
    {
        final DistinctFilter distinctFilter = new DistinctFilter(collection.size());
        collection.removeIf(e -> !distinctFilter.test(e));
    }


    /**
     * Creates a collector for stream to return a collection with no duplicated instance.
     *
     * @see com.hybris.cockpitng.core.util.impl.IdentityUtils.DistinctCollection
     * @param <E>
     *           element type
     * @return collector
     */
    public static <E> Collector<E, ?, Collection<E>> distinctCollector()
    {
        return Collectors.toCollection(DistinctCollection::new);
    }


    /**
     * Creates a collector for stream to return a collection with no duplicated instance.
     *
     * @see com.hybris.cockpitng.core.util.impl.IdentityUtils.DistinctCollection
     * @param initialCapacity
     *           collection initial capacity
     * @param <E>
     *           element type
     * @return collector
     */
    public static <E> Collector<E, ?, Collection<E>> distinctCollector(final int initialCapacity)
    {
        return Collectors.toCollection(() -> new DistinctCollection<>(initialCapacity));
    }


    /**
     * An object that wraps any other in the way, that {@link #equals(Object)} and {@link #hashCode()} depends only on
     * wrapped object's instance.
     * <P>
     * In other words two wrappers are considered as equal only if they wrap the same instance.
     *
     * @param <E>
     *           wrapped element's type
     */
    public static class Wrapper<E>
    {
        private final E element;


        public Wrapper(final E element)
        {
            this.element = element;
        }


        public E getElement()
        {
            return element;
        }


        @Override
        public boolean equals(final Object obj)
        {
            return obj != null && (obj.getClass() == this.getClass()) && ((Wrapper)obj).element == this.element;
        }


        @Override
        public int hashCode()
        {
            return System.identityHashCode(element);
        }
    }


    /**
     * Iterator to iterate over elements while having iterator of their wrapped instances.
     *
     * @param <E>
     *           type of element
     */
    public static class WrapperIterator<E> implements Iterator<E>
    {
        private final Iterator<Wrapper<E>> iterator;


        public WrapperIterator(final Iterator<Wrapper<E>> iterator)
        {
            this.iterator = iterator;
        }


        @Override
        public boolean hasNext()
        {
            return iterator.hasNext();
        }


        @Override
        public E next()
        {
            return iterator.next().element;
        }


        @Override
        public void remove()
        {
            iterator.remove();
        }
    }


    /**
     * Collection that contains only single copy of each instance.
     * <P>
     * Collection is implemented in the way, that it may be altered in whatever way is needed and no duplicates are being
     * identified in the process. First read from collection after it has been changed triggers duplicates removal.
     *
     * @param <E>
     *           type of elements
     */
    public static class DistinctCollection<E> extends AbstractCollection<E>
    {
        private final Set<Wrapper<E>> elements;


        public DistinctCollection()
        {
            elements = new LinkedHashSet<>();
        }


        public DistinctCollection(final int initialCapacity)
        {
            elements = new LinkedHashSet<>(initialCapacity);
        }


        public DistinctCollection(final Collection<E> initial)
        {
            elements = new LinkedHashSet<>(initial.size());
            initial.forEach(this::add);
        }


        @Override
        public boolean add(final E t)
        {
            return elements.add(new Wrapper<>(t));
        }


        @Override
        public boolean remove(final Object o)
        {
            return elements.remove(new Wrapper(o));
        }


        @Override
        public void clear()
        {
            elements.clear();
        }


        @Override
        public Iterator<E> iterator()
        {
            return new WrapperIterator<>(elements.iterator());
        }


        @Override
        public int size()
        {
            return elements.size();
        }


        @Override
        public boolean contains(final Object o)
        {
            return elements.contains(new Wrapper(o));
        }
    }


    /**
     * Filter that returns true only for the first time specified element is tested. It may be used in
     * {@link java.util.stream.Stream#filter(Predicate)} to get a distinct stream in context of instance.
     *
     * @param <E>
     *           type of elements to filter
     */
    public static class DistinctFilter<E> implements Predicate<E>
    {
        // The value to represent null in the Map
        private static final Wrapper NULL_VALUE = new Wrapper(null);
        private final Map<Wrapper<E>, Boolean> seen;


        public DistinctFilter()
        {
            this.seen = Collections.synchronizedMap(new HashMap<>());
        }


        public DistinctFilter(final int maxSize)
        {
            this.seen = Collections.synchronizedMap(new HashMap<>(maxSize));
        }


        @Override
        public boolean test(final E e)
        {
            return seen.putIfAbsent(map(e), Boolean.TRUE) == null;
        }


        private Wrapper<E> map(final E t)
        {
            return t != null ? new Wrapper<>(t) : NULL_VALUE;
        }
    }


    /**
     * A wrapping spliterator that only reports distinct elements instances of the underlying spliterator. Does not preserve
     * size and encounter order.
     */
    public static final class DistinctSpliterator<T> implements Spliterator<T>, Consumer<T>
    {
        // The value to represent null in the ConcurrentHashMap
        private static final Object NULL_VALUE = new Object();
        // The underlying spliterator
        private final Spliterator<T> s;
        // ConcurrentHashMap holding distinct elements as keys
        private final Map<T, Boolean> seen;
        // Temporary element, only used with tryAdvance
        private T tmpSlot;


        private DistinctSpliterator(final Spliterator<T> s, final Map<T, Boolean> seen)
        {
            this.s = s;
            this.seen = Collections.synchronizedMap(seen);
        }


        @Override
        public void accept(final T t)
        {
            this.tmpSlot = t;
        }


        private T mapNull(final T t)
        {
            return t != null ? t : (T)NULL_VALUE;
        }


        @Override
        public boolean tryAdvance(final Consumer<? super T> action)
        {
            while(s.tryAdvance(this))
            {
                if(seen.putIfAbsent(mapNull(tmpSlot), Boolean.TRUE) == null)
                {
                    action.accept(tmpSlot);
                    tmpSlot = null;
                    return true;
                }
            }
            return false;
        }


        @Override
        public void forEachRemaining(final Consumer<? super T> action)
        {
            s.forEachRemaining(t -> {
                if(seen.putIfAbsent(mapNull(t), Boolean.TRUE) == null)
                {
                    action.accept(t);
                }
            });
        }


        @Override
        public Spliterator<T> trySplit()
        {
            final Spliterator<T> split = s.trySplit();
            return (split != null) ? new DistinctSpliterator<>(split, seen) : null;
        }


        @Override
        public long estimateSize()
        {
            return s.estimateSize();
        }


        @Override
        public int characteristics()
        {
            return (s.characteristics() & ~(Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.SORTED | Spliterator.ORDERED))
                            | Spliterator.DISTINCT;
        }


        @Override
        public Comparator<? super T> getComparator()
        {
            return s.getComparator();
        }
    }
}
