/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin.impl;

import com.hybris.cockpitng.admin.CockpitAdminService;
import com.hybris.cockpitng.admin.WidgetGroupingService;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetService;
import com.hybris.cockpitng.core.persistence.WidgetPersistenceService;
import com.hybris.cockpitng.core.util.impl.WidgetTreeUtils;
import com.hybris.cockpitng.engine.SessionWidgetInstanceRegistry;
import com.hybris.cockpitng.util.WidgetTreeUIUtils;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

/**
 * Default service for grouping and ungrouping widgets
 */
public class DefaultWidgetGroupingService implements WidgetGroupingService
{
    private CockpitAdminService cockpitAdminService;
    private WidgetService widgetService;
    private WidgetPersistenceService widgetPersistenceService;
    private SessionWidgetInstanceRegistry sessionWidgetInstanceRegistry;


    private void setPartOfGroupRecursive(final Widget widget, final boolean value)
    {
        widget.setPartOfGroup(value);
        final List<Widget> children = widget.getChildren();
        if(CollectionUtils.isNotEmpty(children))
        {
            for(final Widget child : children)
            {
                setPartOfGroupRecursive(child, value);
            }
        }
    }


    @Override
    public void groupWidget(final Widgetslot widgetslot)
    {
        final Widget widget = widgetslot.getWidgetInstance().getWidget();
        final String rootWidgetSlot = WidgetTreeUtils.getRootWidget(widget).getSlotId();
        cockpitAdminService.showGroupWidgetWizard(widget, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event) throws Exception
            {
                sessionWidgetInstanceRegistry.unregisterWidgetInstance(rootWidgetSlot);
                widgetslot.setWidgetInstance(null);
                WidgetTreeUIUtils.updateRootWidget(widgetslot, true);
            }
        });
    }


    @Override
    public void ungroupWidget(final Widgetslot widgetslot)
    {
        final Widget widget = widgetslot.getWidgetInstance().getWidget();
        final Widget composedWidget = widget;
        final Widget parent = widget.getParent();
        final Widget composedRoot = widget.getComposedRootInstance();
        widgetService.moveWidget(composedRoot, parent);
        composedRoot.setSlotId(widgetslot.getSlotID());
        composedRoot.setGroupContainer(null);
        setPartOfGroupRecursive(composedRoot, false);
        if(parent == null)
        {
            // Composed widget was the root
            composedRoot.setId(composedWidget.getId()); // sets the main slot id to root widget
            widgetPersistenceService.deleteWidgetTree(composedWidget);
            widgetPersistenceService.storeWidgetTree(composedRoot);
            sessionWidgetInstanceRegistry.clear();
        }
        else
        {
            parent.removeChild(composedWidget);
            widgetPersistenceService.storeWidgetTree(parent);
        }
        cockpitAdminService.removeWidgetFromClipboard(composedWidget);
        WidgetTreeUIUtils.updateRootWidget(widgetslot, true);
    }


    @Required
    public void setCockpitAdminService(final CockpitAdminService cockpitAdminService)
    {
        this.cockpitAdminService = cockpitAdminService;
    }


    @Required
    public void setWidgetService(final WidgetService widgetService)
    {
        this.widgetService = widgetService;
    }


    @Required
    public void setWidgetPersistenceService(final WidgetPersistenceService widgetPersistenceService)
    {
        this.widgetPersistenceService = widgetPersistenceService;
    }


    @Required
    public void setSessionWidgetInstanceRegistry(final SessionWidgetInstanceRegistry sessionWidgetInstanceRegistry)
    {
        this.sessionWidgetInstanceRegistry = sessionWidgetInstanceRegistry;
    }
}
