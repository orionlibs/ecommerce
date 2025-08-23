/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dnd;

import com.hybris.cockpitng.core.context.CockpitContext;
import com.hybris.cockpitng.core.context.impl.DefaultCockpitContext;
import com.hybris.cockpitng.mouse.MouseKeys;
import java.util.Collections;
import java.util.Set;

public class DefaultDragAndDropContext extends DefaultCockpitContext implements BackofficeDragAndDropContext
{
    private final Set<MouseKeys> keys;
    private final CockpitContext draggedContext;
    private final CockpitContext targetContext;
    private DragAndDropActionType actionType;


    private DefaultDragAndDropContext(final CockpitContext context, final Set<MouseKeys> keys,
                    final CockpitContext draggedContext, final CockpitContext targetContext)
    {
        DefaultDragAndDropContext.this.setParameters(context.getParameters());
        this.keys = keys;
        this.draggedContext = draggedContext;
        this.targetContext = targetContext;
    }


    @Override
    public Set<MouseKeys> getKeys()
    {
        return keys;
    }


    @Override
    public CockpitContext getDraggedContext()
    {
        return draggedContext;
    }


    @Override
    public CockpitContext getTargetContext()
    {
        return targetContext;
    }


    @Override
    public DragAndDropActionType getActionType()
    {
        return actionType;
    }


    @Override
    public void setActionType(final DragAndDropActionType actionType)
    {
        this.actionType = actionType;
    }


    public static class Builder
    {
        private Set<MouseKeys> keys;
        private CockpitContext context;
        private CockpitContext draggedContext;
        private CockpitContext targetContext;


        public Builder()
        {
            this.keys = Collections.emptySet();
            this.context = new DefaultCockpitContext();
            this.draggedContext = new DefaultCockpitContext();
            this.targetContext = new DefaultCockpitContext();
        }


        public Builder withKeys(final Set<MouseKeys> keys)
        {
            this.keys = keys;
            return this;
        }


        public Builder withContext(final CockpitContext context)
        {
            this.context = context;
            return this;
        }


        public Builder withDraggedContext(final CockpitContext draggedContext)
        {
            this.draggedContext = draggedContext;
            return this;
        }


        public Builder withTargetContext(final CockpitContext targetContext)
        {
            this.targetContext = targetContext;
            return this;
        }


        public DefaultDragAndDropContext build()
        {
            return new DefaultDragAndDropContext(context, keys, draggedContext, targetContext);
        }
    }
}
