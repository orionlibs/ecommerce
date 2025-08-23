package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;
import org.apache.commons.lang3.tuple.Pair;

public class Schedule implements Iterable<ModelWrapper>
{
    private final List<ModelWrapper> schedule;
    private final Set<Pair<Object, PersistenceOperation>> registry;


    public Schedule(int size)
    {
        this.schedule = new ArrayList<>(size);
        this.registry = new HashSet<>(size);
    }


    public void add(ModelWrapper wrapper)
    {
        this.schedule.add(wrapper);
        this.registry.add(Pair.of(wrapper.getModel(), wrapper.getOperationToPerform()));
    }


    public boolean contains(ModelWrapper wrapper)
    {
        return containsWrapperFor(wrapper.getModel(), wrapper.getOperationToPerform());
    }


    public Iterator<ModelWrapper> iterator()
    {
        return UnmodifiableIterator.unmodifiableIterator(this.schedule.iterator());
    }


    public boolean containsWrapperFor(Object model, PersistenceOperation operation)
    {
        return this.registry.contains(Pair.of(model, operation));
    }
}
