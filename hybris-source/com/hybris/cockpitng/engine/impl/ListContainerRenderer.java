/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.components.Widgetchildren;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.impl.NotificationStack;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.engine.ModalWindowStack;
import com.hybris.cockpitng.util.UITools;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

public class ListContainerRenderer extends AbstractChildrenContainerRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(ListContainerRenderer.class);
    public static final String CLOSE_LISTENER_SETTING = "closeListener";
    private String defaultWindowWidth;
    private String defaultWindowHeight;
    private boolean defaultHighlightedMode;
    /**
     * @deprecated since 2005 not used anymore, the logic responsible is called in {@link DefaultListContainerCloseListener}
     */
    @Deprecated(since = "2005", forRemoval = true)
    private NotificationStack notificationStack;
    private ModalWindowStack modalWindowStack;
    private String defaultCloseListenerBeanName;


    @Override
    public void render(final Widgetchildren childrenComponent, final List<WidgetInstance> children, final Map<String, Object> ctx)
    {
        final Component container = getOrCreateContainer(childrenComponent);
        final List<Component> componentList = new ArrayList<>();
        final Set<Component> used = new HashSet<>();
        final List<Widgetslot> newWidgetslots = new ArrayList<>();
        try
        {
            for(final WidgetInstance child : children)
            {
                // check if instance already placed
                final Component component = getComponentFor(child, container);
                if(component != null)
                {
                    used.add(component);
                    componentList.add(component);
                    continue;
                }
                // instance not placed in this container yet
                final Widgetslot widgetContainer = new Widgetslot();
                newWidgetslots.add(widgetContainer);
                if(child.getWidget().isTemplate() && !Boolean.TRUE.equals(ctx.get("adminMode"))
                                && !child.getWidget().getWidgetSettings().getBoolean("_isStubWidget"))
                {
                    final Window window = createWindow(widgetContainer, child);
                    container.appendChild(window);
                    componentList.add(window);
                    used.add(window);
                    addWidget(window, widgetContainer, childrenComponent, child);
                    final Object highlightedMode = child.getWidget().getWidgetSettings().get("_highlightedMode");
                    if(Boolean.FALSE.equals(highlightedMode))
                    {
                        window.doOverlapped();
                    }
                    else
                    {
                        window.doHighlighted();
                    }
                }
                else
                {
                    componentList.add(widgetContainer);
                    used.add(widgetContainer);
                    addWidget(container, widgetContainer, childrenComponent, child);
                }
            }
            // remove components holding removed instances
            final List<Component> childComponents = new ArrayList<>(container.getChildren());
            childComponents.stream()//
                            .filter(component -> !used.contains(component))//
                            .forEach(container::removeChild);
            // align the order of components according to the order of instances
            syncComponentOrder(container.getChildren(), componentList);
            // call after compose on new widget slots finally
            newWidgetslotsAfterCompose(newWidgetslots);
        }
        catch(final Exception e)
        {
            LOG.error("Error while trying to render widgetchildren for widget '"
                            + childrenComponent.getParentWidgetInstance().getId() + "'. Reason: ", e);
        }
    }


    protected Window createWindow(final Widgetslot widgetContainer, final WidgetInstance widgetInstance)
    {
        final Window window = new Window();
        UITools.modifySClass(window, "yw-modal-responsive", widgetInstance.getWidget().getWidgetInstanceSettings().isResponsive());
        UITools.modifySClass(window, "yw-modal-" + widgetInstance.getWidget().getId(), true);
        final Caption wndCaption = new Caption();
        wndCaption.setLabel(getLocalizedTitle(widgetInstance));
        window.appendChild(wndCaption);
        addTitleChangeListener(widgetContainer, event -> wndCaption.setLabel(String.valueOf(event.getData())));
        window.setClosable(true);
        window.addEventListener(Events.ON_Z_INDEX,
                        event -> window.setZIndex(modalWindowStack.getModalWindowZIndex(widgetInstance)));
        window.addEventListener(Events.ON_CLOSE, event -> {
            final TypedSettingsMap widgetSettings = widgetInstance.getWidget().getWidgetSettings();
            final ListContainerCloseListener closeListener = resolveCloseListener(widgetSettings);
            closeListener.onClose(event, widgetInstance);
        });
        final String width = widgetInstance.getWidget().getWidgetSettings().getString("_width");
        final String height = widgetInstance.getWidget().getWidgetSettings().getString("_height");
        window.setWidth(width == null ? defaultWindowWidth : width);
        window.setHeight(height == null ? defaultWindowHeight : height);
        return window;
    }


    private static void addWidget(final Component parent, final Widgetslot widgetContainer, final Widgetchildren childrenComponent,
                    final WidgetInstance child)
    {
        widgetContainer.setWidgetInstance(child);
        widgetContainer.setParentChildrenContainer(childrenComponent);
        parent.appendChild(widgetContainer);
    }


    private static Component getOrCreateContainer(final Widgetchildren widgetChildren)
    {
        final List<Component> children = widgetChildren.getChildren();
        if(children.isEmpty())
        {
            final Div result = new Div();
            widgetChildren.appendChild(result);
            result.setSclass("widget_children_list");
            return result;
        }
        else
        {
            return children.iterator().next();
        }
    }


    /**
     * Resolves {@link ListContainerCloseListener} bean. If not specified differently in the widget settings, it will be
     * resolved to {@link DefaultListContainerCloseListener}.
     *
     * @param widgetSettings
     *           widget settings
     * @return instance of {@link ListContainerCloseListener}
     */
    protected ListContainerCloseListener resolveCloseListener(final TypedSettingsMap widgetSettings)
    {
        final String closeListenerSpecifiedBySetting = widgetSettings.getString(CLOSE_LISTENER_SETTING);
        final String closeListenerBeanName = StringUtils.isBlank(closeListenerSpecifiedBySetting) ? defaultCloseListenerBeanName
                        : closeListenerSpecifiedBySetting;
        return SpringUtil.getApplicationContext().getBean(closeListenerBeanName, ListContainerCloseListener.class);
    }


    public String getDefaultCloseListenerBeanName()
    {
        return defaultCloseListenerBeanName;
    }


    @Required
    public void setDefaultCloseListenerBeanName(final String defaultCloseListenerBeanName)
    {
        this.defaultCloseListenerBeanName = defaultCloseListenerBeanName;
    }


    public String getDefaultWindowWidth()
    {
        return defaultWindowWidth;
    }


    public void setDefaultWindowWidth(final String defaultWindowWidth)
    {
        this.defaultWindowWidth = defaultWindowWidth;
    }


    public String getDefaultWindowHeight()
    {
        return defaultWindowHeight;
    }


    public void setDefaultWindowHeight(final String defaultWindowHeight)
    {
        this.defaultWindowHeight = defaultWindowHeight;
    }


    public boolean isDefaultHighlightedMode()
    {
        return defaultHighlightedMode;
    }


    public void setDefaultHighlightedMode(final boolean defaultHighlightedMode)
    {
        this.defaultHighlightedMode = defaultHighlightedMode;
    }


    @Deprecated(since = "2005", forRemoval = true)
    @Required
    public void setNotificationStack(final NotificationStack notificationStack)
    {
        this.notificationStack = notificationStack;
    }


    @Required
    public void setModalWindowStack(final ModalWindowStack modalWindowStack)
    {
        this.modalWindowStack = modalWindowStack;
    }
}
