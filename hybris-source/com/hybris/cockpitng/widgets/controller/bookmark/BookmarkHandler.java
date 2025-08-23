/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller.bookmark;

import com.hybris.cockpitng.engine.WidgetInstanceManager;
import org.zkoss.zk.ui.event.BookmarkEvent;

/**
 * Handler for bookmark events.
 *
 * @see GenericBookmarkHandlerController
 */
public interface BookmarkHandler
{
    /**
     * Handles a bookmark event.
     *
     * @param data
     *           Bookmark event data {@link BookmarkEvent}
     * @param wim
     *           {@link WidgetInstanceManager} of the widget instance that invokes the handler.
     */
    void handleBookmark(BookmarkEvent data, WidgetInstanceManager wim);
}
