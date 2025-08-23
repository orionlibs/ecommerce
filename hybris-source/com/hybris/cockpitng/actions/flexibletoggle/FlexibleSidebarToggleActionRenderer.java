/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.flexibletoggle;

import static com.hybris.cockpitng.actions.flexibletoggle.FlexibleSidebarToggleAction.PATTERN_RESPONSIVE_BREAKPOINT;
import static com.hybris.cockpitng.actions.flexibletoggle.FlexibleSidebarToggleAction.SETTING_BREAKPOINT_OUTPUT;
import static com.hybris.cockpitng.actions.flexibletoggle.FlexibleSidebarToggleAction.SETTING_RESPONSIVE_BREAKPOINT;
import static com.hybris.cockpitng.actions.flexibletoggle.FlexibleSidebarToggleAction.SETTING_TOGGLE_INPUT;
import static com.hybris.cockpitng.actions.flexibletoggle.FlexibleSidebarToggleAction.VALUE_OUTPUT;
import static com.hybris.cockpitng.widgets.controller.FlexibleLayoutWidgetController.SIDEBAR_STATE_OPENED;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionListener;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.actions.toggle.AbstractToggleActionRenderer;
import com.hybris.cockpitng.components.Action;
import com.hybris.cockpitng.util.UITools;
import java.util.Collection;
import java.util.Objects;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;

public class FlexibleSidebarToggleActionRenderer extends AbstractToggleActionRenderer
{
    private static final String ATTRIBUTE_INITIALIZED = "flexibleInitialized";
    private static final String SCLASS_FLEX_TRIGGER = "yw-flexlayout-trigger";
    private static final String EVENT_ON_LOOPBACK = "onLoopbackEvent";


    @Override
    public void render(final Component parent, final CockpitAction<Object, Object> action, final ActionContext<Object> context,
                    final boolean updateMode, final ActionListener<Object> listener)
    {
        super.render(parent, action, context, updateMode, listener);
        final Action actionComponent = (Action)parent;
        final HtmlBasedComponent container = getOrCreateContainer(parent);
        UITools.modifySClass(container, SCLASS_FLEX_TRIGGER, true);
        final Object breakpoint = context.getParameter(SETTING_RESPONSIVE_BREAKPOINT);
        if(breakpoint != null)
        {
            UITools.modifySClass(container, String.format(PATTERN_RESPONSIVE_BREAKPOINT, breakpoint), true);
            if(context.getParameter(SETTING_BREAKPOINT_OUTPUT) != null && !isBreakpointInitialized(parent, context))
            {
                postponedMessageSend(actionComponent, ObjectUtils.toString(context.getParameter(SETTING_BREAKPOINT_OUTPUT)),
                                breakpoint);
                setBreakpointInitialized(parent, context);
            }
        }
    }


    protected void postponedMessageSend(final Action parent, final String socket, final Object value)
    {
        parent.addEventListener(EVENT_ON_LOOPBACK, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event) throws Exception
            {
                parent.getWidgetInstanceManager().sendOutput(socket, value);
                parent.removeEventListener(EVENT_ON_LOOPBACK, this);
            }
        });
        Events.echoEvent(EVENT_ON_LOOPBACK, parent, null);
    }


    protected boolean isBreakpointInitialized(final Component parent, final ActionContext<Object> actionContext)
    {
        final Object attribute = parent.getAttribute(ATTRIBUTE_INITIALIZED);
        return (attribute instanceof Boolean) && BooleanUtils.isTrue((Boolean)attribute);
    }


    protected void setBreakpointInitialized(final Component parent, final ActionContext<Object> actionContext)
    {
        parent.setAttribute(ATTRIBUTE_INITIALIZED, Boolean.TRUE);
    }


    @Override
    protected boolean getDefaultActiveState(final ActionContext<Object> context)
    {
        return false;
    }


    @Override
    protected Object getOutputValue(final ActionContext<Object> ctx)
    {
        return VALUE_OUTPUT;
    }


    @Override
    protected boolean isInputConfigured(final ActionContext<Object> context)
    {
        return true;
    }


    @Override
    protected String getToggleInput(final ActionContext<Object> context)
    {
        return (String)context.getParameter(SETTING_TOGGLE_INPUT);
    }


    @Override
    protected boolean isActionActivated(final ActionContext<Object> context, final Object inputData)
    {
        if(inputData instanceof Collection)
        {
            return ((Collection<Object>)inputData).stream().anyMatch(data -> Objects.equals(data, SIDEBAR_STATE_OPENED));
        }
        else
        {
            return Objects.equals(inputData, SIDEBAR_STATE_OPENED);
        }
    }
}
