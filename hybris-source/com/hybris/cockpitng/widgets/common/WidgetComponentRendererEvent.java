/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common;

import java.util.function.Predicate;
import org.zkoss.zk.ui.Component;

/**
 * Event describing rendering some component
 */
public class WidgetComponentRendererEvent<P, C, D>
{
    private final Component source;
    private final NotifyingWidgetComponentRenderer<P, C, D> renderer;
    private WidgetComponentRendererEvent<?, ?, ?> cause;
    private P parent;
    private C config;
    private D data;


    /**
     * @param source
     *           component that has been rendered
     * @param renderer
     *           renderer that rendered the component
     * @deprecated since 6.5
     * @see #WidgetComponentRendererEvent(Component, NotifyingWidgetComponentRenderer)
     */
    @Deprecated(since = "6.5", forRemoval = true)
    public WidgetComponentRendererEvent(final Component source, final WidgetComponentRenderer<P, C, D> renderer)
    {
        this.source = source;
        this.renderer = renderer instanceof NotifyingWidgetComponentRenderer ? (NotifyingWidgetComponentRenderer<P, C, D>)renderer
                        : null;
    }


    /**
     * @param source
     *           component that has been rendered
     * @param renderer
     *           renderer that rendered the component
     */
    public WidgetComponentRendererEvent(final Component source, final NotifyingWidgetComponentRenderer<P, C, D> renderer)
    {
        this.source = source;
        this.renderer = renderer;
    }


    /**
     * Sets information kept by event
     *
     * @param parent
     *           container which children are being rendered
     * @param config
     *           renderer configuration
     * @param data
     *           data being rendered
     */
    public void setEventData(final P parent, final C config, final D data)
    {
        this.parent = parent;
        this.config = config;
        this.data = data;
    }


    /**
     * @return component that has been rendered
     */
    public Component getSource()
    {
        return source;
    }


    protected void initCause(final WidgetComponentRendererEvent<?, ?, ?> cause)
    {
        this.cause = cause;
    }


    /**
     * Gets rendering parameters of specific nested renderer.
     * <P>
     * Helpful when nested rendering is used (i.e. EditorAreaRenderer uses TabRenderer). In such situation it is possible
     * that all listeners of parent renderer (i.e. EditorArea's) wishes to be notified of rendering events of nested
     * renderer (i.e. TabRenderer). If any events are posted by wrapped renderer, then they are passed over to parent
     * renderer.
     * <P>
     * Parent listeners that are notified of nested rendering events gets information about particular component that has
     * been rendered and parent's configuration/data.
     *
     * @return nested renderer's event
     */
    public WidgetComponentRendererEvent<?, ?, ?> getCause()
    {
        return cause;
    }


    /**
     * Gets rendering parameters of specific nested renderer that was rendering configuration of specified type.
     * <P>
     * Helpful when nested rendering is used (i.e. EditorAreaRenderer uses TabRenderer). In such situation it is possible
     * that all listeners of parent renderer (i.e. EditorArea's) wishes to be notified of rendering events of nested
     * renderer (i.e. TabRenderer). If any events are posted by wrapped renderer, then they are passed over to parent
     * renderer.
     * <P>
     * Parent listeners that are notified of nested rendering events gets information about particular component that has
     * been rendered and parent's configuration/data.
     *
     * @param configClass
     *           class of a configuration which rendering event is to be found
     * @return nested renderer's event
     */
    public <PP, CC, DD> WidgetComponentRendererEvent<PP, CC, DD> getCause(final Class<? extends CC> configClass)
    {
        return getCause((event) -> configClass.isInstance(event.getConfig()));
    }


    /**
     * Gets rendering parameters of specific nested renderer that meets specified conditions.
     * <P>
     * Helpful when nested rendering is used (i.e. EditorAreaRenderer uses TabRenderer). In such situation it is possible
     * that all listeners of parent renderer (i.e. EditorArea's) wishes to be notified of rendering events of nested
     * renderer (i.e. TabRenderer). If any events are posted by wrapped renderer, then they are passed over to parent
     * renderer.
     * <P>
     * Parent listeners that are notified of nested rendering events gets information about particular component that has
     * been rendered and parent's configuration/data.
     *
     * @param condition
     *           condition that event needs to meet
     * @return nested renderer's event
     */
    public <PP, CC, DD> WidgetComponentRendererEvent<PP, CC, DD> getCause(final Predicate<WidgetComponentRendererEvent> condition)
    {
        WidgetComponentRendererEvent result = this;
        while(result != null && !condition.test(result))
        {
            result = result.getCause();
        }
        return result;
    }


    /**
     * @return container which children are being rendered
     */
    public P getParent()
    {
        return parent;
    }


    /**
     * @return renderer configuration
     */
    public C getConfig()
    {
        return config;
    }


    /**
     * @return data being rendered
     */
    public D getData()
    {
        return data;
    }


    /**
     * @return renderer which fired event
     */
    public NotifyingWidgetComponentRenderer<P, C, D> getRenderer()
    {
        return renderer;
    }


    /**
     * Checks whether this is final event of rendering. An event is considered to be final, if no other events concerning
     * {@link #getParent()} will be triggered. Also {@link #getParent()} may be considered as fully rendered.
     *
     * @return <code>true</code> if rendering has been finished
     */
    public boolean isFinal()
    {
        return source == parent;
    }
}
