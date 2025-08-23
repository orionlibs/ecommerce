/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.core.impl.NotificationStack;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.ui.WidgetInstanceFacade;
import com.hybris.cockpitng.engine.ModalWindowStack;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.event.Event;

/**
 * Default listener for 'on close' event of widget templates
 */
public class DefaultListContainerCloseListener implements ListContainerCloseListener
{
    private WidgetInstanceFacade widgetInstanceFacade;
    private NotificationStack notificationStack;
    private ModalWindowStack modalWindowStack;


    @Override
    public void onClose(final Event event, final WidgetInstance widgetInstance)
    {
        getNotificationStack().onTemplateClosed(widgetInstance);
        getModalWindowStack().onWindowClosed(widgetInstance);
        getWidgetInstanceFacade().removeWidgetInstance(widgetInstance);
    }


    public WidgetInstanceFacade getWidgetInstanceFacade()
    {
        return widgetInstanceFacade;
    }


    @Required
    public void setWidgetInstanceFacade(final WidgetInstanceFacade widgetInstanceFacade)
    {
        this.widgetInstanceFacade = widgetInstanceFacade;
    }


    public NotificationStack getNotificationStack()
    {
        return notificationStack;
    }


    @Required
    public void setNotificationStack(final NotificationStack notificationStack)
    {
        this.notificationStack = notificationStack;
    }


    public ModalWindowStack getModalWindowStack()
    {
        return modalWindowStack;
    }


    @Required
    public void setModalWindowStack(final ModalWindowStack modalWindowStack)
    {
        this.modalWindowStack = modalWindowStack;
    }
}
