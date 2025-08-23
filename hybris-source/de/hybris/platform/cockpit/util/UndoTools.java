package de.hybris.platform.cockpit.util;

import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.UndoAddedEvent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.undo.UndoableOperation;
import de.hybris.platform.cockpit.model.undo.impl.BulkUndoableOperation;
import de.hybris.platform.cockpit.model.undo.impl.CannotRedoException;
import de.hybris.platform.cockpit.model.undo.impl.CannotUndoException;
import de.hybris.platform.cockpit.services.undo.UndoManager;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.ItemModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UndoTools
{
    public static void undo(UndoManager undoManager, UndoableOperation operation) throws CannotUndoException
    {
        List<UndoableOperation> undoOperations = undoManager.getUndoOperations();
        int undoCount = 0;
        if(operation != null)
        {
            for(UndoableOperation undoOperation : undoOperations)
            {
                undoCount++;
                if(operation.equals(undoOperation))
                {
                    break;
                }
            }
        }
        for(int i = 0; i < undoCount; i++)
        {
            if(undoManager.canUndo())
            {
                undoManager.undo();
            }
        }
    }


    public static void redo(UndoManager undoManager, UndoableOperation operation) throws CannotRedoException
    {
        List<UndoableOperation> redoOperations = undoManager.getRedoOperations();
        int redoCount = 0;
        if(operation != null)
        {
            for(UndoableOperation redoOperation : redoOperations)
            {
                redoCount++;
                if(operation.equals(redoOperation))
                {
                    break;
                }
            }
        }
        for(int i = 0; i < redoCount; i++)
        {
            if(undoManager.canRedo())
            {
                undoManager.redo();
            }
        }
    }


    public static void addUndoOperationAndEvent(UndoManager undoManager, UndoableOperation operation, Object eventSource)
    {
        if(isUndoGrouping())
        {
            addOperationToGroup(operation);
        }
        else
        {
            addOperationAndEventInternal(undoManager, operation, eventSource);
        }
    }


    private static ThreadLocal<Boolean> isGrouping = new ThreadLocal<>();
    private static ThreadLocal<List<UndoableOperation>> groupingStack = new ThreadLocal<>();


    public static void startUndoGrouping()
    {
        isGrouping.set(Boolean.TRUE);
        groupingStack.set(null);
    }


    public static void stopUndoGrouping(UndoManager undoManager, Object eventSource)
    {
        stopUndoGrouping(undoManager, eventSource, null);
    }


    public static void stopUndoGrouping(UndoManager undoManager, Object eventSource, BulkUndoableOperation bulk)
    {
        isGrouping.set(Boolean.FALSE);
        List<UndoableOperation> operations = groupingStack.get();
        if(operations != null && !operations.isEmpty())
        {
            addOperationAndEventInternal(undoManager, (bulk == null) ? (UndoableOperation)new BulkUndoableOperation(operations) : (UndoableOperation)bulk, eventSource);
        }
        groupingStack.set(null);
    }


    public static List<UndoableOperation> getGroupedOperations()
    {
        List<UndoableOperation> operations = groupingStack.get();
        return (operations == null) ? Collections.EMPTY_LIST : Collections.<UndoableOperation>unmodifiableList(operations);
    }


    public static void discardUndoGrouping()
    {
        isGrouping.set(Boolean.FALSE);
        groupingStack.set(null);
    }


    private static boolean isUndoGrouping()
    {
        Boolean grouping = isGrouping.get();
        return (grouping != null && grouping.booleanValue());
    }


    private static void addOperationToGroup(UndoableOperation operation)
    {
        List<UndoableOperation> list = groupingStack.get();
        if(list == null)
        {
            list = new ArrayList<>();
            groupingStack.set(list);
        }
        list.add(operation);
    }


    public static void addOperationAndEventInternal(UndoManager undoManager, UndoableOperation operation, Object eventSource)
    {
        undoManager.addOperation(operation);
        UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new UndoAddedEvent(eventSource, undoManager.peekUndoOperation()));
    }


    public static boolean isItemValid(TypedObject typedObject)
    {
        if(typedObject != null)
        {
            Object modelObject = typedObject.getObject();
            if(modelObject != null)
            {
                if(modelObject instanceof ItemModel)
                {
                    ItemModel model = (ItemModel)modelObject;
                    if(!UISessionUtils.getCurrentSession().getModelService().isRemoved(model))
                    {
                        return true;
                    }
                }
                else
                {
                    return true;
                }
            }
        }
        return false;
    }
}
