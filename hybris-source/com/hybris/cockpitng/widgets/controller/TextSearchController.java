/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.annotations.ViewEvents;
import com.hybris.cockpitng.util.DefaultWidgetController;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;

/**
 * Widget controller of the <code>textsearch</code> widget.
 */
public class TextSearchController extends DefaultWidgetController
{
    public static final String SOCKET_IN_QUERY = "query";
    public static final String SOCKET_OUT_QUERY = SOCKET_IN_QUERY;
    public static final String SOCKET_IN_CLEARQUERY = "clearQuery";
    public static final String SOCKET_IN_ENABLE = "enabled";
    protected static final String QUERY_ATTRIBUTE = "query";
    protected static final String ENABLED_ATTRIBUTE = "enabled";
    protected static final String SETTING_ENABLED_BY_DEFAULT = "enabledByDefault";
    protected static final String COMP_SEARCH_BUTTON = "searchButton";
    protected static final String COMP_SEARCH_INPUT = "searchBox";
    private static final long serialVersionUID = -4567033846698631596L;
    private Textbox searchBox;
    private Button searchButton;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        final Boolean enabled = isEnabled();
        if(enabled == null)
        {
            if(BooleanUtils.isFalse(getWidgetSettings().getBoolean(SETTING_ENABLED_BY_DEFAULT)))
            {
                enableTextSearchInternal(false);
            }
        }
        else
        {
            enableTextSearchInternal(enabled);
        }
        final String query = getSearchQuery();
        if(query != null)
        {
            initializeQuery(query);
        }
    }


    private void enableTextSearchInternal(final boolean enabled)
    {
        setEnabled(enabled ? Boolean.TRUE : Boolean.FALSE);
        searchBox.setDisabled(!enabled);
        searchButton.setDisabled(!enabled);
    }


    @SocketEvent(socketId = SOCKET_IN_CLEARQUERY)
    public void clearQuery()
    {
        initializeQuery(StringUtils.EMPTY);
    }


    @SocketEvent(socketId = SOCKET_IN_QUERY)
    public void initializeQuery(final String value)
    {
        setSearchQuery(value);
        searchBox.setValue(StringUtils.defaultIfBlank(value, StringUtils.EMPTY));
    }


    @SocketEvent(socketId = SOCKET_IN_ENABLE)
    public void enableTextSearch(final Boolean enable)
    {
        enableTextSearchInternal(BooleanUtils.toBoolean(enable));
    }


    @ViewEvent(componentID = COMP_SEARCH_INPUT, eventName = Events.ON_CHANGE)
    public void onQueryChange()
    {
        if(searchBox != null)
        {
            setSearchQuery(searchBox.getValue());
        }
    }


    @ViewEvents({@ViewEvent(componentID = COMP_SEARCH_BUTTON, eventName = Events.ON_CLICK),
                    @ViewEvent(componentID = COMP_SEARCH_INPUT, eventName = Events.ON_OK)})
    public void onSearch()
    {
        if(searchBox != null)
        {
            sendOutput(SOCKET_OUT_QUERY, searchBox.getValue());
        }
    }


    private String getSearchQuery()
    {
        return getModel().getValue(QUERY_ATTRIBUTE, String.class);
    }


    private void setSearchQuery(final String query)
    {
        getModel().setValue(QUERY_ATTRIBUTE, query);
    }


    private Boolean isEnabled()
    {
        return getModel().getValue(ENABLED_ATTRIBUTE, Boolean.class);
    }


    private void setEnabled(final Boolean enabled)
    {
        getModel().setValue(ENABLED_ATTRIBUTE, enabled);
    }


    public Textbox getSearchBox()
    {
        return searchBox;
    }


    public Button getSearchButton()
    {
        return searchButton;
    }
}
