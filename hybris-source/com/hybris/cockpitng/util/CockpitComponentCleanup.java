/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.core.Cleanable;
import com.hybris.cockpitng.core.Initializable;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.ShadowElement;
import org.zkoss.zk.ui.util.DesktopCleanup;
import org.zkoss.zk.ui.util.UiLifeCycle;

/**
 * Calls {@link Cleanable#cleanup()} for each component detached. Calls {@link Initializable#initialize()} for each
 * component attached.
 */
public class CockpitComponentCleanup implements UiLifeCycle, DesktopCleanup
{
    private static final String ATTRIBUTE_CLEANUP_CALLBACK = "cleanupCallback";
    private static final String ATTRIBUTE_INIT_CALLBACK = "initCallback";


    /**
     * Registers a cleanup that should be performed after specified component is detached.
     * <P>
     * Cleanup is a one-time event and all cleanups will get unregistered after component is detached. This means that,
     * if component it to be attached and detached multiple times, then a a cleanup needs to be registered every time
     * component is attached.
     *
     * @param component
     *           triggering component
     * @param cleanup
     *           cleanup to be performed
     */
    public static void registerCleanable(final Component component, final Cleanable cleanup)
    {
        List<Cleanable> cleanableList = (List<Cleanable>)component.getAttribute(ATTRIBUTE_CLEANUP_CALLBACK);
        if(cleanableList == null)
        {
            cleanableList = new ArrayList<>();
            component.setAttribute(ATTRIBUTE_CLEANUP_CALLBACK, cleanableList);
        }
        cleanableList.add(cleanup);
    }


    /**
     * Unregisters a cleanup that should not be performed after specified component is detached.
     *
     * @param component
     *           triggering component
     * @param cleanup
     *           cleanup to unregister
     */
    public static void unregisterCleanable(final Component component, final Cleanable cleanup)
    {
        final List<Cleanable> cleanableList = (List<Cleanable>)component.getAttribute(ATTRIBUTE_CLEANUP_CALLBACK);
        if(cleanableList != null)
        {
            cleanableList.remove(cleanup);
        }
    }


    /**
     * Registers a initialize that should be performed before specified component is displayed.
     * <P>
     * Initialization is a one-time event and all setups will get unregistered after component is displayed. This means
     * that, if component it to be attached and detached multiple times, then a initialize needs to be registered every
     * time component is detached.
     *
     * @param component
     *           triggering component
     * @param initialize
     *           cleanup to be performed
     */
    public static void registerInitializable(final Component component, final Initializable initialize)
    {
        List<Initializable> initializableList = (List<Initializable>)component.getAttribute(ATTRIBUTE_INIT_CALLBACK);
        if(initializableList == null)
        {
            initializableList = new ArrayList<>();
            component.setAttribute(ATTRIBUTE_INIT_CALLBACK, initializableList);
        }
        initializableList.add(initialize);
    }


    /**
     * Calls {@link Initializable#initialize()} for provided component and all {@link Initializable} registered for it.
     * Method is invoked for whole tree of components children.
     *
     * @param component
     *           component to be initialized
     */
    public static void initialize(final Component component)
    {
        initialize(component, true);
    }


    /**
     * Calls {@link Initializable#initialize()} for provided component and all {@link Initializable} registered for it.
     * Method may be invoked for whole tree of components children, depending on value of <code>nested</code>.
     *
     * @param component
     *           component to be initialized
     * @param nested
     *           if <code>false</code> then only provided component will be initialized, otherwise whole tree
     */
    public static void initialize(final Component component, final boolean nested)
    {
        if(component instanceof Initializable)
        {
            ((Initializable)component).initialize();
        }
        final List<Initializable> initializableList = (List<Initializable>)component.removeAttribute(ATTRIBUTE_INIT_CALLBACK);
        if(initializableList != null)
        {
            initializableList.forEach(Initializable::initialize);
            initializableList.clear();
        }
        if(nested)
        {
            component.getChildren().forEach(cmp -> initialize(cmp, nested));
        }
    }


    /**
     * Calls {@link Cleanable#cleanup()} for provided component and all {@link Cleanable} registered for it. Method is
     * invoked for whole tree of components children.
     *
     * @param component
     *           component to be cleaned
     */
    public static void cleanup(final Component component)
    {
        cleanup(component, true);
    }


    /**
     * Calls {@link Cleanable#cleanup()} for provided component and all {@link Cleanable} registered for it. Method may
     * be invoked for whole tree of components children, depending on value of <code>nested</code>.
     *
     * @param component
     *           component to be cleaned
     * @param nested
     *           if <code>false</code> then only provided component will be cleaned, otherwise whole tree
     */
    public static void cleanup(final Component component, final boolean nested)
    {
        if(component instanceof Cleanable)
        {
            ((Cleanable)component).cleanup();
        }
        final List<Cleanable> cleanableList = (List<Cleanable>)component.removeAttribute(ATTRIBUTE_CLEANUP_CALLBACK);
        if(cleanableList != null)
        {
            cleanableList.forEach(Cleanable::cleanup);
            cleanableList.clear();
        }
        if(nested)
        {
            component.getChildren().forEach(cmp -> cleanup(cmp, nested));
        }
    }


    @Override
    public void afterShadowAttached(final ShadowElement shadowElement, final Component component)
    {
        // not implemented
    }


    @Override
    public void afterShadowDetached(final ShadowElement shadowElement, final Component component)
    {
        // not implemented
    }


    @Override
    public void afterComponentAttached(final Component component, final Page page)
    {
        initialize(component, true);
    }


    @Override
    public void afterComponentDetached(final Component component, final Page page)
    {
        cleanup(component, true);
    }


    @Override
    public void afterComponentMoved(final Component component, final Component component1, final Component component2)
    {
        // not implemented
    }


    @Override
    public void afterPageAttached(final Page page, final Desktop desktop)
    {
        desktop.getComponents().forEach(cmp -> initialize(cmp, false));
    }


    @Override
    public void afterPageDetached(final Page page, final Desktop desktop)
    {
        desktop.getComponents().forEach(cmp -> cleanup(cmp, false));
    }


    @Override
    public void cleanup(final Desktop desktop) throws Exception
    {
        desktop.getComponents().forEach(cmp -> cleanup(cmp, false));
    }
}