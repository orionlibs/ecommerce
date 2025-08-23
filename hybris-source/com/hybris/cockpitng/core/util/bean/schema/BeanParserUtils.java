/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.bean.schema;

import static com.hybris.cockpitng.core.util.bean.schema.CockpitNamespaceHandler.NAMESPACE;

import java.util.AbstractList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Utilities class that contains methods useful for spring namespace
 * {@link org.springframework.beans.factory.xml.BeanDefinitionParser}.
 */
public final class BeanParserUtils
{
    private BeanParserUtils()
    {
        //Utility class
    }


    /**
     * Method for all situations matching the following flow:
     * <ul>
     * <li>check if value is present in {@link Optional} object</li>
     * <li>if value is present, then process it somehow</li>
     * <li>pass processed value to final consumer</li>
     * </ul>
     * <p>
     * Most common use case is - acquire a {@link Node}, get it's value if exists, parse it, change bean definition with
     * parse result.
     * </p>
     *
     * @param value
     *           value to be processed and consumed
     * @param mapper
     *           processing function
     * @param consumer
     *           final value consumer
     * @param <V>
     *           type of original value
     * @param <M>
     *           type of processed value
     */
    public static <V, M> void mapAndConsume(final Optional<V> value, final Function<V, M> mapper, final Consumer<M> consumer)
    {
        value.ifPresent(getConsumer(mapper, consumer));
    }


    /**
     * Method for all situations matching the following flow:
     * <ul>
     * <li>check if value is present in {@link Optional} object</li>
     * <li>if value is present, then process it somehow</li>
     * <li>check if process result meets some restrictions</li>
     * <li>pass processed value to final consumer</li>
     * </ul>
     * <p>
     * Most common use case is - acquire a {@link Node}, get it's value if exists, parse it, check some condition, change
     * bean definition with parse result if condition is satisfied.
     * </p>
     *
     * @param value
     *           value to be processed and consumed
     * @param mapper
     *           processing function
     * @param filter
     *           condition that needs to be satisfied by processed value for it to be consumed
     * @param consumer
     *           final value consumer
     * @param <V>
     *           type of original value
     * @param <M>
     *           type of processed value
     */
    public static <V, M> void mapAndConsume(final Optional<V> value, final Function<V, M> mapper, final Consumer<M> consumer,
                    final Predicate<M> filter)
    {
        value.ifPresent(getConsumer(mapper, consumer, filter));
    }


    /**
     * Creates a consumer that processes provided value and passes its result to provided consumer.
     *
     * @param mapper
     *           processing method
     * @param consumer
     *           final value consumer
     * @param <V>
     *           type of original value
     * @param <M>
     *           type of processed value
     * @return consumer
     */
    public static <V, M> Consumer<V> getConsumer(final Function<V, M> mapper, final Consumer<M> consumer)
    {
        return getConsumer(mapper, consumer, null);
    }


    /**
     * Creates a consumer that processes provided value and passes its result to provided consumer if it matches provided
     * restrictions.
     *
     * @param mapper
     *           processing method
     * @param consumer
     *           final value consumer
     * @param filter
     *           condition that needs to be satisfied by processed value for it to be consumed
     * @param <V>
     *           type of original value
     * @param <M>
     *           type of processed value
     * @return consumer
     */
    public static <V, M> Consumer<V> getConsumer(final Function<V, M> mapper, final Consumer<M> consumer,
                    final Predicate<M> filter)
    {
        return value -> {
            final M mappedValue = mapper.apply(value);
            if(filter == null || filter.test(mappedValue))
            {
                consumer.accept(mappedValue);
            }
        };
    }


    /**
     * Gets an attribute's value if it exists.
     *
     * @param node
     *           element which attribute is to be read
     * @param attributeName
     *           name of attribute to read
     * @return a value of attribute or <code>empty</code> if not available
     */
    public static Optional<String> getAttributeValue(final Node node, final String attributeName)
    {
        if(node.hasAttributes())
        {
            final Node attribute = node.getAttributes().getNamedItem(attributeName);
            if(attribute != null)
            {
                return Optional.of(attribute.getNodeValue());
            }
        }
        return Optional.empty();
    }


    /**
     * Gets first child element of specified name if it exists.
     *
     * @param parent
     *           parent element
     * @param nodeName
     *           name of child
     * @return a child element or <code>empty</code> if not available
     */
    public static Optional<Element> getFirstChild(final Element parent, final String nodeName)
    {
        return getChild(parent, nodeName, 0);
    }


    /**
     * Gets a child element of specified name if it exists.
     *
     * @param parent
     *           parent element
     * @param nodeName
     *           name of child
     * @param index
     *           index of a child among all children of this name
     * @return a child element or <code>empty</code> if not available
     */
    public static Optional<Element> getChild(final Element parent, final String nodeName, final int index)
    {
        final NodeList children = parent.getElementsByTagNameNS(NAMESPACE, nodeName);
        if(children != null && children.getLength() > index)
        {
            return Optional.of((Element)children.item(index));
        }
        else
        {
            return Optional.empty();
        }
    }


    /**
     * Gets all children from specified parent element.
     *
     * @param parent
     *           parent element
     * @return list of children
     */
    public static List<Node> getChildrenNodes(final Element parent)
    {
        return new NodeIterableList<>(parent.getChildNodes());
    }


    /**
     * Gets all children from specified element of provided name.
     *
     * @param parent
     *           parent element
     * @param nodeName
     *           name of children to be found
     * @return list of children
     */
    public static List<Element> getChildren(final Element parent, final String nodeName)
    {
        final NodeList children = parent.getElementsByTagNameNS(NAMESPACE, nodeName);
        if(children != null)
        {
            return new NodeIterableList<>(children);
        }
        else
        {
            return Collections.emptyList();
        }
    }


    /**
     * Gets all children of type {@link Element} from specified parent element.
     *
     * @param parent
     *           parent element
     * @return list of children
     */
    public static Iterable<Element> getChildren(final Element parent)
    {
        return new NodeIterable<>(parent.getChildNodes(), e -> e instanceof Element);
    }


    /**
     * Gets all children from specified parent element that satisfies provided condition.
     *
     * @param parent
     *           parent element
     * @param filter
     *           condition that must be satisfied by child node
     * @return list of children
     */
    public static Iterable<Node> getChildren(final Element parent, final Predicate<Node> filter)
    {
        return new NodeIterable<>(parent.getChildNodes(), e -> e instanceof Element);
    }


    protected static class NodeIterableList<E extends Node> extends AbstractList<E>
    {
        private final NodeList nodes;


        public NodeIterableList(final NodeList nodes)
        {
            this.nodes = nodes;
        }


        @Override
        public E get(final int index)
        {
            return (E)nodes.item(index);
        }


        @Override
        public int size()
        {
            return nodes.getLength();
        }


        @Override
        public boolean equals(final Object o)
        {
            if(this == o)
            {
                return true;
            }
            if(o == null)
            {
                return false;
            }
            if(o.getClass() != this.getClass())
            {
                return false;
            }
            if(!super.equals(o))
            {
                return false;
            }
            final NodeIterableList<?> that = (NodeIterableList<?>)o;
            return nodes != null ? nodes.equals(that.nodes) : that.nodes == null;
        }


        @Override
        public int hashCode()
        {
            int result = super.hashCode();
            result = 31 * result + (nodes != null ? nodes.hashCode() : 0);
            return result;
        }
    }


    protected static class NodeIterable<E extends Node> implements Iterable<E>
    {
        private final NodeList nodes;
        private final Predicate<Node> filter;


        public NodeIterable(final NodeList nodes, final Predicate<Node> filter)
        {
            this.nodes = nodes;
            this.filter = filter;
        }


        @Override
        public Iterator<E> iterator()
        {
            return new NodeIterator<>(nodes, filter);
        }
    }


    protected static class NodeIterator<E extends Node> implements Iterator<E>
    {
        private final NodeList nodes;
        private final Predicate<Node> filter;
        private int index;


        public NodeIterator(final NodeList nodes, final Predicate<Node> filter)
        {
            this.nodes = nodes;
            this.filter = filter;
            index = calculateNextIndex();
        }


        @Override
        public boolean hasNext()
        {
            return index < nodes.getLength();
        }


        @Override
        public E next()
        {
            if(!hasNext())
            {
                throw new NoSuchElementException();
            }
            final E result = (E)nodes.item(index);
            index = calculateNextIndex();
            return result;
        }


        private int calculateNextIndex()
        {
            int result = index + 1;
            while(result < nodes.getLength() && !filter.test(nodes.item(result)))
            {
                result++;
            }
            return result;
        }
    }
}
