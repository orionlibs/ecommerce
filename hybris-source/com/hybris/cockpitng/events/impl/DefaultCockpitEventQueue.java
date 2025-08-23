/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.events.impl;

import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.util.CockpitEventUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;

public class DefaultCockpitEventQueue implements CockpitEventQueue
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCockpitEventQueue.class);
    private final Map<String, Set<ListenerInfo>> eventToListenerWidgetMapping = new HashMap<>();
    private final Map<String, Set<CockpitEvent>> eventsForWidgets = new HashMap<>();
    private CockpitUserService cockpitUserService;
    private CockpitProperties cockpitProperties;
    private final Map<String, Long> cleanupWidgets = new HashMap<>();
    private static final long DEFAULT_MAX_TTL = 1800000L;
    protected static final String MAX_TTL_PROPERTY = "cockpitng.event.queue.ttl";


    private void cleanupInvalidWidgets()
    {
        final long currTime = System.currentTimeMillis();
        eventsForWidgets.keySet().forEach(widgetID -> cleanupWidgets.computeIfAbsent(widgetID, k -> currTime));
        final long ttl = getMaxTTL();
        /* free any widgets events reached the max TTL. */
        final Set<String> widgetIDs = new HashSet<>(cleanupWidgets.keySet());
        widgetIDs.forEach(widgetID -> {
            final long duration = currTime - cleanupWidgets.get(widgetID);
            if(duration >= ttl)
            {
                LOG.debug("Clean up the widget({}) since it has reached the max TTL({}ms)!", widgetID, ttl);
                removeListener(widgetID);
            }
        });
    }


    protected long getMaxTTL()
    {
        final int ttlMinutes = getCockpitProperties().getInteger(MAX_TTL_PROPERTY);
        return ttlMinutes > 0 ? TimeUnit.MINUTES.toMillis(ttlMinutes) : DEFAULT_MAX_TTL;
    }


    @Override
    public void publishEvent(final CockpitEvent event)
    {
        synchronized(this)
        {
            final List<ListenerInfo> listenerWidgetsToNotify = findWidgetListenersToNotify(event);
            addEventToAggregatedEventsForWidgets(event, listenerWidgetsToNotify);
            CockpitEventUtils.dispatchGlobalEvents(this);
            cleanupInvalidWidgets();
        }
    }


    protected void addEventToAggregatedEventsForWidgets(final CockpitEvent event, final List<ListenerInfo> listenerWidgetsToNotify)
    {
        if(CollectionUtils.isNotEmpty(listenerWidgetsToNotify))
        {
            synchronized(this)
            {
                for(final ListenerInfo listenerInfo : listenerWidgetsToNotify)
                {
                    final Set<CockpitEvent> set = eventsForWidgets.computeIfAbsent(listenerInfo.getListenerUid(), key -> new LinkedHashSet<>());
                    set.add(event);
                    LOG.debug("{} added to heap of listener {}", event.getName(), listenerInfo);
                }
            }
        }
    }


    protected List<ListenerInfo> findWidgetListenersToNotify(final CockpitEvent event)
    {
        List<ListenerInfo> listenerWidgetsToNotify = null;
        final ScopeContext currentScopeContext = getCurrentScopeContext();
        final Set<ListenerInfo> listenersForEvent = eventToListenerWidgetMapping.get(event.getName());
        LOG.debug("Received event {}", event.getName());
        if(listenersForEvent != null)
        {
            listenerWidgetsToNotify = new ArrayList<>();
            for(final ListenerInfo listenerInfo : listenersForEvent)
            {
                if(checkScope(currentScopeContext, listenerInfo))
                {
                    listenerWidgetsToNotify.add(listenerInfo);
                }
            }
        }
        return listenerWidgetsToNotify;
    }


    protected boolean checkScope(final ScopeContext currentContext, final ListenerInfo listenerInfo)
    {
        final boolean isApplicationScope = CockpitEvent.APPLICATION.equals(listenerInfo.getScope());
        final boolean isUserScope = CockpitEvent.USER.equals(listenerInfo.getScope())
                        && Objects.equals(currentContext.getUserID(), listenerInfo.getScopeContext().getUserID());
        final boolean isSessionScope = CockpitEvent.SESSION.equals(listenerInfo.getScope())
                        && Objects.equals(currentContext.getSessionID(), listenerInfo.getScopeContext().getSessionID());
        final boolean isDesktopScope = CockpitEvent.DESKTOP.equals(listenerInfo.getScope())
                        && Objects.equals(currentContext.getDesktopID(), listenerInfo.getScopeContext().getDesktopID());
        return isApplicationScope || isUserScope || isSessionScope || isDesktopScope;
    }


    protected ScopeContext getCurrentScopeContext()
    {
        String desktopID = null;
        String sessionID = null;
        String userID = null;
        try
        {
            desktopID = Executions.getCurrent().getDesktop().getId();
            sessionID = String.valueOf(Sessions.getCurrent().hashCode());
            userID = cockpitUserService.getCurrentUser();
        }
        catch(final Exception e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Could not retrieve scope info, assuming application scope.", e);
            }
        }
        return new ScopeContext(desktopID, sessionID, userID);
    }


    @Override
    public List<CockpitEvent> fetchEvents(final String widgetID)
    {
        synchronized(this)
        {
            final Set<CockpitEvent> set = eventsForWidgets.remove(widgetID);
            cleanupWidgets.remove(widgetID);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("{} events were dispatched to widgetComponent {}", set != null ? set.size() : 0, widgetID);
            }
            return set != null ? new ArrayList<>(set) : Collections.emptyList();
        }
    }


    @Override
    public void registerListener(final String widgetID, final String eventName, final String scope)
    {
        synchronized(this)
        {
            final Set<ListenerInfo> listenerSet = eventToListenerWidgetMapping.computeIfAbsent(eventName, key -> new HashSet<>());
            listenerSet.add(new ListenerInfo(widgetID, StringUtils.isNotBlank(scope) ? scope : CockpitEvent.APPLICATION,
                            getCurrentScopeContext()));
        }
    }


    protected Set<ListenerInfo> findListenersByName(final String eventName)
    {
        final Set<ListenerInfo> listeners = eventToListenerWidgetMapping.get(eventName);
        return listeners == null ? Collections.emptySet() : Collections.unmodifiableSet(listeners);
    }


    protected Set<CockpitEvent> findEventsForWidgets(final String widgetId)
    {
        final Set<CockpitEvent> events = eventsForWidgets.get(widgetId);
        return events == null ? Collections.emptySet() : Collections.unmodifiableSet(events);
    }


    /**
     * <b>This implementation is async and therefore the calling side should not rely on it being effective immediately.</b>
     * {@inheritDoc}
     */
    @Override
    public void removeListener(final String widgetID)
    {
        synchronized(this)
        {
            eventsForWidgets.remove(widgetID);
            cleanupWidgets.remove(widgetID);
            for(final Entry<String, Set<ListenerInfo>> mapping : eventToListenerWidgetMapping.entrySet())
            {
                final Set<ListenerInfo> listenerSet = mapping.getValue();
                final Set<ListenerInfo> toRemove = listenerSet.stream()
                                .filter(listenerInfo -> widgetID.equals(listenerInfo.getListenerUid())).collect(Collectors.toSet());
                listenerSet.removeAll(toRemove);
            }
            LOG.debug("Remove widget listener({})", widgetID);
        }
    }


    public void setCockpitUserService(final CockpitUserService cockpitUserService)
    {
        this.cockpitUserService = cockpitUserService;
    }


    protected CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }
}
