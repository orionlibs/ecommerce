/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.table.iterator;

import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;

public class DefaultTableComponentIterator<E extends Component> implements TableComponentIterator<E>
{
    private final AbstractComponent parent;
    private final Supplier<E> newElementSupplier;
    private final ListIterator<E> iterator;
    private E current;


    public DefaultTableComponentIterator(final AbstractComponent parent, final Class<? extends E> elementType,
                    final Supplier<E> newElementSupplier)
    {
        this.parent = parent;
        this.newElementSupplier = newElementSupplier;
        this.iterator = parent.getChildren().stream().filter(child -> elementType.isAssignableFrom(child.getClass()))
                        .map(child -> (E)child).collect(Collectors.toList()).listIterator();
    }


    protected E createNewElement()
    {
        return newElementSupplier.get();
    }


    public E request()
    {
        if(!hasNext())
        {
            current = createNewElement();
            iterator.add(current);
            parent.appendChild(current);
            return current;
        }
        else
        {
            current = next();
            return current;
        }
    }


    @Override
    public boolean hasNext()
    {
        return iterator.hasNext();
    }


    @Override
    public E next()
    {
        return iterator.next();
    }


    @Override
    public void remove()
    {
        iterator.remove();
        parent.removeChild(current);
    }


    @Override
    public void forEachRemaining(final Consumer<? super E> action)
    {
        iterator.forEachRemaining(action);
    }


    public void removeRemaining()
    {
        forEachRemaining(parent::removeChild);
    }
}
