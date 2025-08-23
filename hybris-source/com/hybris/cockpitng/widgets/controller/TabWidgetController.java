/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.components.Widgetchildren;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.ui.WidgetInstanceFacade;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Tabbox;

public class TabWidgetController extends DefaultWidgetController
{
    public static final String CLOSE_ALL_INPUT_SOCKET = "closeAll";
    public static final String SOCKET_OUT_SELECTED_WIDGET_ID = "selectedWidgetId";
    public static final String SETTING_TAB_SCROLLABLE = "tabScrollable";
    private static final long serialVersionUID = 6564349837929943458L;
    private transient WidgetInstanceFacade widgetInstanceFacade;
    private Widgetchildren tabContainer;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        tabContainer.addEventListener(Events.ON_SELECT, event -> {
            if(event.getData() instanceof String)
            {
                onTabSelected((String)event.getData());
            }
        });
        if(StringUtils.equals(tabContainer.getType(), Widgetchildren.TAB))
        {
            Optional.ofNullable(tabContainer.getFirstChild()).filter(Tabbox.class::isInstance).map(Tabbox.class::cast)
                            .ifPresent(tabbox -> tabbox.setTabscroll(isTabScrollable()));
        }
    }


    protected boolean isTabScrollable()
    {
        return getWidgetSettings().getBoolean(SETTING_TAB_SCROLLABLE);
    }


    public void onTabSelected(final String selectedWidgetId)
    {
        sendOutput(SOCKET_OUT_SELECTED_WIDGET_ID, selectedWidgetId);
    }


    @SocketEvent(socketId = CLOSE_ALL_INPUT_SOCKET)
    public void closeAll()
    {
        final List<WidgetInstance> instances = widgetInstanceFacade.getWidgetInstances(getWidgetslot().getWidgetInstance(),
                        tabContainer.getSlotID(), false);
        for(final WidgetInstance instance : instances)
        {
            if(instance.getWidget().isTemplate())
            {
                widgetInstanceFacade.removeWidgetInstance(instance);
            }
        }
        tabContainer.updateChildren();
    }


    public WidgetInstanceFacade getWidgetInstanceFacade()
    {
        return widgetInstanceFacade;
    }


    public Widgetchildren getTabContainer()
    {
        return tabContainer;
    }
}
