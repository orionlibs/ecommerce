package de.hybris.platform.cockpit.events.impl;

import de.hybris.platform.cockpit.model.undo.UndoableOperation;

public class AbstractUndoRedoEvent extends AbstractCockpitEvent
{
    private final UndoableOperation operation;


    public AbstractUndoRedoEvent(Object source, UndoableOperation operation)
    {
        super(source);
        this.operation = operation;
    }


    public UndoableOperation getOperation()
    {
        return this.operation;
    }
}
