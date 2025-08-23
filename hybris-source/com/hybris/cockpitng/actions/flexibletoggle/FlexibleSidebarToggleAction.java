/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.flexibletoggle;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.toggle.AbstractToggleAction;

public class FlexibleSidebarToggleAction extends AbstractToggleAction
{
    public static final String PATTERN_RESPONSIVE_BREAKPOINT = "yw-flexlayout-responsive-breakpoint-%s";
    protected static final String DEFAULT_OUTPUT_SOCKET = "toggle";
    protected static final String SETTING_OUTPUT_SOCKET = "toggleSocket";
    protected static final String SETTING_TOGGLE_INPUT = "stateInput";
    protected static final String SETTING_BREAKPOINT_OUTPUT = "breakpointOutput";
    protected static final String SETTING_RESPONSIVE_BREAKPOINT = "breakpoint";
    protected static final Object VALUE_OUTPUT = "toggle";


    @Override
    protected String getOutputSocket(final ActionContext<Object> ctx)
    {
        final String socket;
        if(ctx.getParameterKeys().contains(SETTING_OUTPUT_SOCKET))
        {
            socket = (String)ctx.getParameter(SETTING_OUTPUT_SOCKET);
        }
        else
        {
            socket = getDefaultOutputSocket();
        }
        return socket;
    }


    protected String getDefaultOutputSocket()
    {
        return DEFAULT_OUTPUT_SOCKET;
    }
}
