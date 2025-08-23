/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.actions.flexibletoggle.FlexibleSidebarToggleAction;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.annotations.ViewEvents;
import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;

public class FlexibleLayoutWidgetController extends DefaultWidgetController
{
    public static final String SIDEBAR_STATE_OPENED = "opened";
    public static final String SIDEBAR_STATE_CLOSED = "closed";
    protected static final String SOCKET_INPUT_CLOSE = "closeSidebar";
    protected static final String SOCKET_INPUT_OPEN = "openSidebar";
    protected static final String SOCKET_INPUT_TOGGLE = "toggleSidebar";
    protected static final String SOCKET_INPUT_TOGGLE_COLLAPSED = "toggleCollapsedSidebar";
    protected static final String SOCKET_INPUT_SET_STATE = "sidebarState";
    protected static final String SOCKET_INPUT_RESPONSIVE_BREAKPOINT = "responsiveBreakpoint";
    protected static final String SOCKET_OUTPUT_STATE_CHANGED = "sidebarState";
    /**
     * @see UITools#EVENT_ON_LOOPBACK
     * @see UITools#postponeExecution(Component, Executable)
     * @deprecated since 6.5
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected static final String EVENT_ON_LOOPBACK = UITools.EVENT_ON_LOOPBACK;
    protected static final String COMPONENT_COLLAPSE_BUTTON = "collapseButton";
    protected static final String COMPONENT_CLOSE_SIDERBAR_BUTTON = "closeSidebarButton";
    protected static final String COMPONENT_FLEXIBLE_LAYOUT_OVERLAY = "flexlayoutOverlay";
    protected static final String SIDEBAR_POSITION_LEFT = "left";
    protected static final String SIDEBAR_POSITION_RIGHT = "right";
    protected static final String SCLASS_SIDEBAR_OPENED = "yw-flexlayout-sidebar-opened";
    protected static final String SCLASS_SIDEBAR_COLLAPSED = "yw-flexlayout-sidebar-collapsed";
    protected static final String SCLASS_SIDEBAR_COLLAPSIBLE = "yw-flexlayout-sidebar-collapsible";
    protected static final String SCLASS_SIDEBAR_CLOSED = "yw-flexlayout-sidebar-closed";
    protected static final String SCLASS_SIDEBAR_RESPONSIVE = "yw-flexlayout-sidebar-responsive";
    protected static final String SETTING_SIDEBAR_STATE = "sidebarState";
    protected static final String SETTING_SIDEBAR_POSITION = "sidebarPosition";
    protected static final String SETTING_SIDEBAR_COLLAPSED = "sidebarCollapsed";
    protected static final String SETTING_SIDEBAR_COLLAPSIBLE = "sidebarCollapsible";
    protected static final String SETTING_OVERLAP_ON_HOVER = "overlapOnHover";
    protected static final String SETTING_SIDEBAR_OVERLAP = "sidebarOverlap";
    protected static final String SCLASS_SIDEBAR_LEFT = "yw-flexlayout-sidebar-left";
    protected static final String SCLASS_SIDEBAR_RIGHT = "yw-flexlayout-sidebar-right";
    protected static final String SCLASS_SIDEBAR_OVERLAP_ON_HOVER = "yw-flexlayout-sidebar-hover-overlap";
    protected static final String SCLASS_SIDEBAR_OVERLAP = "yw-flexlayout-sidebar-overlap";
    protected static final String MODEL_SIDEBAR_STATE = "sidebarState";
    protected static final String MODEL_SIDEBAR_FORCE_STATE = "sidebarForceOpenState";
    protected static final String MODEL_SIDEBAR_RESPONSIVE_BREAKPOINT = "sidebarResponsiveBreakpoint";
    private static final String MODEL_SIDEBAR_COLLAPSED = "sidebarCollapsed";
    private static final Boolean DEFAULT_SIDEBAR_COLLAPSIBLE = Boolean.TRUE;
    private static final String EVENT_BEFORE_INVALIDATE_CONTENTS = "onBeforeContentsInvalidate";
    @Wire
    private Div rootpane;
    @Wire
    private Div sidebar;
    @Wire
    private Div contents;
    @Wire
    private Button closeSidebarButton;
    @Wire
    private Div flexlayoutOverlay;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        switch((String)getWidgetSettings().getOrDefault(SETTING_SIDEBAR_POSITION, SIDEBAR_POSITION_LEFT))
        {
            case SIDEBAR_POSITION_RIGHT:
                UITools.modifySClass(getRootpane(), SCLASS_SIDEBAR_RIGHT, true);
                break;
            case SIDEBAR_POSITION_LEFT:
            default:
                UITools.modifySClass(getRootpane(), SCLASS_SIDEBAR_LEFT, true);
                break;
        }
        if(getWidgetSettings().getBoolean(SETTING_OVERLAP_ON_HOVER))
        {
            UITools.modifySClass(getRootpane(), SCLASS_SIDEBAR_OVERLAP_ON_HOVER, true);
        }
        restoreResponsivenessImmediately();
        restoreSidebarCollapsedImmediately();
        UITools.modifySClass(getRootpane(), SCLASS_SIDEBAR_COLLAPSIBLE, isSidebarCollapsible());
        UITools.modifySClass(getRootpane(), SCLASS_SIDEBAR_OVERLAP, getWidgetSettings().getBoolean(SETTING_SIDEBAR_OVERLAP));
        if(!isSidebarResponsive() || isSidebarForceState())
        {
            postponedStateSend(comp);
        }
    }


    protected void postponedStateSend(final Component parent)
    {
        UITools.postponeExecution(parent, this::notifySidebarState);
    }


    @ViewEvents(
                    {@ViewEvent(eventName = Events.ON_CLICK, componentID = COMPONENT_FLEXIBLE_LAYOUT_OVERLAY),
                                    @ViewEvent(eventName = Events.ON_CLICK, componentID = COMPONENT_CLOSE_SIDERBAR_BUTTON)})
    @SocketEvent(socketId = SOCKET_INPUT_TOGGLE)
    public void toggleSidebar()
    {
        if(isSidebarResponsive())
        {
            setSidebarForceState(!isSidebarForceState());
            if(isSidebarForceState())
            {
                setSidebarState(getSidebarState());
            }
            else
            {
                setSidebarState(StringUtils.EMPTY);
            }
        }
        else
        {
            setSidebarState(SIDEBAR_STATE_OPENED.equals(getSidebarState()) ? SIDEBAR_STATE_CLOSED : SIDEBAR_STATE_OPENED);
        }
    }


    protected void notifySidebarState()
    {
        final String state;
        if(isSidebarResponsive())
        {
            state = isSidebarForceState() ? getSidebarState() : StringUtils.EMPTY;
        }
        else
        {
            state = SIDEBAR_STATE_OPENED.equals(getSidebarState()) ? SIDEBAR_STATE_OPENED : SIDEBAR_STATE_CLOSED;
        }
        sendOutput(SOCKET_OUTPUT_STATE_CHANGED, state);
    }


    @SocketEvent(socketId = SOCKET_INPUT_OPEN)
    public void openSidebar()
    {
        setSidebarState(SIDEBAR_STATE_OPENED);
    }


    @SocketEvent(socketId = SOCKET_INPUT_CLOSE)
    public void closeSidebar()
    {
        setSidebarState(SIDEBAR_STATE_CLOSED);
    }


    @SocketEvent(socketId = SOCKET_INPUT_SET_STATE)
    public void setSidebarState(final String state)
    {
        setSidebarStateImmediately(state);
        notifySidebarState();
        if(!getWidgetSettings().getBoolean(SETTING_SIDEBAR_OVERLAP))
        {
            postponedContentsInvalidation();
        }
    }


    protected void restoreStateImmediately()
    {
        setSidebarStateImmediately(getSidebarState());
    }


    protected void setSidebarStateImmediately(final String state)
    {
        UITools.modifySClass(getRootpane(), SCLASS_SIDEBAR_OPENED, SIDEBAR_STATE_OPENED.equals(state));
        UITools.modifySClass(getRootpane(), SCLASS_SIDEBAR_CLOSED, SIDEBAR_STATE_CLOSED.equals(state));
        if(StringUtils.isNotBlank(state))
        {
            getModel().setValue(MODEL_SIDEBAR_STATE, state);
        }
    }


    protected String getSidebarState()
    {
        final String state = getModel().getValue(MODEL_SIDEBAR_STATE, String.class);
        return ObjectUtils.defaultIfNull(state, (String)getWidgetSettings().get(SETTING_SIDEBAR_STATE));
    }


    protected void restoreSidebarCollapsedImmediately()
    {
        setSidebarCollapsedImmediately(isSidebarCollapsed());
    }


    protected boolean isSidebarCollapsed()
    {
        return BooleanUtils.toBooleanDefaultIfNull(getModel().getValue(MODEL_SIDEBAR_COLLAPSED, Boolean.class),
                        getWidgetSettings().getBoolean(SETTING_SIDEBAR_COLLAPSED));
    }


    protected void setSidebarCollapsed(final boolean collapsed)
    {
        setSidebarCollapsedImmediately(collapsed);
        if(!getWidgetSettings().getBoolean(SETTING_SIDEBAR_OVERLAP))
        {
            postponedContentsInvalidation();
        }
    }


    protected void setSidebarCollapsedImmediately(final boolean collapsed)
    {
        UITools.modifySClass(getRootpane(), SCLASS_SIDEBAR_COLLAPSED, collapsed);
        getModel().setValue(MODEL_SIDEBAR_COLLAPSED, Boolean.valueOf(collapsed));
    }


    protected void postponedContentsInvalidation()
    {
        getRootpane().addEventListener(EVENT_BEFORE_INVALIDATE_CONTENTS, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event) throws Exception
            {
                try
                {
                    getContents().invalidate();
                }
                finally
                {
                    getRootpane().removeEventListener(EVENT_BEFORE_INVALIDATE_CONTENTS, this);
                }
            }
        });
        Events.echoEvent(EVENT_BEFORE_INVALIDATE_CONTENTS, getRootpane(), getContents());
    }


    protected boolean shouldSetState()
    {
        return isSidebarForceState() || !isSidebarResponsive();
    }


    protected boolean isSidebarForceState()
    {
        return BooleanUtils.toBooleanDefaultIfNull(getModel().getValue(MODEL_SIDEBAR_FORCE_STATE, Boolean.class), false);
    }


    protected void setSidebarForceState(final boolean forceOpen)
    {
        getModel().setValue(MODEL_SIDEBAR_FORCE_STATE, Boolean.valueOf(forceOpen));
    }


    protected boolean isSidebarCollapsible()
    {
        final Boolean collapsible = (Boolean)getWidgetSettings().getOrDefault(SETTING_SIDEBAR_COLLAPSIBLE,
                        DEFAULT_SIDEBAR_COLLAPSIBLE);
        return BooleanUtils.isTrue(collapsible);
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = COMPONENT_COLLAPSE_BUTTON)
    @SocketEvent(socketId = SOCKET_INPUT_TOGGLE_COLLAPSED)
    public void toggleCollapsedSidebar()
    {
        if(isSidebarCollapsible())
        {
            setSidebarCollapsed(!isSidebarCollapsed());
        }
    }


    protected void restoreResponsivenessImmediately()
    {
        setSidebarResponsiveImmediately(getSidebarResponsiveBreakpoint());
    }


    protected Object getSidebarResponsiveBreakpoint()
    {
        return getValue(MODEL_SIDEBAR_RESPONSIVE_BREAKPOINT, Object.class);
    }


    protected boolean isSidebarResponsive()
    {
        return getSidebarResponsiveBreakpoint() != null;
    }


    @SocketEvent(socketId = SOCKET_INPUT_RESPONSIVE_BREAKPOINT)
    public void setSidebarResponsive(final Object breakpoint)
    {
        setSidebarResponsiveImmediately(breakpoint);
        notifySidebarState();
    }


    protected void setSidebarResponsiveImmediately(final Object breakpoint)
    {
        final Object previous = getSidebarResponsiveBreakpoint();
        if(ObjectUtils.notEqual(breakpoint, previous))
        {
            UITools.removeSClass(getRootpane(), String.format(FlexibleSidebarToggleAction.PATTERN_RESPONSIVE_BREAKPOINT, previous));
        }
        final boolean responsive = breakpoint != null;
        setValue(MODEL_SIDEBAR_RESPONSIVE_BREAKPOINT, breakpoint);
        UITools.modifySClass(getRootpane(), SCLASS_SIDEBAR_RESPONSIVE, responsive);
        if(responsive)
        {
            UITools.addSClass(getRootpane(), String.format(FlexibleSidebarToggleAction.PATTERN_RESPONSIVE_BREAKPOINT, breakpoint));
        }
        if(shouldSetState())
        {
            restoreStateImmediately();
        }
        else
        {
            setSidebarStateImmediately(StringUtils.EMPTY);
        }
    }


    protected Div getRootpane()
    {
        return rootpane;
    }


    protected Div getSidebar()
    {
        return sidebar;
    }


    protected Div getContents()
    {
        return contents;
    }


    protected Button getCloseSidebarButton()
    {
        return closeSidebarButton;
    }


    public Div getFlexlayoutOverlay()
    {
        return flexlayoutOverlay;
    }
}
