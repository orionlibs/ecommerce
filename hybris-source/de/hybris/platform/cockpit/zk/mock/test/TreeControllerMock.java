package de.hybris.platform.cockpit.zk.mock.test;

import de.hybris.platform.cockpit.components.mvc.tree.Tree;
import de.hybris.platform.cockpit.components.mvc.tree.TreeController;
import java.util.Set;
import org.zkoss.zk.ui.event.Event;

public class TreeControllerMock implements TreeController<Object>
{
    public Set<Object> getSelected()
    {
        return null;
    }


    public void selected(Tree tree, Set selectedItems)
    {
    }


    public void move(Tree tree, Object node, Object target, boolean addAsChild)
    {
    }


    public void add(Tree tree, Object object, Object target, int index)
    {
    }


    public void add(Tree tree, Object object, Object target)
    {
    }


    public Object create(Tree tree, Object target)
    {
        return null;
    }


    public Object create(Tree tree, Object target, int index)
    {
        return null;
    }


    public void delete(Tree tree, Object node)
    {
    }


    public void doubleClicked(Tree tree, Object node)
    {
    }


    public Object customAction(Tree tree, Event event, Object node)
    {
        return null;
    }
}
