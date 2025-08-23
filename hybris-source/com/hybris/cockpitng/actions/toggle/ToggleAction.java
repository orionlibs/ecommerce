/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.toggle;

import com.hybris.cockpitng.actions.ActionContext;

public class ToggleAction extends AbstractToggleAction
{
    protected static final String DEFAULT_OUTPUT_SOCKET = "output";
    protected static final String SETTING_OUTPUT_SOCKET = "outputSocket";
    protected static final String SETTING_OUTPUT_VALUE = "outputValue";
    protected static final String SETTING_DEFAULT_ACTIVE = "defaultActive";
    protected static final String SETTING_TOGGLE_INPUT = "toggleInput";
    protected static final String SETTING_TOGGLE_CONDITION = "toggleCondition";


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
