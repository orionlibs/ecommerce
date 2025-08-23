package de.hybris.platform.cockpit.events.impl;

import de.hybris.platform.cockpit.model.undo.UndoableOperation;

public class UndoRedoEvent extends AbstractUndoRedoEvent
{
    private final boolean undo;


    public UndoRedoEvent(Object source, UndoableOperation operation, boolean undo)
    {
        super(source, operation);
        this.undo = undo;
    }


    public boolean isUndo()
    {
        return this.undo;
    }
}
