/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.services.dnd;

import com.hybris.cockpitng.dnd.DragAndDropActionType;

/**
 * Stores drag and drop operation configuration.
 */
public interface DragAndDropConfigurationService
{
    DragAndDropActionType getDefaultActionType();


    void setDefaultActionType(DragAndDropActionType actionType);
}
