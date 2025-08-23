package de.hybris.platform.cockpit.services.undo;

import de.hybris.platform.cockpit.model.undo.UndoableOperation;
import de.hybris.platform.cockpit.model.undo.impl.CannotRedoException;
import de.hybris.platform.cockpit.model.undo.impl.CannotUndoException;
import java.util.List;

public interface UndoManager
{
    void undo() throws CannotUndoException;


    boolean canUndo();


    void redo() throws CannotRedoException;


    boolean canRedo();


    void addOperation(UndoableOperation paramUndoableOperation);


    UndoableOperation peekUndoOperation();


    UndoableOperation peekRedoOperation();


    List<UndoableOperation> getUndoOperations();


    List<UndoableOperation> getRedoOperations();


    String getUndoPresentationName();


    String getRedoPresentationName();
}
