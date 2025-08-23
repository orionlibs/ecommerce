package de.hybris.platform.cockpit.model.undo.impl;

import de.hybris.platform.cockpit.model.undo.UndoableOperation;
import java.util.Collections;
import java.util.List;
import org.zkoss.util.resource.Labels;

public class BulkUndoableOperation implements UndoableOperation
{
    final List<UndoableOperation> operations;
    boolean showOperationCount = true;


    public BulkUndoableOperation(List<UndoableOperation> operations)
    {
        this.operations = (operations == null) ? Collections.EMPTY_LIST : Collections.<UndoableOperation>unmodifiableList(operations);
    }


    public void setShowOperationCount(boolean showOperationCount)
    {
        this.showOperationCount = showOperationCount;
    }


    public boolean isShowOperationCount()
    {
        return this.showOperationCount;
    }


    public List<UndoableOperation> getOperations()
    {
        return this.operations;
    }


    public boolean canUndo()
    {
        for(UndoableOperation operation : this.operations)
        {
            if(!operation.canUndo())
            {
                return false;
            }
        }
        return true;
    }


    public boolean canRedo()
    {
        for(UndoableOperation operation : this.operations)
        {
            if(!operation.canRedo())
            {
                return false;
            }
        }
        return true;
    }


    public String getUndoPresentationName()
    {
        UndoableOperation operation = getFirstOperation();
        return (operation == null) ? "" : (
                        operation.getUndoPresentationName() + operation.getUndoPresentationName());
    }


    public String getRedoPresentationName()
    {
        UndoableOperation operation = getFirstOperation();
        return (operation == null) ? "" : (
                        operation.getRedoPresentationName() + operation.getRedoPresentationName());
    }


    public void undo() throws CannotUndoException
    {
        if(!this.operations.isEmpty())
        {
            for(int i = this.operations.size() - 1; i >= 0; i--)
            {
                ((UndoableOperation)this.operations.get(i)).undo();
            }
        }
    }


    public void redo() throws CannotRedoException
    {
        if(!this.operations.isEmpty())
        {
            for(int i = 0; i < this.operations.size(); i++)
            {
                ((UndoableOperation)this.operations.get(i)).redo();
            }
        }
    }


    private UndoableOperation getFirstOperation()
    {
        return this.operations.isEmpty() ? null : this.operations.get(0);
    }


    public String getRedoContextDescription()
    {
        return getUndoContextDescription();
    }


    public String getUndoContextDescription()
    {
        return Labels.getLabel("undoredocomponent.multiple");
    }
}
