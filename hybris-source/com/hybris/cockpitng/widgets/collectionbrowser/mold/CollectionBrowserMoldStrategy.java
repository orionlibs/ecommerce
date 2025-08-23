/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold;

import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.testing.annotation.NullSafeWidget;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.SinglePage;
import com.hybris.cockpitng.widgets.navigation.NavigationItemSelectorContext;
import java.util.Collections;
import java.util.Set;
import org.springframework.core.Ordered;
import org.zkoss.zk.ui.Component;

/**
 * Interface for mold strategies used by Collection Browser widget.
 */
public interface CollectionBrowserMoldStrategy extends Ordered
{
    /**
     * Sets a communication channel with widget controller.
     *
     * @param context
     *           communication channel with widget controller.
     */
    @NullSafeWidget(false) // mold might be fully dependent on context
    void setContext(CollectionBrowserMoldContext context);


    /**
     * Set data for a single page.
     */
    void setPage(SinglePage singlePage);


    /**
     * Render mold inside given parent component with single page.
     */
    void render(Component parent, SinglePage singlePage);


    /**
     * Handle "previous item" navigation event.
     */
    void previousItemSelectorInvocation();


    /**
     * Handle "next item" navigation event.
     */
    void nextItemSelectorInvocation();


    /**
     * Check if we can handle object events for a given type code.
     *
     * @param typeCode
     *           type code.
     * @return {@code true} if mold is interested in handling object events for objects of given type, otherwise
     *         {@code false}.
     */
    boolean isHandlingObjectEvents(String typeCode);


    /**
     * Additional logic to perform on object deleted event.
     */
    void handleObjectDeleteEvent(CockpitEvent event);


    /**
     * Additional logic to perform on object updated event.
     */
    void handleObjectUpdateEvent(CockpitEvent event);


    /**
     * Additional logic to perform on objects updated event.
     */
    default void handleObjectsUpdateEvent(final CockpitEvent event)
    {
        // NOP
    }


    /**
     * Additional logic to perform on object create event.
     */
    void handleObjectCreateEvent(CockpitEvent event);


    /**
     * Select given items.
     * <p>
     * No notifications should be sent.
     * </p>
     *
     * @param items
     *           items to be selected.
     */
    void selectItems(Set<?> items);


    /**
     * Deselects all items.
     * <p>
     * No notifications should be sent.
     * </p>
     */
    void deselectItems();


    /**
     * Mark provided item as focused. Only one item may be focused at the time.
     *
     * @param oldFocus
     *           item that was focused up until now
     * @param newFocus
     *           item to be marked as focused
     */
    default void focusItem(final Object oldFocus, final Object newFocus)
    {
        selectItems(Collections.singleton(newFocus));
    }


    /**
     * Method called by the controller when mold is turned off. It should remove itself from its parent and free
     * resources.
     */
    void release();


    /**
     * @return name of the mold for internal use (e.g. to determine CSS class of (in)active mold selection button).
     */
    String getName();


    /**
     * Set type code for the current mold strategy.
     */
    void setTypeCode(String typeCode);


    /**
     * @return type code for the current mold strategy.
     */
    String getTypeCode();


    /**
     * @return tooltip text that should appear on a mold selection button.
     */
    String getTooltipText();


    /**
     * @return current {@link com.hybris.cockpitng.widgets.navigation.NavigationItemSelectorContext}.
     */
    NavigationItemSelectorContext getNavigationItemSelectorContext();


    /**
     * Resets the mold to the initial state, displaying no data.
     */
    void reset();


    /**
     * Set the order value for this bean. The higher the order, the farther it will appear in mold selector.
     */
    void setOrder(int order);
}
