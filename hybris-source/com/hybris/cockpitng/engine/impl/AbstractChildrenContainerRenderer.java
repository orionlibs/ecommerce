/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.ui.WidgetInstanceFacade;
import com.hybris.cockpitng.engine.WidgetChildrenContainerRenderer;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

public abstract class AbstractChildrenContainerRenderer implements WidgetChildrenContainerRenderer
{
    private static final String WIDGET_TITLE = "widget.title";
    private static final String WIDGET_DEFAULT_TITLE = "widget.default.title";
    private static final String UNTITLED = "[untitled]";
    private WidgetInstanceFacade widgetInstanceFacade;


    protected Component getComponentFor(final WidgetInstance child, final Component container)
    {
        for(final Component component : container.getChildren())
        {
            final Widgetslot widgetslot = findWidgetslot(component);
            if(widgetslot != null && child != null && child.equals(widgetslot.getWidgetInstance()))
            {
                return component;
            }
        }
        return null;
    }


    private Widgetslot findWidgetslot(final Component component)
    {
        if(component instanceof Widgetslot)
        {
            return (Widgetslot)component;
        }
        else
        {
            final List<Component> children = component.getChildren();
            for(final Component child : children)
            {
                final Widgetslot slot = findWidgetslot(child);
                if(slot != null)
                {
                    return slot;
                }
            }
        }
        return null;
    }


    protected void syncComponentOrder(final List<Component> componentLiveList, final List<Component> componentSortedList)
    {
        int index = 0;
        for(final Component component : componentSortedList)
        {
            if(index < componentLiveList.size())
            {
                final Component liveComponent = componentLiveList.get(index);
                if(!component.equals(liveComponent) && componentLiveList.remove(component))
                {
                    componentLiveList.add(index, component);
                }
            }
            else if(componentLiveList.remove(component))
            {
                componentLiveList.add(component);
            }
            index++;
        }
    }


    protected void newWidgetslotsAfterCompose(final List<Widgetslot> newWidgetslots)
    {
        if(CollectionUtils.isNotEmpty(newWidgetslots))
        {
            for(final Widgetslot slot : newWidgetslots)
            {
                slot.afterCompose();
            }
        }
    }


    protected void addTitleChangeListener(final Widgetslot slot, final EventListener<Event> listener)
    {
        slot.setAttribute(WidgetInstanceManager.CNG_TITLE_CHANGE_LISTENER, listener);
    }


    protected String getLocalizedTitle(final WidgetInstance widgetInstance)
    {
        final String localizationKey;
        final String widgetTitle = widgetInstance.getWidget().getTitle();
        if(StringUtils.isBlank(widgetTitle))
        {
            localizationKey = widgetInstance.getWidget().getWidgetDefinitionId() + "." + WIDGET_TITLE;
        }
        else
        {
            localizationKey = widgetInstance.getWidget().getWidgetDefinitionId() + "." + StringUtils.trimToEmpty(widgetTitle);
        }
        final String title = Labels.getLabel(localizationKey, widgetTitle);
        return (StringUtils.isBlank(title) ? Labels.getLabel(WIDGET_DEFAULT_TITLE, UNTITLED) : title);
    }


    @Required
    public void setWidgetInstanceFacade(final WidgetInstanceFacade widgetInstanceFacade)
    {
        this.widgetInstanceFacade = widgetInstanceFacade;
    }


    protected WidgetInstanceFacade getWidgetInstanceFacade()
    {
        return widgetInstanceFacade;
    }
}
