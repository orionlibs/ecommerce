/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller.bookmark;

import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.util.Collection;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.event.BookmarkEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.spring.SpringUtil;

public class GenericBookmarkHandlerController extends DefaultWidgetController
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBookmarkHandler.class);
    private static final String SETTING_HANDLER_BEAN = "handlerBean";
    private transient BookmarkHandler bookmarkHandler;


    @GlobalCockpitEvent(eventName = Events.ON_BOOKMARK_CHANGE, scope = CockpitEvent.SESSION)
    public void handleBookmarkChanged(final CockpitEvent event)
    {
        Validate.notNull("The event may not be null", event);
        final Collection<Object> bookmarkEvents = event.getDataAsCollection();
        if(!bookmarkEvents.isEmpty())
        {
            final BookmarkHandler handler = resolveBookmarkHandler();
            bookmarkEvents.stream() //
                            .filter(data -> data instanceof BookmarkEvent) //
                            .forEach(data -> handleBookmark((BookmarkEvent)data, handler));
        }
    }


    protected BookmarkHandler resolveBookmarkHandler()
    {
        final String id = getWidgetSettings().getString(SETTING_HANDLER_BEAN);
        if(StringUtils.isNotBlank(id))
        {
            final BookmarkHandler handler = getBeanById(id);
            if(handler != null)
            {
                return handler;
            }
            LOG.error("Could not find bookmark handler with id: {}. DefaultBookmarkHandler will be used.", id);
        }
        return getBookmarkHandler();
    }


    protected BookmarkHandler getBeanById(final String id)
    {
        return (BookmarkHandler)SpringUtil.getBean(id, BookmarkHandler.class);
    }


    protected void handleBookmark(final BookmarkEvent data, final BookmarkHandler handler)
    {
        handler.handleBookmark(data, getWidgetInstanceManager());
    }


    /**
     * CAUTION: THis method is not used anymore!
     * @deprecated since 1905, use {@link #getBookmarkHandler()}
     */
    @Deprecated(since = "1905", forRemoval = true)
    public BookmarkHandler getDefaultBookmarkHandler()
    {
        return bookmarkHandler;
    }


    /**
     * CAUTION: THis method is not used anymore!
     * @param defaultBookmarkHandler
     *           bookamrk handler bean
     * @deprecated since 1905, use {@link #setBookmarkHandler(BookmarkHandler)}
     */
    @Deprecated(since = "1905", forRemoval = true)
    public void setDefaultBookmarkHandler(final BookmarkHandler defaultBookmarkHandler)
    {
        //NOOP left only to satisfy the compiler
    }


    public BookmarkHandler getBookmarkHandler()
    {
        return bookmarkHandler;
    }


    @Required
    public void setBookmarkHandler(final BookmarkHandler bookmarkHandler)
    {
        this.bookmarkHandler = bookmarkHandler;
    }
}
