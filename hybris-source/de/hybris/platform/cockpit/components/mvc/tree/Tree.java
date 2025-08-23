package de.hybris.platform.cockpit.components.mvc.tree;

import de.hybris.platform.cockpit.components.mvc.tree.listeners.DropListener;
import de.hybris.platform.cockpit.components.mvc.tree.listeners.SelectListener;
import de.hybris.platform.cockpit.components.mvc.tree.view.DefaultNodeRenderer;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.TreeitemRenderer;

public class Tree<T> extends Tree
{
    private static final long serialVersionUID = 1L;
    private TreeController<T> controller;


    public Tree()
    {
        setTreeitemRenderer((TreeitemRenderer)new DefaultNodeRenderer((EventListener)new DropListener()));
        addEventListener("onSelect", (EventListener)new SelectListener());
        addEventListener("onTreeItemsWereSelected", (EventListener)new SelectHandler(this));
        addEventListener("onSomethingWasDropped", (EventListener)new DropHandler(this));
        addEventListener("onKeyPressed", (EventListener)new KeyStrokeHandler(this));
        addEventListener("onDeleteIconWasClicked", (EventListener)new DeleteHandler(this));
        addEventListener("onCreateNode", (EventListener)new CreateHandler(this));
        addEventListener("onCreateChild", (EventListener)new CreateHandler(this));
        addEventListener("onDoubleClicked", (EventListener)new DoubleClickHandler(this));
        addEventListener("onCustomAction", (EventListener)new CustomActionHandler(this));
    }


    public Tree(TreeController<T> controller)
    {
        this.controller = controller;
        setTreeitemRenderer((TreeitemRenderer)new DefaultNodeRenderer((EventListener)new DropListener()));
        addEventListener("onSelect", (EventListener)new SelectListener());
        addEventListener("onTreeItemsWereSelected", (EventListener)new SelectHandler(this));
        addEventListener("onSomethingWasDropped", (EventListener)new DropHandler(this));
        addEventListener("onKeyPressed", (EventListener)new KeyStrokeHandler(this));
        addEventListener("onDeleteIconWasClicked", (EventListener)new DeleteHandler(this));
        addEventListener("onCreateNode", (EventListener)new CreateHandler(this));
        addEventListener("onCreateChild", (EventListener)new CreateHandler(this));
        addEventListener("onDoubleClicked", (EventListener)new DoubleClickHandler(this));
        addEventListener("onCustomAction", (EventListener)new CustomActionHandler(this));
    }


    public void setController(TreeController<T> controller)
    {
        this.controller = controller;
    }
}
