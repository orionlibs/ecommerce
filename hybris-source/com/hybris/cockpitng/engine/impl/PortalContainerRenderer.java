/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.components.Widgetchildren;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.renderers.header.DefaultWidgetCaptionWrapper;
import com.hybris.cockpitng.renderers.header.WidgetCaptionEventListener;
import com.hybris.cockpitng.renderers.header.WidgetCaptionRenderer;
import com.hybris.cockpitng.renderers.header.WidgetCaptionWrapper;
import com.hybris.cockpitng.util.WidgetSlotUtils;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkmax.zul.Portalchildren;
import org.zkoss.zkmax.zul.Portallayout;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

/**
 * Renders a {@link Widgetchildren} component as {@link Portallayout} and its children as {@link Panel}s
 */
public class PortalContainerRenderer extends AbstractChildrenContainerRenderer
{
    @Override
    public void render(final Widgetchildren childrenComponent, final List<WidgetInstance> children, final Map<String, Object> ctx)
    {
        final Portallayout portallayout = new Portallayout();
        childrenComponent.appendChild(portallayout);
        final Portalchildren portalchildren = new Portalchildren();
        portallayout.appendChild(portalchildren);
        portalchildren.setWidth("100%");
        portalchildren.setHeight("100%");
        int index = 0;
        for(final WidgetInstance child : children)
        {
            renderPanel(child, portalchildren, index, childrenComponent);
            index++;
        }
    }


    /**
     * Override to customize how a panel is rendered.
     *
     * @param widgetInstance
     *           The child widget instance to render.
     * @param parent
     *           The parent zk component where the child component is append to.
     * @param index
     *           The index of the child widget.
     * @param childrenComponent
     *           The corresponding {@link Widgetchildren} component.
     */
    protected void renderPanel(final WidgetInstance widgetInstance, final Component parent, final int index,
                    final Widgetchildren childrenComponent)
    {
        final Panel panel = new Panel();
        panel.setSclass("widgetPanel");
        panel.setCollapsible(true);
        panel.setMaximizable(true);
        panel.setClosable(widgetInstance.getWidget().isTemplate());
        panel.setSizable(true);
        panel.setVflex("true");
        panel.addEventListener(Events.ON_CLOSE, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event) throws Exception
            {
                getWidgetInstanceFacade().removeWidgetInstance(widgetInstance);
            }
        });
        panel.setBorder("normal");
        panel.setTitle(getLocalizedTitle(widgetInstance));
        final Widgetslot widgetContainer = new Widgetslot();
        widgetContainer.setWidgetInstance(widgetInstance);
        widgetContainer.setParentChildrenContainer(childrenComponent);
        final Panelchildren panelchildren = new Panelchildren();
        panelchildren.appendChild(widgetContainer);
        panel.appendChild(panelchildren);
        parent.appendChild(panel);
        addTitleChangeListener(widgetContainer, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event) throws Exception
            {
                panel.setTitle(String.valueOf(event.getData()));
            }
        });
        widgetContainer.afterCompose();
        final Object widgetController = widgetContainer.getAttribute(Widgetslot.ATTRIBUTE_WIDGET_CONTROLLER);
        if(widgetController instanceof WidgetCaptionRenderer)
        {
            final WidgetCaptionEventListener listener = new WidgetCaptionEventListener()
            {
                @Override
                public void onEvent(final String eventName, final WidgetCaptionWrapper captionWrapper)
                {
                    if(WidgetCaptionWrapper.ON_WIDGET_COLLAPSE.equals(eventName))
                    {
                        panel.setOpen(!captionWrapper.isCollapsed());
                    }
                    else if(WidgetCaptionWrapper.ON_WIDGET_FOCUS.equals(eventName))
                    {
                        panel.setFocus(captionWrapper.isFocused());
                    }
                    else if(WidgetCaptionWrapper.ON_WIDGET_MINIMIZE.equals(eventName))
                    {
                        panel.setMinimized(captionWrapper.isMinimized());
                    }
                    else if(WidgetCaptionWrapper.ON_WIDGET_MAXIMIZE.equals(eventName))
                    {
                        panel.setMaximized(captionWrapper.isMaximized());
                    }
                    else if(WidgetCaptionWrapper.ON_WIDGET_CLOSE.equals(eventName))
                    {
                        Events.sendEvent(Events.ON_CLOSE, panel, null);
                    }
                }
            };
            final DefaultWidgetCaptionWrapper captionWrapper = new DefaultWidgetCaptionWrapper(listener, false, panel.isMinimized(),
                            panel.isMaximized(), false);
            captionWrapper.setCollapsible(panel.isCollapsible());
            captionWrapper.setClosable(panel.isClosable());
            captionWrapper.setMaximizable(panel.isMaximizable());
            captionWrapper.setMinimizable(panel.isMinimizable());
            panel.addEventListener(Events.ON_OPEN, new EventListener<Event>()
            {
                @Override
                public void onEvent(final Event event)
                {
                    captionWrapper.sendEvent(WidgetCaptionWrapper.ON_WIDGET_COLLAPSE, !panel.isOpen());
                }
            });
            panel.addEventListener(Events.ON_MAXIMIZE, new EventListener<Event>()
            {
                @Override
                public void onEvent(final Event event)
                {
                    captionWrapper.sendEvent(WidgetCaptionWrapper.ON_WIDGET_MAXIMIZE, panel.isMaximized());
                }
            });
            panel.addEventListener(Events.ON_MINIMIZE, new EventListener<Event>()
            {
                @Override
                public void onEvent(final Event event)
                {
                    captionWrapper.sendEvent(WidgetCaptionWrapper.ON_WIDGET_MINIMIZE, panel.isMinimized());
                }
            });
            panel.addEventListener(Events.ON_FOCUS, new EventListener<Event>()
            {
                @Override
                public void onEvent(final Event event)
                {
                    captionWrapper.sendEvent(WidgetCaptionWrapper.ON_WIDGET_FOCUS, true);
                }
            });
            panel.addEventListener(Events.ON_BLUR, new EventListener<Event>()
            {
                @Override
                public void onEvent(final Event event)
                {
                    captionWrapper.sendEvent(WidgetCaptionWrapper.ON_WIDGET_FOCUS, false);
                }
            });
            final Component createCaption = WidgetSlotUtils.createCaption(widgetContainer, "", captionWrapper);
            if(createCaption != null)
            {
                panel.setTitle(null);
                final Caption caption = new Caption();
                panel.appendChild(caption);
                caption.appendChild(createCaption);
            }
            final int controls = captionWrapper.getHiddenContainerControls();
            if(controls > 0)
            {
                if((WidgetCaptionWrapper.CONTROL_COLLAPSE & controls) == WidgetCaptionWrapper.CONTROL_COLLAPSE)
                {
                    panel.setCollapsible(false);
                }
                if((WidgetCaptionWrapper.CONTROL_CLOSE & controls) == WidgetCaptionWrapper.CONTROL_CLOSE)
                {
                    panel.setClosable(false);
                }
                if((WidgetCaptionWrapper.CONTROL_MAXIMIZE & controls) == WidgetCaptionWrapper.CONTROL_MAXIMIZE)
                {
                    panel.setMaximizable(false);
                }
                if((WidgetCaptionWrapper.CONTROL_MINIMIZE & controls) == WidgetCaptionWrapper.CONTROL_MINIMIZE)
                {
                    panel.setMinimizable(false);
                }
            }
        }
    }
}
