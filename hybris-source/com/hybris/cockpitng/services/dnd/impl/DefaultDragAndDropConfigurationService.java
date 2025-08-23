/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.services.dnd.impl;

import com.hybris.cockpitng.dnd.DragAndDropActionType;
import com.hybris.cockpitng.services.dnd.DragAndDropConfigurationService;
import com.hybris.cockpitng.util.CockpitSessionService;
import org.springframework.beans.factory.annotation.Required;

/**
 * Stores drag and drop operation configuration.
 */
public class DefaultDragAndDropConfigurationService implements DragAndDropConfigurationService
{
    public static final String PCM_BACKOFFICE_DEFAULT_DND_BEHAVIOUR = "default_backoffice_dnd_behaviour";
    private CockpitSessionService cockpitSessionService;


    @Override
    public DragAndDropActionType getDefaultActionType()
    {
        final Object attribute = getCockpitSessionService().getAttribute(PCM_BACKOFFICE_DEFAULT_DND_BEHAVIOUR);
        if(attribute instanceof DragAndDropActionType)
        {
            return (DragAndDropActionType)attribute;
        }
        final DragAndDropActionType defaultActionType = DragAndDropActionType.REPLACE;
        setDefaultActionType(defaultActionType);
        return defaultActionType;
    }


    @Override
    public void setDefaultActionType(final DragAndDropActionType actionType)
    {
        getCockpitSessionService().setAttribute(PCM_BACKOFFICE_DEFAULT_DND_BEHAVIOUR, actionType);
    }


    public CockpitSessionService getCockpitSessionService()
    {
        return cockpitSessionService;
    }


    @Required
    public void setCockpitSessionService(final CockpitSessionService cockpitSessionService)
    {
        this.cockpitSessionService = cockpitSessionService;
    }
}
