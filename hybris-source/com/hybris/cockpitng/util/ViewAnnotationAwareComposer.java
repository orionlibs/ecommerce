/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.annotations.ViewEvents;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.GenericForwardComposer;

public class ViewAnnotationAwareComposer extends GenericForwardComposer<Component>
{
    private static final long serialVersionUID = -8566830035669220293L;
    private static final Logger LOG = LoggerFactory.getLogger(ViewAnnotationAwareComposer.class);
    private static final String ATTRIBUTE_VIEW_EVENTS_SCANNED = "viewEventsScanned";


    @Override
    public void doAfterCompose(final Component comp) throws Exception
    {
        super.doAfterCompose(comp);
        scanEventListeners(comp);
    }


    protected void scanEventListeners(final Component parent)
    {
        final Method[] methods = getClass().getMethods();
        final Set<String> newlyScanned = new HashSet<>();
        final Set<String> scanned = new HashSet<>();
        if(parent.hasAttribute(ATTRIBUTE_VIEW_EVENTS_SCANNED))
        {
            scanned.addAll(
                            Arrays.asList(StringUtils.split(ObjectUtils.toString(parent.getAttribute(ATTRIBUTE_VIEW_EVENTS_SCANNED)), ",")));
        }
        for(final Method method : methods)
        {
            final ViewEvent viewAnnotation = method.getAnnotation(ViewEvent.class);
            if(viewAnnotation != null)
            {
                final boolean alreadyAdded = !StringUtils.isEmpty(viewAnnotation.componentID())
                                && scanned.contains(viewAnnotation.componentID());
                if(!alreadyAdded && applyEventListener(parent, method, viewAnnotation)
                                && !StringUtils.isEmpty(viewAnnotation.componentID()))
                {
                    newlyScanned.add(viewAnnotation.componentID());
                }
            }
            final ViewEvents viewAnnotations = method.getAnnotation(ViewEvents.class);
            if(viewAnnotations != null && viewAnnotations.value() != null)
            {
                for(final ViewEvent ann : viewAnnotations.value())
                {
                    final boolean alreadyAdded = !StringUtils.isEmpty(ann.componentID()) && scanned.contains(ann.componentID());
                    if(!alreadyAdded && applyEventListener(parent, method, ann) && !StringUtils.isEmpty(ann.componentID()))
                    {
                        newlyScanned.add(ann.componentID());
                    }
                }
            }
        }
        scanned.addAll(newlyScanned);
        parent.setAttribute(ATTRIBUTE_VIEW_EVENTS_SCANNED, StringUtils.join(scanned, ","));
    }


    /**
     * Override to intercept listener calls
     *
     * @param method
     * @param event
     * @param viewAnnotation
     *
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    protected void invokeListenerMethod(final Method method, final Event event, final ViewEvent viewAnnotation)
                    throws IllegalAccessException, InvocationTargetException
    {
        if(event == null)
        {
            method.invoke(ViewAnnotationAwareComposer.this);
        }
        else
        {
            method.invoke(ViewAnnotationAwareComposer.this, event);
        }
    }


    private boolean applyEventListener(final Component parent, final Method method, final ViewEvent viewAnnotation)
    {
        final Class[] parameterTypes = method.getParameterTypes();
        boolean result = false;
        if(parameterTypes != null)
        {
            final EventListener<Event> eventListener;
            if(ArrayUtils.isEmpty(parameterTypes))
            {
                eventListener = event -> invokeListenerMethod(method, null, viewAnnotation);
            }
            else if(Event.class.equals(method.getParameterTypes()[0]))
            {
                eventListener = event -> invokeListenerMethod(method, event, viewAnnotation);
            }
            else if(Event.class.isAssignableFrom(method.getParameterTypes()[0]))
            {
                eventListener = event -> {
                    if(event != null && method.getParameterTypes()[0].isAssignableFrom(event.getClass()))
                    {
                        invokeListenerMethod(method, event, viewAnnotation);
                    }
                    else
                    {
                        LOG.warn("Event class not compatible, expected an instance of '{}' but got '{}'.",
                                        method.getParameterTypes()[0], event);
                    }
                };
            }
            else
            {
                eventListener = null;
            }
            if(eventListener != null && StringUtils.isNotBlank(viewAnnotation.componentID()))
            {
                final String anID = String.format("#%s", viewAnnotation.componentID());
                final List<Component> fellows = Selectors.find(parent, anID);
                if(CollectionUtils.isEmpty(fellows))
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Could not apply forward event, no component with id '" + viewAnnotation.componentID()
                                        + "' has been found.");
                    }
                }
                else
                {
                    fellows.forEach(component -> component.addEventListener(viewAnnotation.eventName(), eventListener));
                    result = true;
                }
            }
            else if(eventListener != null)
            {
                parent.addEventListener(viewAnnotation.eventName(), eventListener);
                result = true;
            }
        }
        return result;
    }
}
