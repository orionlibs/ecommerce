package de.hybris.platform.cockpit.events.impl;

import de.hybris.platform.cockpit.model.undo.UndoableOperation;

public class UndoAddedEvent extends AbstractUndoRedoEvent
{
    public UndoAddedEvent(Object source, UndoableOperation operation)
    {
        super(source, operation);
    }
}
