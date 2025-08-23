package de.hybris.platform.cockpit.components.mvc.tree;

import de.hybris.platform.cockpit.components.mvc.SelectableComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Treeitem;

public interface TreeController<T> extends SelectableComponent<Tree, Treeitem, T>
{
    void move(Tree paramTree, T paramT1, T paramT2, boolean paramBoolean);


    void add(Tree paramTree, Object paramObject, T paramT, int paramInt);


    void add(Tree paramTree, Object paramObject, T paramT);


    T create(Tree paramTree, T paramT);


    T create(Tree paramTree, T paramT, int paramInt);


    void delete(Tree paramTree, T paramT);


    void doubleClicked(Tree paramTree, T paramT);


    Object customAction(Tree paramTree, Event paramEvent, T paramT);
}
