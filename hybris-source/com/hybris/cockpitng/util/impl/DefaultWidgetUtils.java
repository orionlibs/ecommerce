/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.impl;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.WidgetService;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.model.Identifiable;
import com.hybris.cockpitng.core.persistence.WidgetPersistenceService;
import com.hybris.cockpitng.core.persistence.impl.XMLWidgetPersistenceService;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.util.Resettable;
import com.hybris.cockpitng.util.CockpitEventUtils;
import com.hybris.cockpitng.util.CockpitSessionService;
import com.hybris.cockpitng.util.WidgetUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.DesktopCleanup;
import org.zkoss.zul.Div;

/**
 * Default implementation for {@link WidgetUtils}. For internal use only.
 */
public class DefaultWidgetUtils implements WidgetUtils
{
    private static final String CNG_DESKTOP_WIDGETSLOTMAP = "cng_desktop_widgetslotmap";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWidgetUtils.class);
    private WidgetService widgetService;
    private CockpitComponentDefinitionService widgetDefinitionService;
    private WidgetPersistenceService widgetPersistenceService;
    private XMLWidgetPersistenceService xmlBasedWidgetPersistenceService;
    private WidgetPersistenceService modelBasedWidgetPersistenceService;
    private CockpitSessionService sessionService;
    private CockpitEventQueue cockpitEventQueue;
    private Set<Resettable> elementsToReset;


    @Override
    public void refreshWidgetLibrary()
    {
        widgetDefinitionService.reloadDefinitions();
        clearCachesAndLabels();
    }


    @Override
    public void clearWidgetLibrary()
    {
        widgetDefinitionService.clearDefinitions();
        clearCachesAndLabels();
    }


    private void clearCachesAndLabels()
    {
        resetCaches();
        Labels.reset();
    }


    private void resetCaches()
    {
        if(getElementsToReset() != null)
        {
            LOG.debug("Reset all caches...");
            for(final Resettable resettable : getElementsToReset())
            {
                resettable.reset();
            }
        }
    }


    private void addListenerToDesktop(final Widgetslot widgetslot, final String eventName, final EventListener eventListener)
    {
        if(eventListener == null || eventName == null)
        {
            return;
        }
        Map<String, Map<String, EventListener>> listenersMap;
        try
        {
            listenersMap = (Map<String, Map<String, EventListener>>)Executions.getCurrent().getDesktop()
                            .getAttribute(CockpitEventUtils.GLOBAL_EVENT_LISTENERS_MAP);
        }
        catch(final ClassCastException e)
        {
            LOG.error("Wrong type for attribute '" + CockpitEventUtils.GLOBAL_EVENT_LISTENERS_MAP + "'", e);
            return;
        }
        if(listenersMap == null)
        {
            listenersMap = new ConcurrentHashMap<>();
            Executions.getCurrent().getDesktop().setAttribute(CockpitEventUtils.GLOBAL_EVENT_LISTENERS_MAP, listenersMap);
        }
        final String widgetID = getWidgetUuid(widgetslot, eventListener);
        Map<String, EventListener> listeners4Widget = listenersMap.get(widgetID);
        if(listeners4Widget == null)
        {
            listeners4Widget = new ConcurrentHashMap<>();
            listenersMap.put(widgetID, listeners4Widget);
        }
        listeners4Widget.put(eventName, eventListener);
    }


    private String getWidgetUuid(final Widgetslot widgetslot, final EventListener eventListener)
    {
        final WidgetInstance widgetInstance = widgetslot.getWidgetInstance();
        final Desktop desktop = widgetslot.getDesktop() == null ? Executions.getCurrent().getDesktop() : widgetslot.getDesktop();
        final String desktop_prefix = desktop == null ? null : desktop.getId();
        final String widgetUuid = Optional.ofNullable(widgetInstance).map(WidgetInstance::getId).orElseGet(widgetslot::getUuid);
        final String uuid = Optional.ofNullable(eventListener).filter(Identifiable.class::isInstance).map(Identifiable.class::cast)
                        .map(Identifiable::getId).map(Objects::toString).map("_"::concat).map(widgetUuid::concat).orElse(widgetUuid);
        return desktop_prefix + "_" + uuid;
    }


    @Override
    public void addGlobalEventListener(final String eventName, final Widgetslot widget, final EventListener eventListener,
                    final String scope)
    {
        final String uuid = getWidgetUuid(widget, eventListener);
        cockpitEventQueue.registerListener(uuid, eventName, scope);
        addListenerToDesktop(widget, eventName, eventListener);
        Executions.getCurrent().getDesktop().addListener((DesktopCleanup)desktop -> {
            LOG.debug("Unregister cockpit event listener with " + uuid);
            cockpitEventQueue.removeListener(uuid);
        });
    }


    @Override
    public void dispatchGlobalEvents()
    {
        CockpitEventUtils.dispatchGlobalEvents(cockpitEventQueue);
    }


    @Override
    public Component addNotifierToStack(final Component content)
    {
        final Component root = getRoot();
        return root != null ? addNotifierToStack(content, root.getFellowIfAny(NOTIFIER_STACK)) : null;
    }


    @Override
    public Component addNotifierToStack(final Component content, final Component notifierStack)
    {
        if(notifierStack != null)
        {
            final Div notifierEntry = new Div();
            notifierEntry.setSclass("notifierEntry");
            notifierStack.appendChild(notifierEntry);
            notifierEntry.appendChild(content);
            return notifierEntry;
        }
        return null;
    }


    @Override
    public Widgetslot getRegisteredWidgetslot(final WidgetInstance widgetInstance)
    {
        final Execution execution = Executions.getCurrent();
        final Desktop desktop = execution == null ? null : execution.getDesktop();
        if(widgetInstance == null)
        {
            return null;
        }
        else
        {
            return desktop == null ? null : (Widgetslot)desktop.getAttribute("widget_" + widgetInstance.getId());
        }
    }


    @Override
    public Widgetslot getRegisteredWidgetslot(final String widgetInstanceId)
    {
        final Desktop desktop = Executions.getCurrent().getDesktop();
        return desktop == null ? null : (Widgetslot)desktop.getAttribute("widget_" + widgetInstanceId);
    }


    @Override
    public void registerWidgetslot(final Widgetslot widgetslot)
    {
        final Desktop desktop = Executions.getCurrent().getDesktop();
        final WidgetInstance widgetInstance = widgetslot.getWidgetInstance();
        if(widgetInstance == null)
        {
            LOG.error("Could not register widget view component, widget was null.");
            return;
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Register widget component: " + widgetInstance + " slotid: " + widgetInstance.getWidget().getSlotId());
        }
        desktop.setAttribute("widget_" + widgetInstance.getId(), widgetslot);
    }


    protected Map<String, Widgetslot> getDesktopWidgetslotMap()
    {
        if(Executions.getCurrent() == null || Executions.getCurrent().getDesktop() == null)
        {
            LOG.error("Could not get registered widgetslots");
            return Collections.emptyMap();
        }
        final Desktop desktop = Executions.getCurrent().getDesktop();
        Map<String, Widgetslot> ret = (Map<String, Widgetslot>)desktop.getAttribute(CNG_DESKTOP_WIDGETSLOTMAP);
        if(ret == null)
        {
            ret = new ConcurrentHashMap<>();
            desktop.setAttribute(CNG_DESKTOP_WIDGETSLOTMAP, ret);
        }
        return ret;
    }


    @Override
    public Component getRoot()
    {
        try
        {
            final Execution execution = Executions.getCurrent();
            if(execution != null)
            {
                final Desktop desktop = execution.getDesktop();
                if(desktop != null)
                {
                    final Page firstPage = desktop.getFirstPage();
                    if(firstPage != null)
                    {
                        final Collection roots = firstPage.getRoots();
                        for(final Object object : roots)
                        {
                            if(object instanceof HtmlBasedComponent)
                            {
                                return (Component)object;
                            }
                        }
                    }
                }
            }
        }
        catch(final Exception e)
        {
            LOG.error("Could not find root, reason: ", e);
        }
        return null;
    }


    @Override
    public Component getCockpitRoot()
    {
        return getRoot();
    }


    @Override
    public boolean isPartOfComposedWidget(final Widget widget)
    {
        final WidgetDefinition widgetDefinition = getWidgetDefinition(widget);
        if(widgetDefinition == null)
        {
            LOG.error("Widget '" + widget.getId() + "' has no definition.");
            return false;
        }
        if(widgetDefinition.getComposedWidgetRoot() != null)
        {
            return false;
        }
        Widget node = widget.getParent();
        while(node != null)
        {
            if(getWidgetDefinition(node).getComposedWidgetRoot() != null)
            {
                return true;
            }
            node = node.getParent();
        }
        return false;
    }


    private WidgetDefinition getWidgetDefinition(final Widget widget)
    {
        return (WidgetDefinition)getWidgetDefinitionService().getComponentDefinitionForCode(widget.getWidgetDefinitionId());
    }


    protected CockpitSessionService getSessionService()
    {
        return sessionService;
    }


    @Required
    public void setSessionService(final CockpitSessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public XMLWidgetPersistenceService getXmlBasedWidgetPersistenceService()
    {
        return xmlBasedWidgetPersistenceService;
    }


    public void setXmlBasedWidgetPersistenceService(final XMLWidgetPersistenceService xmlBasedWidgetPersistenceService)
    {
        this.xmlBasedWidgetPersistenceService = xmlBasedWidgetPersistenceService;
    }


    public WidgetPersistenceService getModelBasedWidgetPersistenceService()
    {
        return modelBasedWidgetPersistenceService;
    }


    public void setModelBasedWidgetPersistenceService(final WidgetPersistenceService modelBasedWidgetPersistenceService)
    {
        this.modelBasedWidgetPersistenceService = modelBasedWidgetPersistenceService;
    }


    protected WidgetService getWidgetService()
    {
        return widgetService;
    }


    @Required
    public void setWidgetService(final WidgetService widgetService)
    {
        this.widgetService = widgetService;
    }


    protected WidgetPersistenceService getWidgetPersistenceService()
    {
        return this.widgetPersistenceService;
    }


    @Required
    public void setWidgetPersistenceService(final WidgetPersistenceService widgetPersistenceService)
    {
        this.widgetPersistenceService = widgetPersistenceService;
    }


    private CockpitComponentDefinitionService getWidgetDefinitionService()
    {
        return widgetDefinitionService;
    }


    public void setWidgetDefinitionService(final CockpitComponentDefinitionService widgetDefinitionService)
    {
        this.widgetDefinitionService = widgetDefinitionService;
    }


    @Required
    public void setCockpitEventQueue(final CockpitEventQueue cockpitEventQueue)
    {
        this.cockpitEventQueue = cockpitEventQueue;
    }


    protected Set<Resettable> getElementsToReset()
    {
        return elementsToReset;
    }


    public void setElementsToReset(final Set<Resettable> elementsToReset)
    {
        this.elementsToReset = elementsToReset;
    }
}
