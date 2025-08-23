/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import org.zkoss.zk.ui.Component;

public interface ComponentMarkingUtils
{
    /**
     * Whenever a component created by renderer that widget controller is not aware of, but there is a logic related with
     * it, a renderer may mark it, so that then controller may assign a logic to it. This method should mark component
     * appropriately.
     *
     * @param parent
     *           parent component which will get a data structure of all marked components for every mark; it should be the
     *           same object as the one passed as parent to render method
     * @param component
     *           component to be marked
     * @param markName
     *           name of the mark - this value should be known by controller
     * @see #registerMarkedComponentsListener(Component, String, String, MarkedEventListener)
     */
    void markComponent(final Component parent, final Component component, final String markName);


    /**
     * Whenever a component created by renderer that widget controller is not aware of, but there is a logic related with
     * it, a renderer may mark it, so that then controller may assign a logic to it. This method should mark component
     * appropriately.
     *
     * @param parent
     *           parent component which will get a data structure of all marked components for every mark; it should be the
     *           same object as the one passed as parent to render method
     * @param component
     *           component to be marked
     * @param markName
     *           name of the mark - this value should be known by controller
     * @param markData
     *           any data object that may be needed by controller to perform a logic
     * @see #registerMarkedComponentsListener(Component, String, String, MarkedEventListener)
     */
    void markComponent(final Component parent, final Component component, final String markName, final Object markData);


    /**
     * Whenever a component created by renderer that widget controller is not aware of, but there is a logic related with
     * it, a renderer may mark it, so that then controller may assign a logic to it. This method registers an UI event to
     * all marked children components. It also makes sure that there is only one consumer for specified event and mark name
     * on particular component.
     * <P>
     * To assure that a particular logic is registered as listener is registered only once, it may be wrapped with
     * {@link com.hybris.cockpitng.core.model.Identifiable} interface. If a listener provided is of type
     * {@link com.hybris.cockpitng.core.model.Identifiable} and a listener with same id is already registered, then the old
     * one will be removed before new is registered.
     * </P>
     *
     * @param parent
     *           parent component to which marked children logic should be assigned
     * @param markName
     *           name of the mark
     * @param eventName
     *           UI event name which should trigger the logic
     * @param listener
     *           whenever an event takes place, a consumer will be asked with initial event and with data retrieved from a
     *           component
     * @see com.hybris.cockpitng.core.model.Identifiable
     */
    void registerMarkedComponentsListener(final Component parent, final String markName, final String eventName,
                    final MarkedEventListener listener);


    /**
     * Copies the marked components data structure from source component to target component and removes it from the source
     * component
     *
     * @param source
     *           component from which the marked components should be copied and removed
     * @param target
     *           component to which the marked components should be copied to
     */
    void moveMarkedComponents(final Component source, final Component target);
}
