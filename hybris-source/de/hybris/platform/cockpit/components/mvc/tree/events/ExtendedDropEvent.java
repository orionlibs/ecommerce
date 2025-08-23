package de.hybris.platform.cockpit.components.mvc.tree.events;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;

public class ExtendedDropEvent extends DropEvent
{
    private final boolean addAsChild;
    private final boolean append;


    public ExtendedDropEvent(String name, Component target, Component dragged, int x_Position, int y_Position, int keys, boolean addAsChild)
    {
        this(name, target, dragged, x_Position, y_Position, keys, addAsChild, false);
    }


    public ExtendedDropEvent(String name, Component target, Component dragged, int x_Position, int y_Position, int keys, boolean addAsChild, boolean append)
    {
        super(name, target, dragged, x_Position, y_Position, keys);
        this.addAsChild = addAsChild;
        this.append = append;
    }


    public boolean isAddAsChild()
    {
        return this.addAsChild;
    }


    public boolean isAppend()
    {
        return this.append;
    }
}
