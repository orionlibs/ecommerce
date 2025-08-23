/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Widgetchildren;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEvents;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.engine.impl.DashboardContainerRenderer;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;

public class DashboardWidgetController extends DefaultWidgetController
{
    @Wire
    private Div innerContainer;
    @Wire
    private Button toggleLock;
    @Wire
    private Widgetchildren childrenContainer;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        final boolean dndEnable = isDndEnabled();
        toggleLock.setLabel(dndEnable ? getLabel("toggle.lock.lock") : getLabel("toggle.lock.unlock"));
        UITools.modifySClass(toggleLock, "yw-dashboard-unlocked", dndEnable);
    }


    protected boolean isDndEnabled()
    {
        return Boolean.TRUE.equals(getValue(DashboardContainerRenderer.DND_ENABLED_MODEL_KEY, Object.class));
    }


    @ViewEvent(eventName = Events.ON_AFTER_SIZE, componentID = "container")
    public void onViewInfo()
    {
        innerContainer.invalidate();
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = "toggleLock")
    public void unlockDnd()
    {
        final boolean editEnabled = !isDndEnabled();
        Events.echoEvent(DashboardContainerRenderer.ON_CHANGE_DND_SUPPORT, childrenContainer, editEnabled);
        toggleLock.setLabel(editEnabled ? getLabel("toggle.lock.lock") : getLabel("toggle.lock.unlock"));
        UITools.modifySClass(toggleLock, "yw-dashboard-unlocked", editEnabled);
        setValue(DashboardContainerRenderer.DND_ENABLED_MODEL_KEY, editEnabled);
    }


    @GlobalCockpitEvent(eventName = Events.ON_CLIENT_INFO, scope = CockpitEvent.SESSION)
    public void globalClientInfo(final DefaultCockpitEvent globalEvent)
    {
        if(Boolean.FALSE.equals(globalEvent.getContext().get(CockpitEvents.INITIAL_CLIENT_INFO)))
        {
            final Object data = globalEvent.getData();
            if(data instanceof ClientInfoEvent)
            {
                final int width = ((ClientInfoEvent)data).getDesktopWidth();
                final Integer min = getValue(DashboardContainerRenderer.DASHBOARD_GRID_CURRENT_MIN_WIDTH, Integer.class);
                final Integer max = getValue(DashboardContainerRenderer.DASHBOARD_GRID_CURRENT_MAX_WIDTH, Integer.class);
                if(isInRange(width, min, max))
                {
                    childrenContainer.updateView();
                }
            }
        }
    }


    /**
     * Checks if the given number is in the given range.
     *
     * @param num
     *           number to check
     * @param min
     *           minimal value in the range (null = -infinity)
     * @param max
     *           maximal value in the range (null = +infinity)
     * @return true if the value is from the range; false otherwise
     */
    protected boolean isInRange(final int num, final Integer min, final Integer max)
    {
        return (min == null && max == null) || (min == null && max < num) || (max == null && min > num)
                        || (min != null && max != null && (num < min || num > max));
    }


    public Div getInnerContainer()
    {
        return innerContainer;
    }


    public Button getToggleLock()
    {
        return toggleLock;
    }


    public Widgetchildren getChildrenContainer()
    {
        return childrenContainer;
    }
}
