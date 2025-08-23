/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetController;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.core.util.impl.WidgetTreeUtils;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.engine.impl.DefaultWidgetInstanceManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.zkoss.xel.VariableResolver;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;

/**
 * Utility class used by the cockpit engine to setup instances of {@link WidgetController}.
 */
public final class WidgetControllers
{
    public static final char GROUP_CHILD_SETTINGS_SEPARATOR = '/';
    private static final Logger LOG = LoggerFactory.getLogger(WidgetControllers.class);
    private static final String WIDGET_STYLE_ATTRIBUTE = "widgetStyleAttribute";
    private static final String WIDGET_STYLE_CLASS = "widgetStyleClass";


    private WidgetControllers()
    {
        throw new AssertionError("Utility class should not be instantiated");
    }


    public static void initSettings(final WidgetInstance widgetInstance, final Widgetslot slot,
                    final CockpitComponentDefinitionService componentDefinitionService)
    {
        final Widget widget = widgetInstance.getWidget();
        final TypedSettingsMap widgetSettings = widget.getWidgetSettings();
        final WidgetDefinition definition = slot.getWidgetDefinition(widget);
        if(definition != null)
        {
            final TypedSettingsMap defaultSettings = definition.getDefaultSettings();
            if(defaultSettings != null)
            {
                for(final Map.Entry<String, Object> entry : defaultSettings.entrySet())
                {
                    initWidgetSetting(widgetSettings, entry.getKey(), defaultSettings.get(entry.getKey()));
                    if(defaultSettings.isEnum(entry.getKey()))
                    {
                        widgetSettings.setAvailableValues(entry.getKey(), defaultSettings.getAvailableValues(entry.getKey()));
                    }
                }
            }
            // Expose group children default settings
            final Widget composedWidgetRoot = definition.getComposedWidgetRoot();
            if(composedWidgetRoot != null)
            {
                if(componentDefinitionService == null)
                {
                    LOG.warn("Could not resolve composed widget settings, componentDefinitionService was null.");
                }
                else
                {
                    final Set<Widget> allChildren = new HashSet<>(WidgetTreeUtils.getAllChildWidgetsRecursively(composedWidgetRoot));
                    allChildren.add(composedWidgetRoot);
                    for(final Widget childWidget : allChildren)
                    {
                        final String id = childWidget.getId();
                        final WidgetDefinition widgetDefinition = componentDefinitionService
                                        .getComponentDefinitionForCode(childWidget.getWidgetDefinitionId(), WidgetDefinition.class);
                        final TypedSettingsMap childSettings = widgetDefinition.getDefaultSettings();
                        if(childSettings != null)
                        {
                            for(final Entry<String, Object> entry : childSettings.entrySet())
                            {
                                final String key = entry.getKey();
                                Object value = childWidget.getWidgetSettings().get(key);
                                if(value == null)
                                {
                                    value = childSettings.getTyped(key);
                                }
                                final String groupKey = GROUP_CHILD_SETTINGS_SEPARATOR + id + GROUP_CHILD_SETTINGS_SEPARATOR + key;
                                initWidgetSetting(widgetSettings, groupKey, value, childSettings.getSettingClass(key));
                                if(childSettings.isEnum(key))
                                {
                                    widgetInstance.getWidget().getWidgetSettings().setAvailableValues(groupKey,
                                                    childSettings.getAvailableValues(key));
                                }
                            }
                        }
                    }
                }
            }
        }
        initWidgetSetting(widgetSettings, WIDGET_STYLE_CLASS, StringUtils.EMPTY);
        initWidgetSetting(widgetSettings, WIDGET_STYLE_ATTRIBUTE, StringUtils.EMPTY);
    }


    public static void initWidgetSetting(final TypedSettingsMap settings, final String key, final Object initialValue,
                    final Class settingClass)
    {
        if(!settings.containsKey(key))
        {
            settings.put(key, initialValue, settingClass);
        }
    }


    public static void initWidgetSetting(final TypedSettingsMap settings, final String key, final Object initialValue)
    {
        if(!settings.containsKey(key))
        {
            settings.put(key, initialValue);
        }
    }


    public static void initSettings(final Component comp, final WidgetInstanceManager widgetInstanceManager)
    {
        try
        {
            CockpitComponentDefinitionService componentDefinitionService = null;
            if(widgetInstanceManager instanceof DefaultWidgetInstanceManager)
            {
                componentDefinitionService = ((DefaultWidgetInstanceManager)widgetInstanceManager).getComponentDefinitionService();
            }
            final Widgetslot widgetslot = widgetInstanceManager.getWidgetslot();
            initSettings(widgetslot.getWidgetInstance(), widgetslot, componentDefinitionService);
            if(comp instanceof HtmlBasedComponent)
            {
                String styleAttribute = widgetInstanceManager.getWidgetSettings().getString(WIDGET_STYLE_ATTRIBUTE);
                if(StringUtils.isBlank(styleAttribute))
                {
                    styleAttribute = ((HtmlBasedComponent)comp).getStyle();
                }
                ((HtmlBasedComponent)comp).setStyle(styleAttribute);
                final String styleClass = widgetInstanceManager.getWidgetSettings().getString(WIDGET_STYLE_CLASS);
                if(StringUtils.isNotBlank(styleClass))
                {
                    UITools.modifySClass(((HtmlBasedComponent)comp), styleClass, true);
                }
            }
        }
        catch(final Exception e)
        {
            LOG.error("unable to load widget settings", e);
        }
    }


    /**
     * Adds an input socket listener for messages received by specified widgetslot.
     *
     * @param slot
     *           widget slot
     * @param socketId
     *           identity of socket to be listened to
     * @param listener
     *           listener to be notified about incoming messages
     */
    public static void addWidgetSocketListeners(final Widgetslot slot, final String socketId, final EventListener listener)
    {
        if(!slot.getChildren().isEmpty())
        {
            addWidgetSocketListeners(slot.getChildren().get(0), socketId, listener);
        }
    }


    public static void addWidgetSocketListeners(final Component comp, final String socketId, final EventListener listener)
    {
        final String eventName = "onSocketInput_" + socketId;
        comp.addEventListener(eventName, listener);
    }


    public static void setupWidgetEventListeners(final Component comp, final Object controller,
                    final WidgetInstanceManager widgetInstanceManager, final WidgetUtils widgetUtils)
    {
        final Method[] methods = controller.getClass().getMethods();
        for(final Method method : methods)
        {
            final String socketId = WidgetControllers.getSocketEventIdIfAny(method);
            if(socketId != null)
            {
                final Class<?>[] parameterTypes = method.getParameterTypes();
                if(parameterTypes.length > 1)
                {
                    LOG.error("Could not apply EventListener for '" + method.getDeclaringClass().getName() + "." + method.getName()
                                    + "', method signature doesn't match requirements.");
                }
                else
                {
                    addWidgetSocketListeners(comp, socketId, e -> {
                        try
                        {
                            if(parameterTypes != null && parameterTypes.length == 1)
                            {
                                fireEvent(controller, e, method);
                            }
                            else
                            {
                                fireEvent(controller, null, method);
                            }
                        }
                        catch(final RuntimeException ex)
                        {
                            LOG.error("Failed to properly handle socket event: " + method.toGenericString(), ex);
                        }
                    });
                }
            }
            bindGlobalEventListenerIfPresent(method, controller, widgetInstanceManager, widgetUtils, null);
        }
    }


    public static void bindGlobalEventListenerIfPresent(final Method method, final Object controller,
                    final WidgetInstanceManager widgetInstanceManager, final WidgetUtils widgetUtils,
                    final EventListener<Event> afterInvokeListener)
    {
        final GlobalCockpitEvent cockpitEventAnnotation = method.getAnnotation(GlobalCockpitEvent.class);
        if(cockpitEventAnnotation != null && widgetInstanceManager != null && widgetUtils != null)
        {
            final Class<?>[] parameterTypes = method.getParameterTypes();
            if(parameterTypes != null && parameterTypes.length == 1)
            {
                widgetUtils.addGlobalEventListener(cockpitEventAnnotation.eventName(), widgetInstanceManager.getWidgetslot(),
                                event -> {
                                    if(event.getData() == null || parameterTypes[0].isAssignableFrom(event.getData().getClass()))
                                    {
                                        try
                                        {
                                            method.invoke(controller, event.getData());
                                            if(afterInvokeListener != null)
                                            {
                                                afterInvokeListener.onEvent(event);
                                            }
                                        }
                                        catch(final InvocationTargetException ite)
                                        {
                                            LOG.error("Error when calling controller callback", ite);
                                        }
                                    }
                                    else if(event.getData() != null)
                                    {
                                        LOG.error("Global event listener method '" + method
                                                        + "' has wrong parameter type. Expected type assignable from '" + event.getData().getClass()
                                                        + "' but got '" + parameterTypes[0] + "'.");
                                    }
                                }, cockpitEventAnnotation.scope());
            }
            else
            {
                LOG.error(
                                "Could not apply GlobalEventListener for '{}', method signature doesn't match requirements.", method);
            }
        }
    }


    public static void wireModuleApplicationContext(final ApplicationContext appCtx, final Object controller, final Page page)
    {
        final VariableResolver varRes = name -> {
            try
            {
                return appCtx.getBean(name);
            }
            catch(final BeansException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.warn(String.format("Could not get bean '%s' from module application context, reason: ", name), e);
                }
                else
                {
                    LOG.warn(String.format("Could not get bean '%s' from module application context, reason: ", name));
                }
                return null;
            }
        };
        Selectors.wireVariables(page, controller, Collections.singletonList(varRes));
    }


    public static String getSocketEventIdIfAny(final Method method)
    {
        final SocketEvent socketAnnotation = method.getAnnotation(SocketEvent.class);
        if(socketAnnotation != null)
        {
            final String socketId = socketAnnotation.socketId();
            return StringUtils.isBlank(socketId) ? method.getName() : socketId;
        }
        return null;
    }


    public static void fireEvent(final Object controller, final Event event, final Method method)
                    throws InvocationTargetException, IllegalAccessException
    {
        if(event == null)
        {
            method.invoke(controller);
        }
        else if(Event.class.equals(method.getParameterTypes()[0]))
        {
            method.invoke(controller, event);
        }
        else
        {
            method.invoke(controller, event.getData());
        }
    }
}
