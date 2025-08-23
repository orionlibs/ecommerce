/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.impl;

import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultNotificationStack implements NotificationStack, Serializable
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultNotificationStack.class);
    private static final String WIDGET_NOTIFICATION_AREA_ID = "com.hybris.backoffice.notificationarea";
    transient Stack<TemplateAndDynamicId> stack = new Stack<>();


    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException
    {
        objectInputStream.defaultReadObject();
        this.stack = new Stack<>();
    }


    private static class TemplateAndDynamicId implements Serializable
    {
        private final String uuid;
        private final String templateWidgetInstanceId;


        private TemplateAndDynamicId(final String uuid, final String templateWidgetInstanceId)
        {
            this.uuid = uuid;
            this.templateWidgetInstanceId = templateWidgetInstanceId;
        }


        public String getUuid()
        {
            return uuid;
        }


        public String getTemplateWidgetInstanceId()
        {
            return templateWidgetInstanceId;
        }
    }


    @Override
    public void onNewTemplateDisplayed(final WidgetInstance wi)
    {
        final Widget notificationWidget = findChildNotificationWidget(wi.getWidget());
        if(notificationWidget != null && notificationWidget.getWidgetSettings().getBoolean("useDynamicNotificationId"))
        {
            final String uuid = UUID.randomUUID().toString();
            stack.push(new TemplateAndDynamicId(uuid, wi.getId()));
            updateDynamicNotificationId(notificationWidget, wi, uuid);
            LOG.debug("new template displayed {}", wi.getId());
        }
    }


    @Override
    public void onTemplateClosed(final WidgetInstance wi)
    {
        if(!stack.isEmpty())
        {
            final TemplateAndDynamicId peek = stack.peek();
            if(peek.getTemplateWidgetInstanceId().equals(wi.getId()))
            {
                stack.pop();
                LOG.debug("new template closed {}", wi.getId());
            }
        }
    }


    private static Widget findChildNotificationWidget(final Widget wi)
    {
        if(wi.getComposedRootInstance() != null)// group widget
        {
            return findChildNotificationWidget(wi.getComposedRootInstance());
        }
        else
        {
            final Optional<Widget> notifWidget = wi.getChildren().stream()
                            .filter(child -> WIDGET_NOTIFICATION_AREA_ID.equals(child.getWidgetDefinitionId())).findFirst();
            if(notifWidget.isPresent())
            {
                return notifWidget.get();
            }
            for(final Widget childrenWidget : wi.getChildren())
            {
                final Widget notifiacationWidget = findChildNotificationWidget(childrenWidget);
                if(notifiacationWidget != null)
                {
                    return notifiacationWidget;
                }
            }
        }
        return null;
    }


    @Override
    public String getTopmostId()
    {
        return !stack.isEmpty() ? stack.peek().getUuid() : null;
    }


    @Override
    public String getPreviousId()
    {
        return stack.size() > 1 ? stack.get(stack.size() - 2).getUuid() : null;
    }


    @Override
    public void resetNotifierStack()
    {
        if(!stack.isEmpty())
        {
            stack.removeAllElements();
        }
    }


    private static void updateDynamicNotificationId(final Widget notificationWidget, final WidgetInstance templateWidgetInstance,
                    final String uuid)
    {
        final List<WidgetInstance> notificationWidgetInstances = notificationWidget.getWidgetInstances();
        final WidgetInstance notificationWidgetInstance = findNotificationWidgetInstanceForGivenTemplateInstance(
                        templateWidgetInstance, notificationWidgetInstances);
        if(notificationWidgetInstance != null)
        {
            final Object model = notificationWidgetInstance.getModel();
            if(model instanceof Map)
            {
                ((Map)(model)).put("dynamicNotificationId", uuid);
            }
        }
    }


    private static WidgetInstance findNotificationWidgetInstanceForGivenTemplateInstance(final WidgetInstance rootWi,
                    final List<WidgetInstance> instanceList)
    {
        for(final WidgetInstance widgetInstance : instanceList)
        {
            if(isParent(widgetInstance, rootWi))
            {
                return widgetInstance;
            }
        }
        return null;
    }


    private static boolean isParent(final WidgetInstance thisWi, final WidgetInstance rootWi)
    {
        if(thisWi.getParent() == null)
        {
            return false;
        }
        if(thisWi.getParent() == rootWi)
        {
            return true;
        }
        return isParent(thisWi.getParent(), rootWi);
    }
}
