/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.components.Widgetchildren;
import com.hybris.cockpitng.util.CockpitSessionService;
import java.util.Map;
import java.util.function.BiPredicate;
import org.springframework.beans.factory.annotation.Required;

public class AdminModeCondition implements BiPredicate<Widgetchildren, Map<String, Object>>
{
    private CockpitSessionService sessionService;


    protected boolean isAdminMode()
    {
        return Boolean.TRUE.equals(getSessionService().getAttribute("cockpitAdminMode"));
    }


    @Override
    public boolean test(final Widgetchildren widgetchildren, final Map<String, Object> stringObjectMap)
    {
        return isAdminMode();
    }


    @Required
    public void setSessionService(final CockpitSessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    protected CockpitSessionService getSessionService()
    {
        return sessionService;
    }
}
