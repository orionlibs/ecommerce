/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.google.common.collect.Maps;
import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.annotations.ViewEvents;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.layout.ResponsiveLayoutStrategy;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang3.BooleanUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SizeEvent;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.East;
import org.zkoss.zul.North;
import org.zkoss.zul.South;
import org.zkoss.zul.West;

public class BorderLayoutWidgetController extends DefaultWidgetController
{
    public static final String SETTING_WEST_BORDER = "westBorder";
    public static final String SETTING_CENTER_BORDER = "centerBorder";
    public static final String SETTING_SOUTH_BORDER = "southBorder";
    public static final String SETTING_NORTH_BORDER = "northBorder";
    public static final String SETTING_EAST_BORDER = "eastBorder";
    public static final String SETTING_BORDER = "border";
    public static final String SETTING_NORTH_DISABLED = "northDisabled";
    public static final String SETTING_SOUTH_DISABLED = "southDisabled";
    public static final String SETTING_EAST_WIDTH = "eastWidth";
    public static final String SETTING_WEST_WIDTH = "westWidth";
    public static final String SETTING_OPAQUE_BG = "opaqueBG";
    public static final String SETTING_NORTH_HEIGHT = "northHeight";
    public static final String SETTING_SOUTH_HEIGHT = "southHeight";
    public static final String SETTING_SOUTH_COLLAPSIBLE = "southCollapsible";
    public static final String SETTING_NORTH_COLLAPSIBLE = "northCollapsible";
    public static final String SETTING_WEST_COLLAPSIBLE = "westCollapsible";
    public static final String SETTING_EAST_COLLAPSIBLE = "eastCollapsible";
    public static final String SETTING_AUTO_CLOSE_WEST = "autoCloseWest";
    public static final String SETTING_BROADCAST_HORIZONAL_RESIZE = "broadcastHorizontalResize";
    public static final String SOCKET_IN_OPEN_NORTH = "openNorth";
    public static final String SOCKET_IN_OPEN_SOUTH = "openSouth";
    public static final String SOCKET_IN_OPEN_WEST = "openWest";
    public static final String SOCKET_IN_OPEN_EAST = "openEast";
    public static final String SOCKET_IN_CLOSE_NORTH = "closeNorth";
    public static final String SOCKET_IN_CLOSE_SOUTH = "closeSouth";
    public static final String SOCKET_IN_CLOSE_WEST = "closeWest";
    public static final String SOCKET_IN_CLOSE_EAST = "closeEast";
    public static final String SOCKET_OUT_RESIZED = "resized";
    public static final String OPEN_WEST_ON_SCREEN_RESIZE = "openWestOnScreenResize";
    public static final String COMP_WEST_ID = "west";
    public static final String COMP_EAST_ID = "east";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String SOURCE = "source";
    private static final String DATA = "data";
    private static final long serialVersionUID = 893711082301242545L;
    private boolean autoCloseWest;
    private West west;
    private East east;
    private South south;
    private North north;
    private Borderlayout borderlayout;
    private transient ResponsiveLayoutStrategy responsiveLayoutStrategy;
    private transient CockpitEventQueue cockpitEventQueue;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        autoCloseWest = getWidgetSettings().getBoolean(SETTING_AUTO_CLOSE_WEST);
    }


    @ViewEvents(
                    {@ViewEvent(componentID = COMP_WEST_ID, eventName = Events.ON_SIZE),
                                    @ViewEvent(componentID = COMP_EAST_ID, eventName = Events.ON_SIZE)})
    public void onEastResize(final SizeEvent event)
    {
        if(BooleanUtils.isTrue(getWidgetSettings().getBoolean(SETTING_BROADCAST_HORIZONAL_RESIZE)) && event != null)
        {
            final Map<String, Object> map = Maps.newHashMap();
            map.put(WIDTH, event.getWidth());
            map.put(HEIGHT, event.getHeight());
            map.put(SOURCE, event.getTarget());
            map.put(DATA, event.getData());
            sendOutput(SOCKET_OUT_RESIZED, map);
        }
    }


    @GlobalCockpitEvent(eventName = Events.ON_CLIENT_INFO, scope = CockpitEvent.SESSION)
    public void onViewInfo(final DefaultCockpitEvent globalEvent)
    {
        if(autoCloseWest)
        {
            final ClientInfoEvent info = (ClientInfoEvent)globalEvent.getData();
            final Map<String, Object> context = Collections.unmodifiableMap(getWidgetSettings());
            final boolean saveSpaceHorizontally = responsiveLayoutStrategy.isSaveSpaceHorizontally(info, context);
            final boolean westOpen = west.isOpen();
            if(saveSpaceHorizontally && getValue(OPEN_WEST_ON_SCREEN_RESIZE, Boolean.class) == null)
            {
                setValue(OPEN_WEST_ON_SCREEN_RESIZE, Boolean.valueOf(westOpen));
                if(westOpen)
                {
                    closeWest();
                }
            }
            else if(!saveSpaceHorizontally)
            {
                final boolean autoExpand = BooleanUtils.isTrue(getValue(OPEN_WEST_ON_SCREEN_RESIZE, Boolean.class));
                if(autoExpand && !westOpen)
                {
                    openWest();
                }
                setValue(OPEN_WEST_ON_SCREEN_RESIZE, null);
            }
        }
    }


    @SocketEvent(socketId = SOCKET_IN_CLOSE_EAST)
    public void closeEast()
    {
        east.setOpen(false);
    }


    @SocketEvent(socketId = SOCKET_IN_CLOSE_WEST)
    public void closeWest()
    {
        west.setOpen(false);
    }


    @SocketEvent(socketId = SOCKET_IN_CLOSE_SOUTH)
    public void closeSouth()
    {
        south.setOpen(false);
    }


    @SocketEvent(socketId = SOCKET_IN_CLOSE_NORTH)
    public void closeNorth()
    {
        north.setOpen(false);
    }


    @SocketEvent(socketId = SOCKET_IN_OPEN_EAST)
    public void openEast()
    {
        east.setOpen(true);
    }


    @SocketEvent(socketId = SOCKET_IN_OPEN_WEST)
    public void openWest()
    {
        west.setOpen(true);
    }


    @SocketEvent(socketId = SOCKET_IN_OPEN_SOUTH)
    public void openSouth()
    {
        south.setOpen(true);
    }


    @SocketEvent(socketId = SOCKET_IN_OPEN_NORTH)
    public void openNorth()
    {
        north.setOpen(true);
    }


    public West getWest()
    {
        return west;
    }


    public East getEast()
    {
        return east;
    }


    public South getSouth()
    {
        return south;
    }


    public North getNorth()
    {
        return north;
    }


    public Borderlayout getBorderlayout()
    {
        return borderlayout;
    }


    public ResponsiveLayoutStrategy getResponsiveLayoutStrategy()
    {
        return responsiveLayoutStrategy;
    }


    public CockpitEventQueue getCockpitEventQueue()
    {
        return cockpitEventQueue;
    }
}
