/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions;

import static com.hybris.cockpitng.actions.AbstractStatefulAction.restoreModelValue;
import static com.hybris.cockpitng.actions.AbstractStatefulAction.storeModelValue;

import com.hybris.cockpitng.actions.impl.DefaultActionRenderer;
import com.hybris.cockpitng.components.Action;
import com.hybris.cockpitng.events.SocketEvent;
import com.hybris.cockpitng.util.WidgetControllers;
import java.util.function.Function;
import java.util.function.Supplier;
import org.zkoss.zk.ui.event.EventListener;

public abstract class AbstractStatefulActionRenderer<INPUT, OUTPUT> extends DefaultActionRenderer<INPUT, OUTPUT>
{
    /**
     * Reads a value from parent's widget model.
     * <P>
     * Method is resistant to situation when many instances of a particular action used in single parent widgets.
     *
     * @param actionContext action context
     * @param key value key
     * @param <V> value type
     * @return value found or <code>null</code>
     */
    protected <V> V getValue(final ActionContext<INPUT> actionContext, final String key)
    {
        return restoreModelValue(actionContext, key);
    }


    /**
     * Puts a value to parent's widget model.
     * <P>
     * Method is resistant to situation when many instances of a particular action used in single parent widgets.
     *
     * @param actionContext action context
     * @param key value key
     * @param value value to be put
     */
    protected void setValue(final ActionContext<INPUT> actionContext, final String key, final Object value)
    {
        storeModelValue(actionContext, key, value);
    }


    /**
     * Registers new socket listener on parent's widget, if it hasn't been done yet. Otherwise nothing happens. Messages
     * received through provided socket will be processed and stored under specified key.
     *
     * @param parent action component
     * @param context action context
     * @param listenerIdentity unique identity of listener in scope of action
     * @param socket name of socket to attach to
     * @param listenerSupplier a method creating new instance of listener
     * @see #initializeStateSocketListener(Action, String, EventListener)
     * @see #createStateSocketListener(Action, ActionContext, String, Function)
     */
    protected void addStateSocketListener(final Action parent, final ActionContext<INPUT> context, final String listenerIdentity,
                    final String socket, final Supplier<EventListener<SocketEvent>> listenerSupplier)
    {
        EventListener<SocketEvent> socketListener = (EventListener<SocketEvent>)parent.getAttribute(listenerIdentity);
        if(socketListener == null)
        {
            socketListener = listenerSupplier.get();
            socketListener = initializeStateSocketListener(parent, listenerIdentity, socketListener);
            WidgetControllers.addWidgetSocketListeners(parent.getWidgetInstanceManager().getWidgetslot(), socket, socketListener);
        }
    }


    /**
     * Initialized newly created listener.
     *
     * @param parent action component
     * @param listenerIdentity  unique identity of listener in scope of action
     * @param socketListener listener to be initialized
     * @return initialized listener that will be registered
     * @see #addStateSocketListener(Action, ActionContext, String, String, Supplier)
     * @see #createStateSocketListener(Action, ActionContext, String, Function)
     */
    protected EventListener<SocketEvent> initializeStateSocketListener(final Action parent, final String listenerIdentity,
                    final EventListener<SocketEvent> socketListener)
    {
        parent.setAttribute(listenerIdentity, socketListener);
        return socketListener;
    }


    /**
     * Creates new instance of socket listener.
     *
     * @param parent action component
     * @param context action context
     * @param key model key under which a processed input message will be kept
     * @param valueSupplier processing method
     * @return new instance of socket listener
     */
    protected EventListener<SocketEvent> createStateSocketListener(final Action parent, final ActionContext<INPUT> context,
                    final String key, final Function valueSupplier)
    {
        return event -> {
            setValue(context, key, valueSupplier.apply(event.getData()));
            parent.reload();
        };
    }
}
