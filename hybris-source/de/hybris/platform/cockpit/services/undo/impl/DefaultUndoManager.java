package de.hybris.platform.cockpit.services.undo.impl;

import de.hybris.platform.cockpit.model.undo.UndoableOperation;
import de.hybris.platform.cockpit.model.undo.impl.CannotRedoException;
import de.hybris.platform.cockpit.model.undo.impl.CannotUndoException;
import de.hybris.platform.cockpit.services.undo.UndoManager;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DefaultUndoManager implements UndoManager
{
    private final int maxSize;
    private final LinkedList<UndoableOperation> undoStack;
    private final LinkedList<UndoableOperation> redoStack;


    public DefaultUndoManager()
    {
        this(20);
    }


    public DefaultUndoManager(int size)
    {
        this.maxSize = size;
        this.undoStack = new LinkedList<>();
        this.redoStack = new LinkedList<>();
    }


    public int getMaxSize()
    {
        return this.maxSize;
    }


    public synchronized void addOperation(UndoableOperation operation)
    {
        if(this.undoStack.size() >= this.maxSize)
        {
            this.undoStack.pollLast();
        }
        this.undoStack.push(operation);
        this.redoStack.clear();
    }


    public boolean canUndo()
    {
        UndoableOperation operation = this.undoStack.peek();
        return (operation == null) ? false : operation.canUndo();
    }


    public boolean canRedo()
    {
        UndoableOperation operation = this.redoStack.peek();
        return (operation == null) ? false : operation.canRedo();
    }


    public synchronized void undo() throws CannotUndoException
    {
        if(canUndo())
        {
            UndoableOperation operation = this.undoStack.pop();
            operation.undo();
            this.redoStack.push(operation);
        }
        else
        {
            throw new CannotUndoException("Unable to perform undo.");
        }
    }


    public synchronized void redo() throws CannotRedoException
    {
        if(canRedo())
        {
            UndoableOperation operation = this.redoStack.pop();
            operation.redo();
            this.undoStack.push(operation);
        }
        else
        {
            throw new CannotRedoException("Unable to perform redo.");
        }
    }


    public UndoableOperation peekUndoOperation()
    {
        return this.undoStack.peek();
    }


    public UndoableOperation peekRedoOperation()
    {
        return this.redoStack.peek();
    }


    public List<UndoableOperation> getUndoOperations()
    {
        return Collections.unmodifiableList(this.undoStack);
    }


    public List<UndoableOperation> getRedoOperations()
    {
        return Collections.unmodifiableList(this.redoStack);
    }


    public String getUndoPresentationName()
    {
        UndoableOperation operation = this.undoStack.peek();
        return (operation == null) ? null : operation.getUndoPresentationName();
    }


    public String getRedoPresentationName()
    {
        UndoableOperation operation = this.redoStack.peek();
        return (operation == null) ? null : operation.getRedoPresentationName();
    }


    public synchronized void clear()
    {
        this.undoStack.clear();
        this.redoStack.clear();
    }
}
