package de.hybris.platform.cockpit.components.mvc.tree.view;

import de.hybris.platform.cockpit.components.mvc.tree.listeners.AddChildNodeListener;
import de.hybris.platform.cockpit.components.mvc.tree.listeners.AddNodeListener;
import de.hybris.platform.cockpit.components.mvc.tree.listeners.DeleteListener;
import org.zkoss.lang.Objects;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

public class DefaultNodeWithActionsRenderer extends AbstractNodeRenderer
{
    public DefaultNodeWithActionsRenderer()
    {
    }


    public DefaultNodeWithActionsRenderer(EventListener dropListener)
    {
        super(dropListener);
    }


    public Treerow renderRow(Treeitem treeItem, Object node)
    {
        Treecell labelCell = new Treecell(Objects.toString(node));
        Treecell actionsCell = new Treecell();
        addActions(actionsCell, node);
        Treerow treeRow = null;
        treeItem.setValue(node);
        if(treeItem.getTreerow() == null)
        {
            treeRow = new Treerow();
            treeRow.setParent((Component)treeItem);
        }
        else
        {
            treeRow = treeItem.getTreerow();
            treeRow.getChildren().clear();
        }
        labelCell.setParent((Component)treeRow);
        actionsCell.setParent((Component)treeRow);
        return treeRow;
    }


    protected void addActions(Treecell actionsCell, Object data)
    {
        Div actionsCellCnt = new Div();
        actionsCellCnt.setParent((Component)actionsCell);
        Button deleteButton = new Button("x");
        deleteButton.addEventListener("onClick", (EventListener)new DeleteListener());
        actionsCellCnt.appendChild((Component)deleteButton);
        Button addButton = new Button("add");
        addButton.addEventListener("onClick", (EventListener)new AddNodeListener());
        actionsCellCnt.appendChild((Component)addButton);
        Button addChild = new Button("add child");
        addChild.addEventListener("onClick", (EventListener)new AddChildNodeListener());
        actionsCellCnt.appendChild((Component)addChild);
    }
}
