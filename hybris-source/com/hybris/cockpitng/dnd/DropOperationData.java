/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dnd;

/**
 * Responsible for storing objects used in "single" drop operation. Single means that only one object is modified.
 *
 * @param <DRAGGED>
 *           dragged objects type.
 * @param <TARGET>
 *           target object type.
 * @param <MODIFIED>
 *           modified objects type.
 */
public class DropOperationData<DRAGGED, TARGET, MODIFIED>
{
    private final DRAGGED dragged;
    private final TARGET target;
    private final MODIFIED modified;
    private final DragAndDropContext context;
    private final String notificationKey;


    public DropOperationData(final DRAGGED dragged, final TARGET target, final MODIFIED modified, final DragAndDropContext context, final String notificationKey)
    {
        this.dragged = dragged;
        this.target = target;
        this.modified = modified;
        this.context = context;
        this.notificationKey = notificationKey;
    }


    public DropOperationData(final DRAGGED dragged, final TARGET target, final DragAndDropContext context)
    {
        this.dragged = dragged;
        this.target = target;
        this.modified = null;
        this.context = context;
        this.notificationKey = null;
    }


    public DRAGGED getDragged()
    {
        return dragged;
    }


    public TARGET getTarget()
    {
        return target;
    }


    public MODIFIED getModified()
    {
        return modified;
    }


    public DragAndDropContext getContext()
    {
        return context;
    }


    public String getNotificationKey()
    {
        return notificationKey;
    }
}
