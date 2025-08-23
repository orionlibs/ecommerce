/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.extendedmultireferenceeditor;

import java.util.Collection;
import java.util.Collections;
import org.apache.commons.lang3.ObjectUtils;
import org.zkoss.zk.ui.event.Event;

/**
 * Event which informs inline editor that should perform refresh
 */
public class InlineEditorRefreshEvent extends Event
{
    private static final String ON_INLINE_EDITOR_REFRESH = "onInlineEditorRefresh";
    private final String inlineProperty;
    private final boolean reload;
    private transient Collection<Object> itemsToRefresh;


    public InlineEditorRefreshEvent(final String inlineProperty)
    {
        super(getInlineEventName(inlineProperty));
        this.inlineProperty = inlineProperty;
        reload = true;
    }


    public InlineEditorRefreshEvent(final String inlineProperty, final Collection<Object> itemsToRefresh)
    {
        super(getInlineEventName(inlineProperty));
        this.inlineProperty = inlineProperty;
        this.itemsToRefresh = itemsToRefresh;
        reload = false;
    }


    public String getInlineProperty()
    {
        return inlineProperty;
    }


    public boolean isReload()
    {
        return reload;
    }


    public Collection<Object> getItemsToRefresh()
    {
        return ObjectUtils.defaultIfNull(itemsToRefresh, Collections.emptyList());
    }


    /**
     * Creates inline editor event name which should be used to send/register events
     *
     * @param inlineProperty inline property
     * @return event name with inline property postfix
     */
    public static String getInlineEventName(final String inlineProperty)
    {
        return ON_INLINE_EDITOR_REFRESH.concat(inlineProperty);
    }
}
