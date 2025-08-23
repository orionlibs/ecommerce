/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.services.impl;

import com.hybris.backoffice.services.PerspectiveInfoService;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.CockpitSessionService;
import java.io.Serializable;
import java.util.Objects;

/**
 * Default implementation of the {@link PerspectiveInfoService}.
 */
public class DefaultPerspectiveInfoService implements PerspectiveInfoService, Serializable
{
    /**
     * Name of attribute, in Cockpit session service, which stores identifier of selected perspective.
     */
    protected static final String SELECTED_ID_ATTRIBUTE_NAME = DefaultPerspectiveInfoService.class.getName() + "#selectedId";
    private transient CockpitSessionService sessionService;


    /**
     * Constructs a new instance.
     *
     * @param sessionService
     *           the Cockpit session service (cannot be {@code null}).
     * @deprecated since 6.7 no longer used. Please use setter injection
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public DefaultPerspectiveInfoService(final CockpitSessionService sessionService)
    {
        Objects.requireNonNull(sessionService);
        this.sessionService = sessionService;
    }


    public DefaultPerspectiveInfoService()
    {
    }


    @Override
    public boolean hasSelectedId()
    {
        return getSessionService().getAttribute(SELECTED_ID_ATTRIBUTE_NAME) != null;
    }


    @Override
    public String getSelectedId()
    {
        return (String)getSessionService().getAttribute(SELECTED_ID_ATTRIBUTE_NAME);
    }


    @Override
    public void setSelectedId(final String id)
    {
        if(id == null)
        {
            getSessionService().removeAttribute(SELECTED_ID_ATTRIBUTE_NAME);
        }
        else
        {
            getSessionService().setAttribute(SELECTED_ID_ATTRIBUTE_NAME, id);
        }
    }


    public CockpitSessionService getSessionService()
    {
        if(sessionService == null)
        {
            sessionService = BackofficeSpringUtil.getBean("cockpitSessionService");
        }
        return sessionService;
    }
}
