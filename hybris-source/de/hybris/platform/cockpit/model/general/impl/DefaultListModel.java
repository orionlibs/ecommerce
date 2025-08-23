package de.hybris.platform.cockpit.model.general.impl;

import de.hybris.platform.cockpit.components.listeditor.DefaultListSelectionModel;
import de.hybris.platform.cockpit.components.listeditor.ListSelectionModel;
import de.hybris.platform.cockpit.model.listview.AbstractListModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class DefaultListModel<T> extends AbstractListModel<T>
{
    protected List<T> elements = null;
    protected ListSelectionModel movableElements = null;
    protected ListSelectionModel removableElements = null;
    protected boolean editable = true;


    public DefaultListModel()
    {
        this.elements = new ArrayList<>();
        this.movableElements = (ListSelectionModel)new DefaultListSelectionModel();
        this.removableElements = (ListSelectionModel)new DefaultListSelectionModel();
    }


    public void add(T element)
    {
        add(element, false, false);
    }


    public void add(T element, boolean removable, boolean movable)
    {
        int size = this.elements.size();
        this.elements.add(element);
        if(removable)
        {
            this.removableElements.addSelectionInterval(size, size);
        }
        if(movable)
        {
            this.movableElements.addSelectionInterval(size, size);
        }
        fireEvent(1, size, size);
    }


    public void add(int index, T element) throws IndexOutOfBoundsException
    {
        add(index, element, false, false);
    }


    public void add(int index, T element, boolean removable, boolean movable) throws IndexOutOfBoundsException
    {
        this.elements.add(index, element);
        this.removableElements.insertIndexRange(index, 1);
        this.movableElements.insertIndexRange(index, 1);
        if(removable)
        {
            this.removableElements.addSelectionInterval(index, index);
        }
        if(movable)
        {
            this.movableElements.addSelectionInterval(index, index);
        }
        fireEvent(1, index, index);
    }


    public void addAll(Collection<T> elements)
    {
        addAll(elements, false, false);
    }


    public void addAll(Collection<T> elements, boolean removable, boolean movable)
    {
        int size = this.elements.size();
        this.elements.addAll(elements);
        if(removable)
        {
            this.removableElements.addSelectionInterval(size, elements.size() - 1);
        }
        if(movable)
        {
            this.movableElements.addSelectionInterval(size, elements.size() - 1);
        }
        fireEvent(1, size, size + elements.size() - 1);
    }


    public void clearAndAddAll(Collection<? extends T> elements, boolean removable, boolean movable)
    {
        clearAndAddAll(elements, removable, movable, true);
    }


    public void clearAndAddAll(Collection<? extends T> elements, boolean removable, boolean movable, boolean fireEvent)
    {
        int size = this.elements.size();
        if(size > 0)
        {
            this.elements.clear();
            this.removableElements.clearSelection();
            this.movableElements.clearSelection();
        }
        this.elements.addAll(elements);
        if(removable)
        {
            this.removableElements.addSelectionInterval(0, elements.size() - 1);
        }
        if(movable)
        {
            this.movableElements.addSelectionInterval(0, elements.size() - 1);
        }
        if(fireEvent)
        {
            fireEvent(0, 0, size);
        }
    }


    public void clearAndAddAll(Collection<T> elements)
    {
        clearAndAddAll(elements, false, false);
    }


    public void set(int index, T element) throws IndexOutOfBoundsException
    {
        set(index, element, false, false);
    }


    public T set(int index, T element, boolean removable, boolean movable)
    {
        T old = null;
        old = this.elements.set(index, element);
        if(removable)
        {
            if(!this.removableElements.isSelectedIndex(index))
            {
                this.removableElements.toggleSelectionInterval(index, index);
            }
        }
        else if(this.removableElements.isSelectedIndex(index))
        {
            this.removableElements.toggleSelectionInterval(index, index);
        }
        if(movable)
        {
            if(!this.movableElements.isSelectedIndex(index))
            {
                this.movableElements.toggleSelectionInterval(index, index);
            }
        }
        else if(this.movableElements.isSelectedIndex(index))
        {
            this.movableElements.toggleSelectionInterval(index, index);
        }
        fireEvent(0, index, index);
        return old;
    }


    public void removeRange(int fromIndex, int toIndex) throws ArrayIndexOutOfBoundsException, IllegalArgumentException
    {
        if(fromIndex > toIndex)
        {
            throw new IllegalArgumentException("fromIndex can not be greater than toIndex (fromIndex='" + fromIndex + "', toIndex='" + toIndex + "')");
        }
        int size = this.elements.size();
        if(fromIndex >= size)
        {
            throw new ArrayIndexOutOfBoundsException("fromIndex is greater than or equal to the list's size (fromIndex='" + fromIndex + "', size='" + size + "')");
        }
        if(toIndex >= size)
        {
            throw new ArrayIndexOutOfBoundsException("toIndex is greater than or equal to the list's size (toIndex='" + toIndex + "', size='" + size + "')");
        }
        this.removableElements.removeIndexRange(fromIndex, toIndex - fromIndex + 1);
        this.movableElements.removeIndexRange(fromIndex, toIndex - fromIndex + 1);
        int index = fromIndex;
        for(Iterator<T> it = this.elements.listIterator(fromIndex); it.hasNext() && index <= toIndex; index++)
        {
            it.next();
            it.remove();
        }
        fireEvent(2, fromIndex, index);
    }


    public T remove(int index) throws IndexOutOfBoundsException
    {
        T old = null;
        old = this.elements.remove(index);
        this.removableElements.removeIndexRange(index, 1);
        this.movableElements.removeIndexRange(index, 1);
        fireEvent(2, index, index);
        return old;
    }


    public boolean remove(T element)
    {
        boolean success = false;
        int index = this.elements.indexOf(element);
        success = this.elements.remove(element);
        if(index >= 0)
        {
            this.removableElements.removeIndexRange(index, 1);
            this.movableElements.removeIndexRange(index, 1);
        }
        fireEvent(2, index, index);
        return success;
    }


    public void removeAll(Collection<T> elements)
    {
        for(T element : elements)
        {
            remove(element);
        }
    }


    public void clear()
    {
        if(!this.elements.isEmpty())
        {
            int size = this.elements.size();
            this.elements.clear();
            this.removableElements.clearSelection();
            this.movableElements.clearSelection();
            fireEvent(2, 0, size - 1);
        }
    }


    public T elementAt(int index) throws IndexOutOfBoundsException
    {
        return this.elements.get(index);
    }


    public List<T> getElements()
    {
        return this.elements;
    }


    public int size()
    {
        return this.elements.size();
    }


    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }


    public boolean isEditable()
    {
        return this.editable;
    }


    public void setMovable(int index, boolean movable)
    {
        if(index >= this.movableElements.getMinSelectionIndex() && index <= this.movableElements.getMaxSelectionIndex())
        {
            if((movable && !this.movableElements.isSelectedIndex(index)) || (!movable && this.movableElements
                            .isSelectedIndex(index)))
            {
                this.movableElements.toggleSelectionInterval(index, index);
            }
        }
    }


    public boolean isMovable(int index) throws IndexOutOfBoundsException
    {
        return (isEditable() && this.movableElements.isSelectedIndex(index));
    }


    public void setRemovable(int index, boolean removable)
    {
        if(index >= this.removableElements.getMinSelectionIndex() && index <= this.removableElements.getMaxSelectionIndex())
        {
            if((removable && !this.removableElements.isSelectedIndex(index)) || (!removable && this.removableElements
                            .isSelectedIndex(index)))
            {
                this.removableElements.toggleSelectionInterval(index, index);
            }
        }
    }


    public boolean isRemovable(int index) throws IndexOutOfBoundsException
    {
        return (isEditable() && this.removableElements.isSelectedIndex(index));
    }


    public void setMovableElementsModel(ListSelectionModel model)
    {
        this.movableElements = model;
    }


    public void setRemovableElementsModel(ListSelectionModel model)
    {
        this.removableElements = model;
    }


    public void moveElement(int fromIndex, int toIndex)
    {
        if(fromIndex >= 0 && fromIndex < size() && toIndex >= 0 && toIndex < size())
        {
            boolean removable = this.removableElements.isSelectedIndex(fromIndex);
            boolean movable = this.movableElements.isSelectedIndex(fromIndex);
            T element = elementAt(fromIndex);
            if(fromIndex < toIndex)
            {
                add(toIndex, element, removable, movable);
                remove(fromIndex);
            }
            else
            {
                remove(fromIndex);
                add(toIndex, element, removable, movable);
            }
        }
    }


    public String toString()
    {
        return getClass().getCanonicalName() + "(" + getClass().getCanonicalName() + ")[editable='" + hashCode() + "', size='" + this.editable + "']";
    }


    public boolean isEmpty()
    {
        return (this.elements == null || this.elements.isEmpty());
    }
}
