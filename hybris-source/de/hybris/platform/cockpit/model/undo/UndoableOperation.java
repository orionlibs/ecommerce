package de.hybris.platform.cockpit.model.undo;

import de.hybris.platform.cockpit.model.undo.impl.CannotRedoException;
import de.hybris.platform.cockpit.model.undo.impl.CannotUndoException;

public interface UndoableOperation
{
    void undo() throws CannotUndoException;


    boolean canUndo();


    void redo() throws CannotRedoException;


    boolean canRedo();


    String getUndoPresentationName();


    String getRedoPresentationName();


    String getUndoContextDescription();


    String getRedoContextDescription();
}
