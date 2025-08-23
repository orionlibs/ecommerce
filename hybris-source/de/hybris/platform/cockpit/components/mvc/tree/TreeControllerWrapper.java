package de.hybris.platform.cockpit.components.mvc.tree;

import java.util.Set;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Treeitem;

public class TreeControllerWrapper<T> implements TreeController<T>
{
    protected final TreeController<T> controller;


    public TreeControllerWrapper(TreeController<T> controllerToWrap)
    {
        this.controller = controllerToWrap;
    }


    public void add(Tree tree, Object object, T target)
    {
        this.controller.add(tree, object, target);
    }


    public void add(Tree tree, Object object, T target, int index)
    {
        this.controller.add(tree, object, target);
    }


    public T create(Tree tree, T target)
    {
        return (T)this.controller.create(tree, target);
    }


    public T create(Tree tree, T target, int index)
    {
        return (T)this.controller.create(tree, target);
    }


    public Object customAction(Tree tree, Event event, T node)
    {
        return this.controller.customAction(tree, event, node);
    }


    public void delete(Tree tree, T node)
    {
        this.controller.delete(tree, node);
    }


    public void doubleClicked(Tree tree, T node)
    {
        this.controller.doubleClicked(tree, node);
    }


    public Set<T> getSelected()
    {
        return this.controller.getSelected();
    }


    public void move(Tree tree, T node, T target, boolean addAsChild)
    {
        this.controller.move(tree, node, target, addAsChild);
    }


    public void selected(Tree tree, Set<Treeitem> selectedItems)
    {
        this.controller.selected((Component)tree, selectedItems);
    }
}
