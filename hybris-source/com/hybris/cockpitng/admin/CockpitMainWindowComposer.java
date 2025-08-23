/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.CNG;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetService;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.CockpitEvents;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.ui.WidgetInstanceService;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.engine.SessionWidgetInstanceRegistry;
import com.hybris.cockpitng.events.SocketEvent;
import com.hybris.cockpitng.helper.RoleChooserHelper;
import com.hybris.cockpitng.util.CockpitSessionService;
import com.hybris.cockpitng.util.ViewAnnotationAwareComposer;
import com.hybris.cockpitng.util.WidgetUtils;
import com.hybris.cockpitng.web.js.DependenciesManager;
import com.hybris.cockpitng.widgets.controller.BreadboardController;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.BookmarkEvent;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

/**
 * Adds adminmode functionality to the cockpit main window.
 */
public class CockpitMainWindowComposer extends ViewAnnotationAwareComposer
{
    public static final String BO_LOGIN_BOOKMARK = "_bologingbmrk";
    public static final String HEARTBEAT_EVENT = "heartbeat";
    private static final Logger LOG = LoggerFactory.getLogger(CockpitMainWindowComposer.class);
    private static final String BREADBOARD = "breadboard";
    private static final String ON_MAIN_WINDOW_RENDERED = "onMainWindowRendered";
    private static final String LAST_SUCCESSFULLY_RENDERED_DESKTOP = "_lastSuccessDesktopId";
    public static final String ON_AFTER_COMPOSE = "onAfterCompose";
    private transient CockpitAdminService cockpitAdminService;
    private transient WidgetInstanceService widgetInstanceService;
    private transient SessionWidgetInstanceRegistry sessionWidgetInstanceRegistry;
    private transient WidgetService widgetService;
    private transient CockpitProperties cockpitProperties;
    private transient WidgetUtils widgetUtils;
    private transient Widgetslot slot;
    private transient CockpitEventQueue cockpitEventQueue;
    private Timer globalEventTimer;
    private transient DependenciesManager dependenciesManager;
    private transient CockpitSessionService cockpitSessionService;
    private transient RoleChooserHelper roleChooserHelper;


    @Override
    public void doAfterCompose(final Component comp) throws Exception
    {
        super.doAfterCompose(comp);
        getDependenciesManager().manageScriptDependencies(getDependenciesManager().getScriptDependencies(), comp);
        if(getCockpitAdminService().isAdminModePermitted())
        {
            ((Window)comp).setCtrlKeys("#f4");
            getCockpitAdminService().setAdminMode(
                            "test".equals(Executions.getCurrent().getParameter("mode")) || getCockpitAdminService().isAdminMode(), getSlot());
            if(isInCngDevelopmentMode() && isSetBreadboardMode())
            {
                activateBreadboardWidget();
            }
            Executions.createComponents("/cockpit_admin.zul", comp, Collections.emptyMap());
            getSlot().getParent().addEventListener("onInvalidateLater", event -> {
                getSlot().invalidate();
                if(getCockpitAdminService().isSymbolicAdminMode() && getCockpitAdminService().isShowConnectionsFlagEnabled())
                {
                    Clients.evalJavaScript("drawConnectionsRequest()");
                }
            });
        }
        getGlobalEventTimer().addEventListener(Events.ON_TIMER, event -> {
            dispatchGlobalEvents();
            publishHeartbeatEvent(event);
        });
        comp.addEventListener(ON_AFTER_COMPOSE, e -> {
            processDeepLinkBookmark();
            comp.removeEventListener(ON_AFTER_COMPOSE, this);
        });
        Events.echoEvent(ON_AFTER_COMPOSE, comp, null);
    }


    protected void processDeepLinkBookmark()
    {
        final HttpServletRequest request = (HttpServletRequest)Executions.getCurrent().getNativeRequest();
        final HttpServletResponse response = (HttpServletResponse)Executions.getCurrent().getNativeResponse();
        final Cookie[] cookies = request.getCookies();
        for(final Cookie cookie : cookies)
        {
            if(BO_LOGIN_BOOKMARK.equals(cookie.getName()))
            {
                final String bookmark = new String(Base64.getDecoder().decode(cookie.getValue()));
                LOG.info("Set bookmark: {}", bookmark);
                Executions.getCurrent().getDesktop().setBookmark(bookmark);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                LOG.info("Bookmark info cookie removed.");
                break;
            }
        }
    }


    protected boolean isInCngDevelopmentMode()
    {
        return getCockpitProperties().getBoolean(CNG.PROPERTY_DEVELOPMENT_MODE);
    }


    protected boolean isSetBreadboardMode()
    {
        return BREADBOARD.equals(Executions.getCurrent().getParameter("mode"));
    }


    protected void activateBreadboardWidget()
    {
        WidgetInstance breadBoardWidgetInstance = getSessionWidgetInstanceRegistry().getRegisteredWidgetInstance(BREADBOARD);
        if(breadBoardWidgetInstance == null)
        {
            final Widget breadBoardWidget = getWidgetService().createWidget(null, BREADBOARD, BREADBOARD,
                            "com.hybris.cockpitng.breadboardwidget");
            breadBoardWidgetInstance = getWidgetInstanceService().createRootWidgetInstance(breadBoardWidget);
            getSessionWidgetInstanceRegistry().registerWidgetInstance(breadBoardWidgetInstance, BREADBOARD);
        }
        if(!BREADBOARD.equals(getSlot().getSlotID()))
        {
            getSlot().setWidgetInstance(null);
            getSlot().setSlotID(BREADBOARD);
            getSlot().updateView();
        }
        final String parameter = Executions.getCurrent().getParameter("widget");
        if(StringUtils.isNotBlank(parameter))
        {
            final List<Component> children = new ArrayList<>(getSlot().getChildren());
            for(final Component component : children)
            {
                final Event event = new SocketEvent("onSocketInput_" + BreadboardController.SELECT_WIDGET_DEFINITION_INPUT_ID,
                                component, parameter, null, null);
                Events.echoEvent(event);
            }
        }
    }


    @ViewEvent(eventName = Events.ON_CTRL_KEY)
    public void toggleAdminMode()
    {
        getCockpitAdminService().toggleAdminMode(getSlot());
    }


    @ViewEvent(eventName = Events.ON_CLIENT_INFO)
    public void publishClientInfo(final ClientInfoEvent event)
    {
        boolean initial = false;
        if(event != null)
        {
            final Session session = Sessions.getCurrent();
            session.setAttribute(CockpitEvents.SCREEN_WIDTH, event.getDesktopWidth());
            session.setAttribute(CockpitEvents.SCREEN_HEIGHT, event.getDesktopHeight());
            final Object initialInfo = session.getAttribute(CockpitEvents.INITIAL_CLIENT_INFO);
            if(initialInfo == null)
            {
                session.setAttribute(CockpitEvents.INITIAL_CLIENT_INFO, false);
                initial = true;
            }
        }
        final DefaultCockpitEvent cockpitEvent = new DefaultCockpitEvent(Events.ON_CLIENT_INFO, event, null);
        cockpitEvent.getContext().put(CockpitEvents.INITIAL_CLIENT_INFO, initial);
        getCockpitEventQueue().publishEvent(cockpitEvent);
    }


    @ViewEvent(eventName = "onClientTimeZone")
    public void publishClientTimeZone(final Event event)
    {
        if(event != null)
        {
            final Session session = Sessions.getCurrent();
            final String timeZoneId = event.getData().toString();
            final TimeZone timeZone = TimeZone.getTimeZone(timeZoneId);
            session.setAttribute(Attributes.PREFERRED_TIME_ZONE, timeZone);
        }
    }


    @ViewEvent(eventName = Events.ON_CREATE)
    public void createMainWindow(final Event event)
    {
        final Component target = event.getTarget();
        Clients.evalJavaScript("if($(\"#" + target.getUuid() + "\").is(\":visible\")) " + "CockpitNG.sendEvent(\"#"
                        + target.getUuid() + "\",\"" + ON_MAIN_WINDOW_RENDERED + "\", null);");
    }


    @ViewEvent(eventName = ON_MAIN_WINDOW_RENDERED)
    public void onMainWindowRendered(final Event event)
    {
        final String currentDesktopId = Executions.getCurrent().getDesktop().getId();
        final String lastDesktopID = getLastSuccessfullyRenderedDesktopId();
        if(ObjectUtils.notEqual(currentDesktopId, lastDesktopID))
        {
            getCockpitSessionService().setAttribute(LAST_SUCCESSFULLY_RENDERED_DESKTOP, currentDesktopId);
            getCockpitEventQueue().publishEvent(new DefaultCockpitEvent("onDesktopChange", event, null));
        }
    }


    /**
     * Receives a ZK {@link BookmarkEvent} and publishes it as a global cockpitng event
     *
     * @param event
     *           the ZK {@link BookmarkEvent}
     */
    @ViewEvent(eventName = Events.ON_BOOKMARK_CHANGE)
    public void publishCurrentBookmarkGlobalEvent(final BookmarkEvent event)
    {
        if(getRoleChooserHelper().isRoleSelectorContainerContainerVisible())
        {
            getCockpitSessionService().setAttribute(BO_LOGIN_BOOKMARK, event);
        }
        else
        {
            getCockpitEventQueue().publishEvent(new DefaultCockpitEvent(Events.ON_BOOKMARK_CHANGE, event, null));
        }
    }


    public void dispatchGlobalEvents() throws Exception
    {
        getWidgetUtils().dispatchGlobalEvents();
    }


    protected void publishHeartbeatEvent(final Event event)
    {
        final DefaultCockpitEvent heartbeatEvent = new DefaultCockpitEvent(HEARTBEAT_EVENT, event, null);
        getCockpitEventQueue().publishEvent(heartbeatEvent);
    }


    protected String getLastSuccessfullyRenderedDesktopId()
    {
        final Object desktopId = getCockpitSessionService().getAttribute(LAST_SUCCESSFULLY_RENDERED_DESKTOP);
        return desktopId == null ? StringUtils.EMPTY : Objects.toString(desktopId);
    }


    public CockpitAdminService getCockpitAdminService()
    {
        return cockpitAdminService;
    }


    public void setCockpitAdminService(final CockpitAdminService cockpitAdminService)
    {
        this.cockpitAdminService = cockpitAdminService;
    }


    public WidgetInstanceService getWidgetInstanceService()
    {
        return widgetInstanceService;
    }


    public void setWidgetInstanceService(final WidgetInstanceService widgetInstanceService)
    {
        this.widgetInstanceService = widgetInstanceService;
    }


    public SessionWidgetInstanceRegistry getSessionWidgetInstanceRegistry()
    {
        return sessionWidgetInstanceRegistry;
    }


    public void setSessionWidgetInstanceRegistry(final SessionWidgetInstanceRegistry sessionWidgetInstanceRegistry)
    {
        this.sessionWidgetInstanceRegistry = sessionWidgetInstanceRegistry;
    }


    public WidgetService getWidgetService()
    {
        return widgetService;
    }


    public void setWidgetService(final WidgetService widgetService)
    {
        this.widgetService = widgetService;
    }


    public CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }


    public WidgetUtils getWidgetUtils()
    {
        return widgetUtils;
    }


    public void setWidgetUtils(final WidgetUtils widgetUtils)
    {
        this.widgetUtils = widgetUtils;
    }


    public Widgetslot getSlot()
    {
        return slot;
    }


    public void setSlot(final Widgetslot slot)
    {
        this.slot = slot;
    }


    public CockpitEventQueue getCockpitEventQueue()
    {
        return cockpitEventQueue;
    }


    public void setCockpitEventQueue(final CockpitEventQueue cockpitEventQueue)
    {
        this.cockpitEventQueue = cockpitEventQueue;
    }


    public Timer getGlobalEventTimer()
    {
        return globalEventTimer;
    }


    public void setGlobalEventTimer(final Timer globalEventTimer)
    {
        this.globalEventTimer = globalEventTimer;
    }


    public DependenciesManager getDependenciesManager()
    {
        return dependenciesManager;
    }


    public void setDependenciesManager(final DependenciesManager dependenciesManager)
    {
        this.dependenciesManager = dependenciesManager;
    }


    public CockpitSessionService getCockpitSessionService()
    {
        return cockpitSessionService;
    }


    public void setCockpitSessionService(final CockpitSessionService cockpitSessionService)
    {
        this.cockpitSessionService = cockpitSessionService;
    }


    public RoleChooserHelper getRoleChooserHelper()
    {
        return roleChooserHelper;
    }


    public void setRoleChooserHelper(final RoleChooserHelper roleChooserHelper)
    {
        this.roleChooserHelper = roleChooserHelper;
    }
}
