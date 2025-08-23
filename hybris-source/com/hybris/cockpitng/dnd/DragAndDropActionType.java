/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dnd;

/**
 * In case when dragged object is dropped on collection, this action type specifies whether dropped object should be
 * just added to existing collection or should clear the collection before adding.
 */
public enum DragAndDropActionType
{
    /**
     * In case when dragged object is dropped on collection, this action replaces old elements by the new one.
     */
    REPLACE,
    /**
     * In case when dragged object is dropped on collection, this action appends the new object to existing collection.
     */
    APPEND
}
