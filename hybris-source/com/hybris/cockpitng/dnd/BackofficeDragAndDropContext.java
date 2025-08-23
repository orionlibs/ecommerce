/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dnd;

public interface BackofficeDragAndDropContext extends DragAndDropContext
{
    /**
     * Returns action type resolved for this operation
     * @return {@link DragAndDropActionType}
     */
    DragAndDropActionType getActionType();


    /**
     * Sets action type resolved for this operation
     * @param actionType {@link DragAndDropActionType}
     */
    void setActionType(DragAndDropActionType actionType);
}
