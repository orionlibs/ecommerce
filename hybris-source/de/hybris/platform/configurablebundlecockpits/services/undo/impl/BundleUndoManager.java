package de.hybris.platform.configurablebundlecockpits.services.undo.impl;

import de.hybris.platform.cockpit.model.undo.UndoableOperation;
import de.hybris.platform.cockpit.model.undo.impl.CannotRedoException;
import de.hybris.platform.cockpit.model.undo.impl.CannotUndoException;
import de.hybris.platform.cockpit.model.undo.impl.ItemChangeUndoableOperation;
import de.hybris.platform.cockpit.services.undo.UndoManager;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class BundleUndoManager implements UndoManager
{
    private TypeService typeService;
    private final int maxSize;
    private final LinkedList<UndoableOperation> undoStack;
    private final LinkedList<UndoableOperation> redoStack;


    public BundleUndoManager(int size)
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
        if(operation instanceof ItemChangeUndoableOperation)
        {
            AttributeDescriptorModel attrDescriptor = getTypeService().getAttributeDescriptor("BundleTemplate", "bundleSelectionCriteria");
            String attrName = attrDescriptor.getName();
            ItemChangeUndoableOperation undoOp = (ItemChangeUndoableOperation)operation;
            String undoPresentationName = undoOp.getUndoPresentationName();
            if(attrName != null && attrName.equals(undoPresentationName))
            {
                return;
            }
        }
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
        return (operation != null && operation.canUndo());
    }


    public boolean canRedo()
    {
        UndoableOperation operation = this.redoStack.peek();
        return (operation != null && operation.canRedo());
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


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
