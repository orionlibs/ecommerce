/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.impl;

import com.hybris.cockpitng.core.model.Identifiable;
import com.hybris.cockpitng.util.ComponentMarkingUtils;
import com.hybris.cockpitng.util.MarkedEventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

public class DefaultComponentMarkingUtils implements ComponentMarkingUtils
{
    private static final String ATTRIBUTE_MARKED_COMPONENTS = "marked-components";
    private static final String ATTRIBUTE_PREFIX_MARK = "mark-";
    private static final String ATTRIBUTE_PREFIX_MARK_DATA = ATTRIBUTE_PREFIX_MARK + "data-";
    private static final String ATTRIBUTE_PREFIX_MARK_LISTENERS = ATTRIBUTE_PREFIX_MARK + "listeners-";


    @Override
    public void markComponent(final Component parent, final Component component, final String markName)
    {
        if(parent.getAttribute(ATTRIBUTE_MARKED_COMPONENTS) == null)
        {
            parent.setAttribute(ATTRIBUTE_MARKED_COMPONENTS, new HashMap<String, Set<Component>>());
        }
        final Map<String, Set<Component>> markedComponents = (Map<String, Set<Component>>)parent
                        .getAttribute(ATTRIBUTE_MARKED_COMPONENTS);
        markedComponents.computeIfAbsent(ATTRIBUTE_PREFIX_MARK + markName, k -> new HashSet<>());
        markedComponents.get(ATTRIBUTE_PREFIX_MARK + markName).add(component);
    }


    @Override
    public void markComponent(final Component parent, final Component component, final String markName, final Object markData)
    {
        component.setAttribute(ATTRIBUTE_PREFIX_MARK_DATA + markName, markData);
        markComponent(parent, component, markName);
    }


    @Override
    public void registerMarkedComponentsListener(final Component parent, final String markName, final String eventName,
                    final MarkedEventListener listener)
    {
        final Map<String, Set<Component>> markedComponents = (Map<String, Set<Component>>)parent
                        .getAttribute(ATTRIBUTE_MARKED_COMPONENTS);
        if(MapUtils.isNotEmpty(markedComponents))
        {
            final Set<Component> components = markedComponents.get(ATTRIBUTE_PREFIX_MARK + markName);
            if(CollectionUtils.isNotEmpty(components))
            {
                components.forEach(component -> handleMarkedComponentListener(component, markName, eventName, listener));
            }
        }
    }


    protected void handleMarkedComponentListener(final Component markedComponent, final String markName, final String eventName,
                    final MarkedEventListener listener)
    {
        Set<EventListener<Event>> currentListeners = (Set<EventListener<Event>>)markedComponent
                        .getAttribute(ATTRIBUTE_PREFIX_MARK_LISTENERS + markName);
        if(currentListeners != null && (listener instanceof Identifiable))
        {
            final Object listenerId = ((Identifiable)listener).getId();
            getListenersToRemove(currentListeners, listenerId)
                            .forEach(listenerToRemove -> markedComponent.removeEventListener(eventName, listenerToRemove));
        }
        else
        {
            currentListeners = new HashSet<>();
        }
        final Listener eventListener = getEventListener(listener, markName);
        markedComponent.addEventListener(eventName, eventListener);
        currentListeners.add(eventListener);
        markedComponent.setAttribute(ATTRIBUTE_PREFIX_MARK_LISTENERS + markName, currentListeners);
    }


    protected Set<EventListener> getListenersToRemove(final Set<EventListener<Event>> eventListeners, final Object newListenerId)
    {
        return eventListeners.stream().filter(listener -> isListenerToRemove(listener, newListenerId)).collect(Collectors.toSet());
    }


    protected boolean isListenerToRemove(final EventListener<Event> eventListener, final Object newListenerId)
    {
        if(eventListener instanceof Identifiable)
        {
            final Identifiable identifiableListener = (Identifiable)eventListener;
            return identifiableListener.getId().equals(newListenerId);
        }
        return true;
    }


    protected Listener getEventListener(final MarkedEventListener listener, final String markName)
    {
        return new Listener(markName, listener);
    }


    @Override
    public void moveMarkedComponents(final Component source, final Component target)
    {
        final Map<String, Set<Component>> sourceMarkedComponents = (Map<String, Set<Component>>)source
                        .getAttribute(ATTRIBUTE_MARKED_COMPONENTS);
        if(MapUtils.isNotEmpty(sourceMarkedComponents))
        {
            final Map<String, Set<Component>> targetMarkedComponents = (Map<String, Set<Component>>)target
                            .getAttribute(ATTRIBUTE_MARKED_COMPONENTS);
            if(MapUtils.isEmpty(targetMarkedComponents))
            {
                target.setAttribute(ATTRIBUTE_MARKED_COMPONENTS, sourceMarkedComponents);
            }
            else
            {
                sourceMarkedComponents.forEach((key, value) -> targetMarkedComponents.merge(key, value, this::mergeComponentSets));
            }
        }
        source.removeAttribute(ATTRIBUTE_MARKED_COMPONENTS);
    }


    protected Set<Component> mergeComponentSets(final Set<Component> first, final Set<Component> second)
    {
        final Set<Component> result = new HashSet<>(first);
        result.addAll(second);
        return result;
    }


    /**
     * @deprecated since 1905, use {@link Listener} instead
     */
    @Deprecated(since = "1905", forRemoval = true)
    protected static class IdentifiableEventListener extends Listener
    {
        protected IdentifiableEventListener(final String markName, final MarkedEventListener listener)
        {
            super(markName, listener);
        }
    }


    protected static class Listener implements com.hybris.cockpitng.util.IdentifiableEventListener
    {
        private final String markName;
        private final MarkedEventListener listener;
        private final Object id;


        protected Listener(final String markName, final MarkedEventListener listener)
        {
            this.markName = markName;
            this.listener = listener;
            if(listener instanceof Identifiable)
            {
                id = ((Identifiable)listener).getId();
            }
            else
            {
                id = UUID.randomUUID();
            }
        }


        @Override
        public Object getId()
        {
            return id;
        }


        @Override
        public void onEvent(final Event event)
        {
            final Object markData = event.getTarget().getAttribute(ATTRIBUTE_PREFIX_MARK_DATA + markName);
            listener.onEvent(event, markData);
        }
    }
}
